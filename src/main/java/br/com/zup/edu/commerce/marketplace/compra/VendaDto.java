package br.com.zup.edu.commerce.marketplace.compra;

import br.com.zup.edu.commerce.marketplace.clients.UsuarioResponse;

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

    public VendaDto(Venda venda, UsuarioResponse usuarioResponse) {
        this.codigoPedido = venda.getCodigoPedido();
        this.comprador = new UsuarioDto(usuarioResponse);
        this.pagamento = new PagamentoDto(venda.getPagamento());
        this.itens = venda.getItens().stream()
                .map(ItemVendaDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "Venda {" +
                "codigoPedido=" + codigoPedido +
                '}';
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
