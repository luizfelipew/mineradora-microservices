package org.br.mineradora.message;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.br.mineradora.dto.ProposalDTO;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@Slf4j
@ApplicationScoped
public class KafkaEvent {

    @Channel("proposal-channel")
    Emitter<ProposalDTO> proposalRequestEmitter;

    public void sendNewKafkaEvent(ProposalDTO proposalDTO) {
        log.info("-- Enviando Nova Proposta para o Tópico Kafka --");
        proposalRequestEmitter.send(proposalDTO)
                .toCompletableFuture()
                .join();
    }

}
