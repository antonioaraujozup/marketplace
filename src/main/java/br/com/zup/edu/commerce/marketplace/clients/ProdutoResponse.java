package br.com.zup.edu.commerce.marketplace.clients;

import br.com.zup.edu.commerce.marketplace.compra.ItemVenda;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProdutoResponse {

    private Long id;
    private String nome;
    private BigDecimal preco;
    private LocalDateTime criadoEm;

    public ProdutoResponse() {
    }

    public ProdutoResponse(Long id, String nome, BigDecimal preco, LocalDateTime criadoEm) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.criadoEm = criadoEm;
    }

    @Override
    public String toString() {
        return "ProdutoResponse{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", preco=" + preco +
                ", criadoEm=" + criadoEm +
                '}';
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public ItemVenda toItemVenda(Integer quantidade) {
        return new ItemVenda(id, quantidade, preco, nome);
    }

}
