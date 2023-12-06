package org.br.mineradora.message;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.br.mineradora.dto.QuotationDTO;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@Slf4j
@ApplicationScoped
public class KafkaEvents {

    @Channel("quotation-channel")
    Emitter<QuotationDTO> quotationRequestEmitter;


    public void sendNewKafkaEvent(final QuotationDTO quotationDTO) {
        log.info("-- Enviando Cotação para Tópico Kafka --");
        quotationRequestEmitter.send(quotationDTO).toCompletableFuture().join();
    }
}
