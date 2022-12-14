package br.com.zup.edu.commerce.marketplace.compra;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class ItemVenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long idProduto;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(nullable = false)
    private BigDecimal valor;

    @ManyToOne(optional = false)
    @JoinColumn(name = "codigo_pedido")
    private Venda venda;

    @Transient
    private String nome;

    /**
     * @deprecated Construtor para uso exclusivo do Hibernate.
     */
    @Deprecated
    public ItemVenda() {
    }

    public ItemVenda(Long idProduto, Integer quantidade, BigDecimal valor, String nome) {
        this.idProduto = idProduto;
        this.quantidade = quantidade;
        this.valor = valor;
        this.nome = nome;
    }

    public Long getId() {
        return id;
    }

    public Long getIdProduto() {
        return idProduto;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public String getNome() {
        return nome;
    }

    public void setVenda(Venda venda) {
        this.venda = venda;
    }

}
