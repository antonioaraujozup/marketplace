package br.com.zup.edu.commerce.marketplace.compra;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VendaRepository extends JpaRepository<Venda, UUID> {
}
