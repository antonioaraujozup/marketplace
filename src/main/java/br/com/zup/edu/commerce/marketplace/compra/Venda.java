package br.com.zup.edu.commerce.marketplace.compra;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class Venda {

    @Id
    @GeneratedValue
    private UUID codigoPedido;

    @Embedded
    private Usuario comprador;

    @Embedded
    private Pagamento pagamento;

    @CreationTimestamp
    private LocalDateTime criadaEm;

    @OneToMany(mappedBy = "venda", cascade = CascadeType.PERSIST)
    private List<ItemVenda> itens = new ArrayList<>();

    /**
     * @deprecated Construtor para uso exclusivo do Hibernate.
     */
    @Deprecated
    public Venda() {
    }

    public Venda(Usuario comprador) {
        this.comprador = comprador;
    }

    public BigDecimal calculaValorTotal() {
        BigDecimal valorTotal = itens.stream()
                .map(i -> {
                    return i.getValor().multiply(new BigDecimal(i.getQuantidade()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return valorTotal;
    }

    public UUID getCodigoPedido() {
        return codigoPedido;
    }

    public void adicionaItemVenda(ItemVenda item) {
        this.itens.add(item);
        item.setVenda(this);
    }

    public void adicionaDadosPagamento(Pagamento pagamento) {
        this.pagamento = pagamento;
    }

}
