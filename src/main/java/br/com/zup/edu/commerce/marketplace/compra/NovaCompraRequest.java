package br.com.zup.edu.commerce.marketplace.compra;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public class NovaCompraRequest {

    @NotNull
    private Long usuario;

    @NotEmpty
    private List<@Valid ProdutoCompraRequest> produtos;

    @NotNull
    private @Valid DadosPagamentoRequest pagamento;

    public NovaCompraRequest() {
    }

    public NovaCompraRequest(Long usuario, List<ProdutoCompraRequest> produtos, DadosPagamentoRequest pagamento) {
        this.usuario = usuario;
        this.produtos = produtos;
        this.pagamento = pagamento;
    }

    public Long getUsuario() {
        return usuario;
    }

    public List<ProdutoCompraRequest> getProdutos() {
        return produtos;
    }

    public DadosPagamentoRequest getPagamento() {
        return pagamento;
    }

}
