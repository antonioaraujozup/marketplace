package br.com.zup.edu.commerce.marketplace.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "gerenciamentoUsuariosClient",
        url = "${integrations.clients.gerenciamento-usuarios.url}"
)
public interface GerenciamentoUsuariosClient {

    @GetMapping("/usuarios/{id}")
    public UsuarioResponse buscarUsuario(@PathVariable Long id);

}
