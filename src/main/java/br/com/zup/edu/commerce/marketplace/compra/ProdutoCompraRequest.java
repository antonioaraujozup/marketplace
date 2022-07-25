package br.com.zup.edu.commerce.marketplace.compra;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class ProdutoCompraRequest {

    @NotNull
    private Long id;

    @NotNull
    @Positive
    private Integer quantidade;

    public ProdutoCompraRequest() {
    }

    public ProdutoCompraRequest(Long id, Integer quantidade) {
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
