package br.com.zup.edu.commerce.marketplace.compra;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.YearMonth;

public class DadosPagamentoRequest {

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

    public DadosPagamentoRequest() {
    }

    public DadosPagamentoRequest(String titular, String numero, YearMonth validoAte, String codigoSeguranca) {
        this.titular = titular;
        this.numero = numero;
        this.validoAte = validoAte;
        this.codigoSeguranca = codigoSeguranca;
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

}
