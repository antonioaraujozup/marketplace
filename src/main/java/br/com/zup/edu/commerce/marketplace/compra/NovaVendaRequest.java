package br.com.zup.edu.commerce.marketplace.compra;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public class NovaVendaRequest {

    @NotNull
    private Long usuario;

    @NotEmpty
    private List<@Valid ProdutoVendaRequest> produtos;

    @NotNull
    private @Valid DadosPagamentoRequest pagamento;

    public NovaVendaRequest() {
    }

    public NovaVendaRequest(Long usuario, List<ProdutoVendaRequest> produtos, DadosPagamentoRequest pagamento) {
        this.usuario = usuario;
        this.produtos = produtos;
        this.pagamento = pagamento;
    }

    public Long getUsuario() {
        return usuario;
    }

    public List<ProdutoVendaRequest> getProdutos() {
        return produtos;
    }

    public DadosPagamentoRequest getPagamento() {
        return pagamento;
    }

}
