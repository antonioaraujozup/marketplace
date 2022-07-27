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

import javax.validation.Valid;
import java.math.BigDecimal;
import java.net.URI;
import java.util.Optional;

@RestController
public class NovaVendaController {

    Logger logger = LoggerFactory.getLogger(NovaVendaController.class);

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
    public ResponseEntity<?> comprar(@RequestBody @Valid NovaVendaRequest request,
                                     UriComponentsBuilder uriComponentsBuilder) {

        // Acessa o microsserviço de Gerenciamento de Usuários para buscar os dados do usuário.
        UsuarioResponse usuarioResponse = buscarUsuario(request.getUsuario())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "O usuário de id = %s não foi encontrado".formatted(request.getUsuario())));

        // Cria o objeto venda.
        Venda venda = new Venda(request.getUsuario());

        // Acessa o microsserviço de Catálogo de Produtos para buscar os dados do(s) produto(s).
        // Preenche o objeto venda com os produtos (itens).
        request.getProdutos().forEach(
                produto -> {
                    ProdutoResponse produtoResponse = buscarProduto(produto.getId())
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                    "O produto de id = %s não foi encontrado".formatted(produto.getId())));

                    venda.adicionaItemVenda(produtoResponse.toItemVenda(produto.getQuantidade()));
                }
        );

        // Calcula o valor total da venda.
        BigDecimal valorTotalVenda = venda.calculaValorTotal();

        // Submete venda para o Serviço de Pagamentos.
        PagamentoResponse pagamentoResponse = submeterPagamento(new PagamentoRequest(request.getPagamento(), valorTotalVenda));

        // Atualiza o objeto venda com os dados do pagamento.
        venda.adicionaDadosPagamento(pagamentoResponse.toPagamento());

        // Salva a venda no banco de dados.
        try {
            vendaRepository.save(venda);

            logger.info("{} cadastrada no banco de dados", venda.toString());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Não foi possível realizar a venda. Erro ao salvar no banco de dados");
        }

        // Se o pagamento foi aprovado, um evento é inserido no tópico Venda.
        if(venda.pagamentoAprovado()) {
            kafkaProducerService.insereEventoNoTopico(new VendaDto(venda,usuarioResponse));
        }

        // Gera a URI de location.
        URI location = uriComponentsBuilder.path("/vendas/{codigoPedido}")
                .buildAndExpand(venda.getCodigoPedido())
                .toUri();

        // Retorna HTTP STATUS 201 CREATED
        // Location no cabeçalho e status do pagamento no corpo da resposta.
        return ResponseEntity
                .created(location)
                .body(new StatusPagamentoResponse(venda.retornaStatusPagamento()));

    }

    private Optional<UsuarioResponse> buscarUsuario(Long idUsuario) {

        try {
            Optional<UsuarioResponse> usuarioResponse = gerenciamentoUsuariosClient.buscarUsuario(idUsuario);

            return usuarioResponse;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Não foi possível realizar a venda. Erro ao buscar o usuário de id = %s".formatted(idUsuario));
        }

    }

    private Optional<ProdutoResponse> buscarProduto(Long idProduto) {

        try {
            Optional<ProdutoResponse> produtoResponse = catalogoProdutosClient.buscarProduto(idProduto);

            return produtoResponse;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Não foi possível realizar a venda. Erro ao buscar o produto de id = %s".formatted(idProduto));
        }

    }

    private PagamentoResponse submeterPagamento(PagamentoRequest pagamentoRequest) {

        try {
            PagamentoResponse pagamentoResponse = servicoPagamentosClient.submeterPagamento(pagamentoRequest);

            return pagamentoResponse;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Não foi possível realizar a venda. Erro ao processar o pagamento");
        }

    }

}
