package br.com.zup.edu.commerce.marketplace.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "servicoPagamentosClient",
        url = "${integrations.clients.servico-pagamentos.url}"
)
public interface ServicoPagamentosClient {

    @PostMapping("/pagamentos/credito")
    public PagamentoResponse submeterPagamento(@RequestBody PagamentoRequest request);

}
