package br.com.zup.edu.commerce.marketplace.clients;

import java.math.BigDecimal;
import java.time.YearMonth;

public class PagamentoRequest {

    private String titular;
    private String numero;
    private YearMonth validoAte;
    private String codigoSeguranca;
    private BigDecimal valorCompra;

    public PagamentoRequest() {
    }

    public PagamentoRequest(String titular, String numero, YearMonth validoAte, String codigoSeguranca, BigDecimal valorCompra) {
        this.titular = titular;
        this.numero = numero;
        this.validoAte = validoAte;
        this.codigoSeguranca = codigoSeguranca;
        this.valorCompra = valorCompra;
    }

    public String getTitular() {
        return titular;
    }

    public String getNumero() {
        return numero;
    }

    public YearMonth getValidoAte() {
        return validoAte;
    }

    public String getCodigoSeguranca() {
        return codigoSeguranca;
    }

    public BigDecimal getValorCompra() {
        return valorCompra;
    }

}
