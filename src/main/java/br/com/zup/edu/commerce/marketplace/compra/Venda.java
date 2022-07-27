package br.com.zup.edu.commerce.marketplace.compra;

import br.com.zup.edu.commerce.marketplace.clients.StatusPagamento;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Entity
public class Venda {

    @Id
    @GeneratedValue
    private UUID codigoPedido;

    @Column(nullable = false)
    private Long idUsuario;

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

    public Venda(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public BigDecimal calculaValorTotal() {
        BigDecimal valorTotal = itens.stream()
                .map(i -> {
                    return i.getValor().multiply(new BigDecimal(i.getQuantidade()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return valorTotal;
    }

    public StatusPagamento retornaStatusPagamento() {
        return this.pagamento.getStatus();
    }

    public void adicionaItemVenda(ItemVenda item) {
        this.itens.add(item);
        item.setVenda(this);
    }

    @Override
    public String toString() {
        return "Venda {" +
                "codigoPedido=" + codigoPedido +
                ", pagamento=" + pagamento.getStatus() +
                '}';
    }

    public void adicionaDadosPagamento(Pagamento pagamento) {
        this.pagamento = pagamento;
    }

    public boolean pagamentoAprovado() {
        return this.pagamento.getStatus() == StatusPagamento.APROVADO;
    }

    public UUID getCodigoPedido() {
        return codigoPedido;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public Pagamento getPagamento() {
        return pagamento;
    }

    public LocalDateTime getCriadaEm() {
        return criadaEm;
    }

    public List<ItemVenda> getItens() {
        return Collections.unmodifiableList(itens);
    }

}
