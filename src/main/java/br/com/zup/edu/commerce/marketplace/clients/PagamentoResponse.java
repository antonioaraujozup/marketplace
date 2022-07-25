package br.com.zup.edu.commerce.marketplace.clients;

import br.com.zup.edu.commerce.marketplace.compra.FormaPagamento;
import br.com.zup.edu.commerce.marketplace.compra.Pagamento;

public class PagamentoResponse {

    private String id;
    private StatusPagamento status;

    public PagamentoResponse() {
    }

    public PagamentoResponse(String id, StatusPagamento status) {
        this.id = id;
        this.status = status;
    }

    @Override
    public String toString() {
        return "PagamentoResponse{" +
                "id='" + id + '\'' +
                ", status=" + status +
                '}';
    }

    public Pagamento toPagamento() {
        return new Pagamento(id, FormaPagamento.CARTAO_DE_CREDITO,status);
    }

    public String getId() {
        return id;
    }

    public StatusPagamento getStatus() {
        return status;
    }

}
