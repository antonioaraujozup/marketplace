package br.com.zup.edu.commerce.marketplace.compra;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class ProdutoVendaRequest {

    @NotNull
    private Long id;

    @NotNull
    @Positive
    private Integer quantidade;

    public ProdutoVendaRequest() {
    }

    public ProdutoVendaRequest(Long id, Integer quantidade) {
        this.id = id;
        this.quantidade = quantidade;
    }

    public Long getId() {
        return id;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

}
