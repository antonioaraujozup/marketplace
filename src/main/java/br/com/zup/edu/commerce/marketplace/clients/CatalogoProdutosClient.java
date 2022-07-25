package br.com.zup.edu.commerce.marketplace.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "catalogoProdutosClient",
        url = "${integrations.clients.catalogo-produtos.url}"
)
public interface CatalogoProdutosClient {

    @GetMapping("/produtos/{id}")
    public ProdutoResponse buscarProduto(@PathVariable Long id);

}
