package br.com.zup.edu.commerce.marketplace.compra;

import br.com.zup.edu.commerce.marketplace.clients.StatusPagamento;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
public class Pagamento {

    @Column(nullable = false, name = "id_pagamento")
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "forma_pagamento")
    private FormaPagamento forma;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "status_pagamento")
    private StatusPagamento status;

    /**
     * @deprecated Construtor para uso exclusivo do Hibernate.
     */
    @Deprecated
    public Pagamento() {
    }

    public Pagamento(String id, FormaPagamento forma, StatusPagamento status) {
        this.id = id;
        this.forma = forma;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public FormaPagamento getForma() {
        return forma;
    }

    public StatusPagamento getStatus() {
        return status;
    }

}
