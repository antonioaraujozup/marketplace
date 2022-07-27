package br.com.zup.edu.commerce.marketplace.compra;

import br.com.zup.edu.commerce.marketplace.clients.*;
import br.com.zup.edu.commerce.marketplace.service.KafkaProducerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;

@RestController
public class NovaCompraController {

    Logger logger = LoggerFactory.getLogger(NovaCompraController.class);

    @Autowired
    private GerenciamentoUsuariosClient gerenciamentoUsuariosClient;

    @Autowired
    private CatalogoProdutosClient catalogoProdutosClient;

    @Autowired
    private ServicoPagamentosClient servicoPagamentosClient;

    @Autowired
    private VendaRepository vendaRepository;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @PostMapping("/vendas")
    @Transactional
    public ResponseEntity<?> comprar(@RequestBody @Valid NovaCompraRequest request,
                                     UriComponentsBuilder uriComponentsBuilder) {

        // Acessa o microsserviço de Gerenciamento de Usuários para buscar os dados do usuário.
        UsuarioResponse usuarioResponse = gerenciamentoUsuariosClient.buscarUsuario(request.getUsuario())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "O usuário de id = %s não foi encontrado".formatted(request.getUsuario())));

        // Cria o objeto venda.
        Venda venda = new Venda(request.getUsuario());

        // Acessa o microsserviço de Catálogo de Produtos para buscar os dados do(s) produto(s).
        // Preenche o objeto venda com os produtos (itens).
        request.getProdutos().forEach(
                produto -> {
                    ProdutoResponse produtoResponse = catalogoProdutosClient.buscarProduto(produto.getId())
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                    "O produto de id = %s não foi encontrado".formatted(produto.getId())));

                    venda.adicionaItemVenda(produtoResponse.toItemVenda(produto.getQuantidade()));
                }
        );

        // Submete venda para o Serviço de Pagamentos.
        PagamentoResponse pagamentoResponse = servicoPagamentosClient.submeterPagamento(new PagamentoRequest(request.getPagamento(), venda));

        // Atualiza o objeto venda com os dados do pagamento.
        venda.adicionaDadosPagamento(pagamentoResponse.toPagamento());

        // Salva a venda (e os itens da venda) no banco de dados.
        vendaRepository.save(venda);

        logger.info("{} cadastrada no banco de dados", venda.toString());

        // Se o pagamento foi aprovado, um evento é inserido no tópico Venda.
        if(venda.pagamentoAprovado()) {
            kafkaProducerService.insereEventoNoTopico(new VendaDto(venda,usuarioResponse));
        }

        // Gera a URI de location.
        URI location = uriComponentsBuilder.path("/vendas/{codigoPedido}")
                .buildAndExpand(venda.getCodigoPedido())
                .toUri();

        // Retorna HTTP STATUS 201 CREATED.
        return ResponseEntity
                .created(location)
                .body(new StatusPagamentoResponse(venda.retornaStatusPagamento()));

    }

}
