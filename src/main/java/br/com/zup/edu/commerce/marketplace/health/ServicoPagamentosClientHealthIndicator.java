package br.com.zup.edu.commerce.marketplace.health;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.Socket;
import java.net.URI;
import java.net.UnknownHostException;

@Component
public class ServicoPagamentosClientHealthIndicator implements HealthIndicator {

    private final URI uri;
    private final int port;

    public ServicoPagamentosClientHealthIndicator(
            @Value("${integrations.clients.servico-pagamentos.url}") String uri,
            @Value("${integrations.clients.servico-pagamentos.port}") int port
    ) {
        this.uri= URI.create(uri);
        this.port=port;
    }

    @Override
    public Health health() {

        try (Socket socket = new Socket(uri.getHost(), port)) {
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            return Health.down().withDetail("error", e.getMessage()).build();
        }
        return Health.up().build();
    }

}
