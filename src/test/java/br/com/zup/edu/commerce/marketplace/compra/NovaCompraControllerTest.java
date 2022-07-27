package br.com.zup.edu.commerce.marketplace.compra;

import br.com.zup.edu.commerce.marketplace.clients.*;
import br.com.zup.edu.commerce.marketplace.exception.MensagemDeErro;
import br.com.zup.edu.commerce.marketplace.service.KafkaProducerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(printOnlyOnFailure = false)
class NovaCompraControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private ItemVendaRepository itemVendaRepository;

    @Autowired
    private VendaRepository vendaRepository;

    @MockBean
    private GerenciamentoUsuariosClient gerenciamentoUsuariosClient;

    @MockBean
    private CatalogoProdutosClient catalogoProdutosClient;

    @MockBean
    private ServicoPagamentosClient servicoPagamentosClient;

    @MockBean
    private KafkaProducerService kafkaProducerService;

    @BeforeEach
    void setUp() {
        this.itemVendaRepository.deleteAll();
        this.vendaRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve cadastrar uma venda quando pagamento for aprovado")
    void test0() throws Exception {

        // Cenário
        Long usuario = 1L;
        ProdutoVendaRequest produto1 = new ProdutoVendaRequest(1L,2);
        ProdutoVendaRequest produto2 = new ProdutoVendaRequest(2L,3);
        List<ProdutoVendaRequest> produtos = List.of(produto1, produto2);
        DadosPagamentoRequest pagamento = new DadosPagamentoRequest(
                "Jose Humberto Coimbra Jr",
                "5478659832154582",
                YearMonth.parse("2025-08"),
                "429"
        );

        NovaVendaRequest novaVendaRequest = new NovaVendaRequest(usuario,produtos,pagamento);

        String payload = mapper.writeValueAsString(novaVendaRequest);

        UsuarioResponse usuarioResponse = new UsuarioResponse(
                "Jordi Henrique Marques da Silva",
                "61231610085",
                "jordi.silva@zup.com.br",
                "Rua Maria Luíza N. Alencar,Dinamérica,Campina Grande,PB -78068555",
                LocalDate.parse("1997-01-15")
        );

        ProdutoResponse produtoResponse1 = new ProdutoResponse(
                1L,
                "Playstation 5",
                new BigDecimal("5500"),
                LocalDateTime.parse("2022-01-15T15:56:23")
        );

        ProdutoResponse produtoResponse2 = new ProdutoResponse(
                2L,
                "Playstation 4",
                new BigDecimal("3500"),
                LocalDateTime.parse("2022-10-02T22:16:23")
        );

        PagamentoResponse pagamentoResponse = new PagamentoResponse(
                "ab750cbe-da1b-40cd-82a2-e781349fc243",
                StatusPagamento.valueOf("APROVADO")
        );

        Mockito.when(gerenciamentoUsuariosClient.buscarUsuario(usuario))
                .thenReturn(Optional.of(usuarioResponse));

        Mockito.when(catalogoProdutosClient.buscarProduto(Mockito.any()))
                .thenReturn(Optional.of(produtoResponse1), Optional.of(produtoResponse2));

        Mockito.when(servicoPagamentosClient.submeterPagamento(Mockito.any()))
                .thenReturn(pagamentoResponse);

        MockHttpServletRequestBuilder request = post("/vendas")
                .header("Accept-Language", "pt-br")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload);

        // Ação e Corretude
        mockMvc.perform(request)
                .andExpect(
                        status().isCreated()
                )
                .andExpect(
                        redirectedUrlPattern("http://localhost/vendas/*")
                );


        // Asserts
        List<Venda> vendas = this.vendaRepository.findAll();
        List<ItemVenda> itens = this.itemVendaRepository.findAll();
        assertThat(vendas).hasSize(1);
        assertThat(itens).hasSize(2);

    }

    @Test
    @DisplayName("Deve cadastrar uma venda quando pagamento for reprovado")
    void test1() throws Exception {

        // Cenário
        Long usuario = 1L;
        ProdutoVendaRequest produto1 = new ProdutoVendaRequest(1L,2);
        ProdutoVendaRequest produto2 = new ProdutoVendaRequest(2L,3);
        List<ProdutoVendaRequest> produtos = List.of(produto1, produto2);
        DadosPagamentoRequest pagamento = new DadosPagamentoRequest(
                "Jose Humberto Coimbra Jr",
                "5478659832154582",
                YearMonth.parse("2025-08"),
                "429"
        );

        NovaVendaRequest novaVendaRequest = new NovaVendaRequest(usuario,produtos,pagamento);

        String payload = mapper.writeValueAsString(novaVendaRequest);

        UsuarioResponse usuarioResponse = new UsuarioResponse(
                "Jordi Henrique Marques da Silva",
                "61231610085",
                "jordi.silva@zup.com.br",
                "Rua Maria Luíza N. Alencar,Dinamérica,Campina Grande,PB -78068555",
                LocalDate.parse("1997-01-15")
        );

        ProdutoResponse produtoResponse1 = new ProdutoResponse(
                1L,
                "Playstation 5",
                new BigDecimal("5500"),
                LocalDateTime.parse("2022-01-15T15:56:23")
        );

        ProdutoResponse produtoResponse2 = new ProdutoResponse(
                2L,
                "Playstation 4",
                new BigDecimal("3500"),
                LocalDateTime.parse("2022-10-02T22:16:23")
        );

        PagamentoResponse pagamentoResponse = new PagamentoResponse(
                "ab750cbe-da1b-40cd-82a2-e781349fc243",
                StatusPagamento.valueOf("REPROVADO")
        );

        Mockito.when(gerenciamentoUsuariosClient.buscarUsuario(usuario))
                .thenReturn(Optional.of(usuarioResponse));

        Mockito.when(catalogoProdutosClient.buscarProduto(Mockito.any()))
                .thenReturn(Optional.of(produtoResponse1), Optional.of(produtoResponse2));

        Mockito.when(servicoPagamentosClient.submeterPagamento(Mockito.any()))
                .thenReturn(pagamentoResponse);

        MockHttpServletRequestBuilder request = post("/vendas")
                .header("Accept-Language", "pt-br")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload);

        // Ação e Corretude
        mockMvc.perform(request)
                .andExpect(
                        status().isCreated()
                )
                .andExpect(
                        redirectedUrlPattern("http://localhost/vendas/*")
                );


        // Asserts
        List<Venda> vendas = this.vendaRepository.findAll();
        List<ItemVenda> itens = this.itemVendaRepository.findAll();
        assertThat(vendas).hasSize(1);
        assertThat(itens).hasSize(2);

    }

    @Test
    @DisplayName("Não deve cadastrar uma venda com dados nulos")
    void test2() throws Exception {

        // Cenário
        NovaVendaRequest novaVendaRequest = new NovaVendaRequest(null,null,null);

        String payloadRequest = mapper.writeValueAsString(novaVendaRequest);

        MockHttpServletRequestBuilder request = post("/vendas")
                .header("Accept-Language", "pt-br")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payloadRequest);

        // Ação e Corretude
        String payloadResponse = mockMvc.perform(request)
                .andExpect(
                        status().isBadRequest()
                )
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        MensagemDeErro mensagemDeErro = mapper.readValue(payloadResponse, MensagemDeErro.class);

        // Asserts
        List<Venda> vendas = this.vendaRepository.findAll();
        List<ItemVenda> itens = this.itemVendaRepository.findAll();
        assertThat(vendas).hasSize(0);
        assertThat(itens).hasSize(0);
        assertThat(mensagemDeErro.getMensagens())
                .hasSize(3)
                .contains(
                        "O campo usuario não deve ser nulo",
                        "O campo produtos não deve estar vazio",
                        "O campo pagamento não deve ser nulo"
                );

    }

    @Test
    @DisplayName("Não deve cadastrar uma venda com um produto cujos dados são nulos")
    void test3() throws Exception {

        // Cenário
        Long usuario = 1L;
        ProdutoVendaRequest produto1 = new ProdutoVendaRequest(null,null);
        ProdutoVendaRequest produto2 = new ProdutoVendaRequest(2L,3);
        List<ProdutoVendaRequest> produtos = List.of(produto1, produto2);
        DadosPagamentoRequest pagamento = new DadosPagamentoRequest(
                "Jose Humberto Coimbra Jr",
                "5478659832154582",
                YearMonth.parse("2025-08"),
                "429"
        );

        NovaVendaRequest novaVendaRequest = new NovaVendaRequest(usuario,produtos,pagamento);

        String payloadRequest = mapper.writeValueAsString(novaVendaRequest);

        MockHttpServletRequestBuilder request = post("/vendas")
                .header("Accept-Language", "pt-br")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payloadRequest);

        // Ação e Corretude
        String payloadResponse = mockMvc.perform(request)
                .andExpect(
                        status().isBadRequest()
                )
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        MensagemDeErro mensagemDeErro = mapper.readValue(payloadResponse, MensagemDeErro.class);

        // Asserts
        List<Venda> vendas = this.vendaRepository.findAll();
        List<ItemVenda> itens = this.itemVendaRepository.findAll();
        assertThat(vendas).hasSize(0);
        assertThat(itens).hasSize(0);
        assertThat(mensagemDeErro.getMensagens())
                .hasSize(2)
                .contains(
                        "O campo produtos[0].id não deve ser nulo",
                        "O campo produtos[0].quantidade não deve ser nulo"
                );

    }

    @Test
    @DisplayName("Não deve cadastrar uma venda com um produto cuja quantidade é menor ou igual a zero")
    void test4() throws Exception {

        // Cenário
        Long usuario = 1L;
        ProdutoVendaRequest produto1 = new ProdutoVendaRequest(1L,0);
        ProdutoVendaRequest produto2 = new ProdutoVendaRequest(2L,-1);
        List<ProdutoVendaRequest> produtos = List.of(produto1, produto2);
        DadosPagamentoRequest pagamento = new DadosPagamentoRequest(
                "Jose Humberto Coimbra Jr",
                "5478659832154582",
                YearMonth.parse("2025-08"),
                "429"
        );

        NovaVendaRequest novaVendaRequest = new NovaVendaRequest(usuario,produtos,pagamento);

        String payloadRequest = mapper.writeValueAsString(novaVendaRequest);

        MockHttpServletRequestBuilder request = post("/vendas")
                .header("Accept-Language", "pt-br")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payloadRequest);

        // Ação e Corretude
        String payloadResponse = mockMvc.perform(request)
                .andExpect(
                        status().isBadRequest()
                )
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        MensagemDeErro mensagemDeErro = mapper.readValue(payloadResponse, MensagemDeErro.class);

        // Asserts
        List<Venda> vendas = this.vendaRepository.findAll();
        List<ItemVenda> itens = this.itemVendaRepository.findAll();
        assertThat(vendas).hasSize(0);
        assertThat(itens).hasSize(0);
        assertThat(mensagemDeErro.getMensagens())
                .hasSize(2)
                .contains(
                        "O campo produtos[0].quantidade deve ser maior que 0",
                        "O campo produtos[1].quantidade deve ser maior que 0"
                );

    }

    @Test
    @DisplayName("Não deve cadastrar uma venda com dados de pagamento nulos")
    void test5() throws Exception {

        // Cenário
        Long usuario = 1L;
        ProdutoVendaRequest produto1 = new ProdutoVendaRequest(1L,2);
        ProdutoVendaRequest produto2 = new ProdutoVendaRequest(2L,3);
        List<ProdutoVendaRequest> produtos = List.of(produto1, produto2);
        DadosPagamentoRequest pagamento = new DadosPagamentoRequest(
                null,
                null,
                null,
                null
        );

        NovaVendaRequest novaVendaRequest = new NovaVendaRequest(usuario,produtos,pagamento);

        String payloadRequest = mapper.writeValueAsString(novaVendaRequest);

        MockHttpServletRequestBuilder request = post("/vendas")
                .header("Accept-Language", "pt-br")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payloadRequest);

        // Ação e Corretude
        String payloadResponse = mockMvc.perform(request)
                .andExpect(
                        status().isBadRequest()
                )
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        MensagemDeErro mensagemDeErro = mapper.readValue(payloadResponse, MensagemDeErro.class);

        // Asserts
        List<Venda> vendas = this.vendaRepository.findAll();
        List<ItemVenda> itens = this.itemVendaRepository.findAll();
        assertThat(vendas).hasSize(0);
        assertThat(itens).hasSize(0);
        assertThat(mensagemDeErro.getMensagens())
                .hasSize(4)
                .contains(
                        "O campo pagamento.codigoSeguranca não deve estar em branco",
                        "O campo pagamento.validoAte não deve ser nulo",
                        "O campo pagamento.titular não deve estar em branco",
                        "O campo pagamento.numero não deve estar em branco"
                );

    }

    @Test
    @DisplayName("Não deve cadastrar uma venda com dados de pagamento inválidos")
    void test6() throws Exception {

        // Cenário
        Long usuario = 1L;
        ProdutoVendaRequest produto1 = new ProdutoVendaRequest(1L,2);
        ProdutoVendaRequest produto2 = new ProdutoVendaRequest(2L,3);
        List<ProdutoVendaRequest> produtos = List.of(produto1, produto2);
        DadosPagamentoRequest pagamento = new DadosPagamentoRequest(
                "Jose Humberto Coimbra Jr",
                "54786598321545",
                YearMonth.parse("2020-08"),
                "42968"
        );

        NovaVendaRequest novaVendaRequest = new NovaVendaRequest(usuario,produtos,pagamento);

        String payloadRequest = mapper.writeValueAsString(novaVendaRequest);

        MockHttpServletRequestBuilder request = post("/vendas")
                .header("Accept-Language", "pt-br")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payloadRequest);

        // Ação e Corretude
        String payloadResponse = mockMvc.perform(request)
                .andExpect(
                        status().isBadRequest()
                )
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        MensagemDeErro mensagemDeErro = mapper.readValue(payloadResponse, MensagemDeErro.class);

        // Asserts
        List<Venda> vendas = this.vendaRepository.findAll();
        List<ItemVenda> itens = this.itemVendaRepository.findAll();
        assertThat(vendas).hasSize(0);
        assertThat(itens).hasSize(0);
        assertThat(mensagemDeErro.getMensagens())
                .hasSize(3)
                .contains(
                        "O campo pagamento.numero deve corresponder a \"^\\d{16}$\"",
                        "O campo pagamento.validoAte deve ser uma data no presente ou no futuro",
                        "O campo pagamento.codigoSeguranca deve corresponder a \"^\\d{3}$\""
                );

    }

    @Test
    @DisplayName("Não deve cadastrar uma venda quando o usuário não for encontrado")
    void test7() throws Exception {

        // Cenário
        Long usuario = 100L;
        ProdutoVendaRequest produto1 = new ProdutoVendaRequest(1L,2);
        ProdutoVendaRequest produto2 = new ProdutoVendaRequest(2L,3);
        List<ProdutoVendaRequest> produtos = List.of(produto1, produto2);
        DadosPagamentoRequest pagamento = new DadosPagamentoRequest(
                "Jose Humberto Coimbra Jr",
                "5478659832154582",
                YearMonth.parse("2025-08"),
                "429"
        );

        NovaVendaRequest novaVendaRequest = new NovaVendaRequest(usuario,produtos,pagamento);

        String payload = mapper.writeValueAsString(novaVendaRequest);

        UsuarioResponse usuarioResponse = new UsuarioResponse(
                "Jordi Henrique Marques da Silva",
                "61231610085",
                "jordi.silva@zup.com.br",
                "Rua Maria Luíza N. Alencar,Dinamérica,Campina Grande,PB -78068555",
                LocalDate.parse("1997-01-15")
        );

        ProdutoResponse produtoResponse1 = new ProdutoResponse(
                1L,
                "Playstation 5",
                new BigDecimal("5500"),
                LocalDateTime.parse("2022-01-15T15:56:23")
        );

        ProdutoResponse produtoResponse2 = new ProdutoResponse(
                2L,
                "Playstation 4",
                new BigDecimal("3500"),
                LocalDateTime.parse("2022-10-02T22:16:23")
        );

        PagamentoResponse pagamentoResponse = new PagamentoResponse(
                "ab750cbe-da1b-40cd-82a2-e781349fc243",
                StatusPagamento.valueOf("APROVADO")
        );

        Mockito.when(gerenciamentoUsuariosClient.buscarUsuario(usuario))
                .thenReturn(Optional.empty());

        Mockito.when(catalogoProdutosClient.buscarProduto(Mockito.any()))
                .thenReturn(Optional.of(produtoResponse1), Optional.of(produtoResponse2));

        Mockito.when(servicoPagamentosClient.submeterPagamento(Mockito.any()))
                .thenReturn(pagamentoResponse);

        MockHttpServletRequestBuilder request = post("/vendas")
                .header("Accept-Language", "pt-br")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload);


        // Ação e Corretude
        Exception resolvedException = mockMvc.perform(request)
                .andExpect(
                        status().isNotFound()
                )
                .andReturn()
                .getResolvedException();


        // Asserts
        List<Venda> vendas = this.vendaRepository.findAll();
        List<ItemVenda> itens = this.itemVendaRepository.findAll();
        assertThat(vendas).hasSize(0);
        assertThat(itens).hasSize(0);
        assertNotNull(resolvedException);
        assertEquals(ResponseStatusException.class, resolvedException.getClass());
        assertEquals("O usuário de id = 100 não foi encontrado", ((ResponseStatusException) resolvedException).getReason());

    }

    @Test
    @DisplayName("Não deve cadastrar uma venda quando um produto não for encontrado")
    void test8() throws Exception {

        // Cenário
        Long usuario = 1L;
        ProdutoVendaRequest produto1 = new ProdutoVendaRequest(100L,2);
        ProdutoVendaRequest produto2 = new ProdutoVendaRequest(2L,3);
        List<ProdutoVendaRequest> produtos = List.of(produto1, produto2);
        DadosPagamentoRequest pagamento = new DadosPagamentoRequest(
                "Jose Humberto Coimbra Jr",
                "5478659832154582",
                YearMonth.parse("2025-08"),
                "429"
        );

        NovaVendaRequest novaVendaRequest = new NovaVendaRequest(usuario,produtos,pagamento);

        String payload = mapper.writeValueAsString(novaVendaRequest);

        UsuarioResponse usuarioResponse = new UsuarioResponse(
                "Jordi Henrique Marques da Silva",
                "61231610085",
                "jordi.silva@zup.com.br",
                "Rua Maria Luíza N. Alencar,Dinamérica,Campina Grande,PB -78068555",
                LocalDate.parse("1997-01-15")
        );

        ProdutoResponse produtoResponse1 = new ProdutoResponse(
                100L,
                "Playstation 5",
                new BigDecimal("5500"),
                LocalDateTime.parse("2022-01-15T15:56:23")
        );

        ProdutoResponse produtoResponse2 = new ProdutoResponse(
                2L,
                "Playstation 4",
                new BigDecimal("3500"),
                LocalDateTime.parse("2022-10-02T22:16:23")
        );

        PagamentoResponse pagamentoResponse = new PagamentoResponse(
                "ab750cbe-da1b-40cd-82a2-e781349fc243",
                StatusPagamento.valueOf("APROVADO")
        );

        Mockito.when(gerenciamentoUsuariosClient.buscarUsuario(usuario))
                .thenReturn(Optional.of(usuarioResponse));

        Mockito.when(catalogoProdutosClient.buscarProduto(Mockito.any()))
                .thenReturn(Optional.empty(), Optional.of(produtoResponse2));

        Mockito.when(servicoPagamentosClient.submeterPagamento(Mockito.any()))
                .thenReturn(pagamentoResponse);

        MockHttpServletRequestBuilder request = post("/vendas")
                .header("Accept-Language", "pt-br")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload);


        // Ação e Corretude
        Exception resolvedException = mockMvc.perform(request)
                .andExpect(
                        status().isNotFound()
                )
                .andReturn()
                .getResolvedException();


        // Asserts
        List<Venda> vendas = this.vendaRepository.findAll();
        List<ItemVenda> itens = this.itemVendaRepository.findAll();
        assertThat(vendas).hasSize(0);
        assertThat(itens).hasSize(0);
        assertNotNull(resolvedException);
        assertEquals(ResponseStatusException.class, resolvedException.getClass());
        assertEquals("O produto de id = 100 não foi encontrado", ((ResponseStatusException) resolvedException).getReason());

    }

    @Test
    @DisplayName("Não deve cadastrar uma venda quando o sistema de Gerenciamento de Usuários apresenta erro")
    void test9() throws Exception {

        // Cenário
        Long usuario = 1L;
        ProdutoVendaRequest produto1 = new ProdutoVendaRequest(1L,2);
        ProdutoVendaRequest produto2 = new ProdutoVendaRequest(2L,3);
        List<ProdutoVendaRequest> produtos = List.of(produto1, produto2);
        DadosPagamentoRequest pagamento = new DadosPagamentoRequest(
                "Jose Humberto Coimbra Jr",
                "5478659832154582",
                YearMonth.parse("2025-08"),
                "429"
        );

        NovaVendaRequest novaVendaRequest = new NovaVendaRequest(usuario,produtos,pagamento);

        String payload = mapper.writeValueAsString(novaVendaRequest);

        UsuarioResponse usuarioResponse = new UsuarioResponse(
                "Jordi Henrique Marques da Silva",
                "61231610085",
                "jordi.silva@zup.com.br",
                "Rua Maria Luíza N. Alencar,Dinamérica,Campina Grande,PB -78068555",
                LocalDate.parse("1997-01-15")
        );

        ProdutoResponse produtoResponse1 = new ProdutoResponse(
                1L,
                "Playstation 5",
                new BigDecimal("5500"),
                LocalDateTime.parse("2022-01-15T15:56:23")
        );

        ProdutoResponse produtoResponse2 = new ProdutoResponse(
                2L,
                "Playstation 4",
                new BigDecimal("3500"),
                LocalDateTime.parse("2022-10-02T22:16:23")
        );

        PagamentoResponse pagamentoResponse = new PagamentoResponse(
                "ab750cbe-da1b-40cd-82a2-e781349fc243",
                StatusPagamento.valueOf("APROVADO")
        );

        Mockito.when(gerenciamentoUsuariosClient.buscarUsuario(usuario))
                        .thenThrow(new RuntimeException());

        Mockito.when(catalogoProdutosClient.buscarProduto(Mockito.any()))
                .thenReturn(Optional.of(produtoResponse1), Optional.of(produtoResponse2));

        Mockito.when(servicoPagamentosClient.submeterPagamento(Mockito.any()))
                .thenReturn(pagamentoResponse);

        MockHttpServletRequestBuilder request = post("/vendas")
                .header("Accept-Language", "pt-br")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload);


        // Ação e Corretude
        Exception resolvedException = mockMvc.perform(request)
                .andExpect(
                        status().isInternalServerError()
                )
                .andReturn()
                .getResolvedException();


        // Asserts
        List<Venda> vendas = this.vendaRepository.findAll();
        List<ItemVenda> itens = this.itemVendaRepository.findAll();
        assertThat(vendas).hasSize(0);
        assertThat(itens).hasSize(0);
        assertNotNull(resolvedException);
        assertEquals(ResponseStatusException.class, resolvedException.getClass());
        assertEquals("Não foi possível realizar a venda. Erro ao buscar o usuário de id = 1", ((ResponseStatusException) resolvedException).getReason());

    }

    @Test
    @DisplayName("Não deve cadastrar uma venda quando o sistema de Catálogo de Produtos apresenta erro")
    void test10() throws Exception {

        // Cenário
        Long usuario = 1L;
        ProdutoVendaRequest produto1 = new ProdutoVendaRequest(1L,2);
        ProdutoVendaRequest produto2 = new ProdutoVendaRequest(2L,3);
        List<ProdutoVendaRequest> produtos = List.of(produto1, produto2);
        DadosPagamentoRequest pagamento = new DadosPagamentoRequest(
                "Jose Humberto Coimbra Jr",
                "5478659832154582",
                YearMonth.parse("2025-08"),
                "429"
        );

        NovaVendaRequest novaVendaRequest = new NovaVendaRequest(usuario,produtos,pagamento);

        String payload = mapper.writeValueAsString(novaVendaRequest);

        UsuarioResponse usuarioResponse = new UsuarioResponse(
                "Jordi Henrique Marques da Silva",
                "61231610085",
                "jordi.silva@zup.com.br",
                "Rua Maria Luíza N. Alencar,Dinamérica,Campina Grande,PB -78068555",
                LocalDate.parse("1997-01-15")
        );

        ProdutoResponse produtoResponse1 = new ProdutoResponse(
                1L,
                "Playstation 5",
                new BigDecimal("5500"),
                LocalDateTime.parse("2022-01-15T15:56:23")
        );

        ProdutoResponse produtoResponse2 = new ProdutoResponse(
                2L,
                "Playstation 4",
                new BigDecimal("3500"),
                LocalDateTime.parse("2022-10-02T22:16:23")
        );

        PagamentoResponse pagamentoResponse = new PagamentoResponse(
                "ab750cbe-da1b-40cd-82a2-e781349fc243",
                StatusPagamento.valueOf("APROVADO")
        );

        Mockito.when(gerenciamentoUsuariosClient.buscarUsuario(usuario))
                .thenReturn(Optional.of(usuarioResponse));

        Mockito.when(catalogoProdutosClient.buscarProduto(Mockito.any()))
                .thenThrow(new RuntimeException());

        Mockito.when(servicoPagamentosClient.submeterPagamento(Mockito.any()))
                .thenReturn(pagamentoResponse);

        MockHttpServletRequestBuilder request = post("/vendas")
                .header("Accept-Language", "pt-br")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload);


        // Ação e Corretude
        Exception resolvedException = mockMvc.perform(request)
                .andExpect(
                        status().isInternalServerError()
                )
                .andReturn()
                .getResolvedException();


        // Asserts
        List<Venda> vendas = this.vendaRepository.findAll();
        List<ItemVenda> itens = this.itemVendaRepository.findAll();
        assertThat(vendas).hasSize(0);
        assertThat(itens).hasSize(0);
        assertNotNull(resolvedException);
        assertEquals(ResponseStatusException.class, resolvedException.getClass());
        assertEquals("Não foi possível realizar a venda. Erro ao buscar o produto de id = 1", ((ResponseStatusException) resolvedException).getReason());

    }

    @Test
    @DisplayName("Não deve cadastrar uma venda quando o Serviço de Pagamentos apresenta erro")
    void test11() throws Exception {

        // Cenário
        Long usuario = 1L;
        ProdutoVendaRequest produto1 = new ProdutoVendaRequest(1L,2);
        ProdutoVendaRequest produto2 = new ProdutoVendaRequest(2L,3);
        List<ProdutoVendaRequest> produtos = List.of(produto1, produto2);
        DadosPagamentoRequest pagamento = new DadosPagamentoRequest(
                "Jose Humberto Coimbra Jr",
                "5478659832154582",
                YearMonth.parse("2025-08"),
                "429"
        );

        NovaVendaRequest novaVendaRequest = new NovaVendaRequest(usuario,produtos,pagamento);

        String payload = mapper.writeValueAsString(novaVendaRequest);

        UsuarioResponse usuarioResponse = new UsuarioResponse(
                "Jordi Henrique Marques da Silva",
                "61231610085",
                "jordi.silva@zup.com.br",
                "Rua Maria Luíza N. Alencar,Dinamérica,Campina Grande,PB -78068555",
                LocalDate.parse("1997-01-15")
        );

        ProdutoResponse produtoResponse1 = new ProdutoResponse(
                1L,
                "Playstation 5",
                new BigDecimal("5500"),
                LocalDateTime.parse("2022-01-15T15:56:23")
        );

        ProdutoResponse produtoResponse2 = new ProdutoResponse(
                2L,
                "Playstation 4",
                new BigDecimal("3500"),
                LocalDateTime.parse("2022-10-02T22:16:23")
        );

        PagamentoResponse pagamentoResponse = new PagamentoResponse(
                "ab750cbe-da1b-40cd-82a2-e781349fc243",
                StatusPagamento.valueOf("APROVADO")
        );

        Mockito.when(gerenciamentoUsuariosClient.buscarUsuario(usuario))
                .thenReturn(Optional.of(usuarioResponse));

        Mockito.when(catalogoProdutosClient.buscarProduto(Mockito.any()))
                .thenReturn(Optional.of(produtoResponse1), Optional.of(produtoResponse2));

        Mockito.when(servicoPagamentosClient.submeterPagamento(Mockito.any()))
                .thenThrow(new RuntimeException());

        MockHttpServletRequestBuilder request = post("/vendas")
                .header("Accept-Language", "pt-br")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload);


        // Ação e Corretude
        Exception resolvedException = mockMvc.perform(request)
                .andExpect(
                        status().isInternalServerError()
                )
                .andReturn()
                .getResolvedException();


        // Asserts
        List<Venda> vendas = this.vendaRepository.findAll();
        List<ItemVenda> itens = this.itemVendaRepository.findAll();
        assertThat(vendas).hasSize(0);
        assertThat(itens).hasSize(0);
        assertNotNull(resolvedException);
        assertEquals(ResponseStatusException.class, resolvedException.getClass());
        assertEquals("Não foi possível realizar a venda. Erro ao processar o pagamento", ((ResponseStatusException) resolvedException).getReason());

    }

}