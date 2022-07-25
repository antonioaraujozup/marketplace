package br.com.zup.edu.commerce.marketplace.service;

import br.com.zup.edu.commerce.marketplace.compra.Venda;
import br.com.zup.edu.commerce.marketplace.compra.VendaDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducerService {

    @Autowired
    private KafkaTemplate<String, VendaDto> kafkaTemplate;

    @Value("${spring.kafka.topics.venda.name}")
    private String topicName;

    public void insereEventoNoTopico(VendaDto venda) {
        kafkaTemplate.send(topicName, venda);
    }

}
