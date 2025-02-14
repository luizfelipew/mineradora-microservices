package org.br.mineradora.message;

import io.smallrye.reactive.messaging.annotations.Blocking;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.br.mineradora.dto.ProposalDTO;
import org.br.mineradora.dto.QuotationDTO;
import org.br.mineradora.service.OpportunityService;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@Slf4j
@ApplicationScoped
public class KafkaEvent {

    private OpportunityService opportunityService;

    @Inject
    public KafkaEvent(OpportunityService opportunityService) {
        this.opportunityService = opportunityService;
    }

    @Incoming("proposal")
    @Transactional
    public void receiveProposal(ProposalDTO proposal) {
        log.info("-- Recebendo Nova Proposta do Tópico Kafka --");
        opportunityService.buildOpportunity(proposal);
    }

    @Incoming("quotation")
    @Blocking
    public void receiveQuotation(QuotationDTO quotation) {
        log.info("-- Recebendo Nova Cotação de Moeda do Tópico Kafka --");
        opportunityService.saveQuotation(quotation);
    }

}
