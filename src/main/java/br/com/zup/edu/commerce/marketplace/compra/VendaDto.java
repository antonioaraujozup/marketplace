package br.com.zup.edu.commerce.marketplace.compra;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class VendaDto {

    private UUID codigoPedido;
    private UsuarioDto comprador;
    private List<ItemVendaDto> itens = new ArrayList<>();
    private PagamentoDto pagamento;

    public VendaDto() {
    }

    public VendaDto(Venda venda) {
        this.codigoPedido = venda.getCodigoPedido();
        this.comprador = new UsuarioDto(venda.getComprador());
        this.pagamento = new PagamentoDto(venda.getPagamento());
        this.itens = venda.getItens().stream()
                .map(ItemVendaDto::new)
                .collect(Collectors.toList());
    }

    public UUID getCodigoPedido() {
        return codigoPedido;
    }

    public UsuarioDto getComprador() {
        return comprador;
    }

    public PagamentoDto getPagamento() {
        return pagamento;
    }

    public List<ItemVendaDto> getItens() {
        return itens;
    }

}
