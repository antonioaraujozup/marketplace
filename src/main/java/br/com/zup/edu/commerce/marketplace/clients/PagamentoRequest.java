package br.com.zup.edu.commerce.marketplace.clients;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.YearMonth;

public class PagamentoRequest {

    @NotBlank
    private String titular;

    @NotBlank
    @Pattern(regexp = "^\\d{16}$")
    private String numero;

    @NotNull
    @FutureOrPresent
    private YearMonth validoAte;

    @NotBlank
    @Pattern(regexp = "^\\d{3}$")
    private String codigoSeguranca;

    @NotNull
    @Positive
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
