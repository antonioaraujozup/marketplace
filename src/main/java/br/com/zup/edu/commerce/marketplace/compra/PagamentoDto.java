package br.com.zup.edu.commerce.marketplace.compra;

import br.com.zup.edu.commerce.marketplace.clients.StatusPagamento;

public class PagamentoDto {

    private String id;
    private FormaPagamento forma;
    private StatusPagamento status;

    public PagamentoDto() {
    }

    public PagamentoDto(Pagamento pagamento) {
        this.id = pagamento.getId();
        this.forma = pagamento.getForma();
        this.status = pagamento.getStatus();
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
