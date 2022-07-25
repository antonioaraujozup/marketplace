package br.com.zup.edu.commerce.marketplace.clients;

public class PagamentoResponse {

    private String id;
    private StatusPagamento status;

    public PagamentoResponse() {
    }

    public PagamentoResponse(String id, StatusPagamento status) {
        this.id = id;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public StatusPagamento getStatus() {
        return status;
    }
    
}
