package br.com.zup.edu.commerce.marketplace.service;

import br.com.zup.edu.commerce.marketplace.compra.VendaDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducerService {

    Logger logger = LoggerFactory.getLogger(KafkaProducerService.class);

    @Autowired
    private KafkaTemplate<String, VendaDto> kafkaTemplate;

    @Value("${spring.kafka.topics.venda.name}")
    private String topicName;

    public void insereEventoNoTopico(VendaDto venda) {
        kafkaTemplate.send(topicName, venda);

        logger.info("Evento da {} inserido no tópico com sucesso", venda.toString());
    }

}
