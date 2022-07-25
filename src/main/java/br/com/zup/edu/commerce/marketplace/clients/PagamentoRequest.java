package br.com.zup.edu.commerce.marketplace.clients;

import br.com.zup.edu.commerce.marketplace.compra.DadosPagamentoRequest;
import br.com.zup.edu.commerce.marketplace.compra.Venda;

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

    public PagamentoRequest(DadosPagamentoRequest pagamento, Venda venda) {
        this.titular = pagamento.getTitular();
        this.numero = pagamento.getNumero();
        this.validoAte = pagamento.getValidoAte();
        this.codigoSeguranca = pagamento.getCodigoSeguranca();
        this.valorCompra = venda.calculaValorTotal();
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
