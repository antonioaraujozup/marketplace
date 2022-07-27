package br.com.zup.edu.commerce.marketplace.compra;

import br.com.zup.edu.commerce.marketplace.clients.StatusPagamento;

public class StatusPagamentoResponse {

    private StatusPagamento pagamento;

    public StatusPagamentoResponse(StatusPagamento statusPagamento) {
        this.pagamento = statusPagamento;
    }

    public StatusPagamentoResponse() {
    }

    public StatusPagamento getPagamento() {
        return pagamento;
    }

}
