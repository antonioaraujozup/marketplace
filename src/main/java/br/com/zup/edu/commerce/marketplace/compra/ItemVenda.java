package br.com.zup.edu.commerce.marketplace.compra;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class ItemVenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(nullable = false)
    private BigDecimal valor;

    @ManyToOne(optional = false)
    @JoinColumn(name = "codigo_pedido")
    private Venda venda;

    /**
     * @deprecated Construtor para uso exclusivo do Hibernate.
     */
    @Deprecated
    public ItemVenda() {
    }

    public ItemVenda(String nome, Integer quantidade, BigDecimal valor) {
        this.nome = nome;
        this.quantidade = quantidade;
        this.valor = valor;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setVenda(Venda venda) {
        this.venda = venda;
    }
}
