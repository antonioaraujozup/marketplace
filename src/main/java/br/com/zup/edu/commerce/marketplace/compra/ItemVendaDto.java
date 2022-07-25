package br.com.zup.edu.commerce.marketplace.compra;

import java.math.BigDecimal;

public class ItemVendaDto {

    private Long id;
    private String nome;
    private Integer quantidade;
    private BigDecimal valor;

    public ItemVendaDto() {
    }

    public ItemVendaDto(ItemVenda itemVenda) {
        this.id = itemVenda.getId();
        this.nome = itemVenda.getNome();
        this.quantidade = itemVenda.getQuantidade();
        this.valor = itemVenda.getValor();
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public BigDecimal getValor() {
        return valor;
    }

}
