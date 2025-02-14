package org.br.mineradora.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.br.mineradora.dto.OpportunityDTO;
import org.br.mineradora.dto.ProposalDTO;
import org.br.mineradora.dto.QuotationDTO;
import org.br.mineradora.entity.OpportunityEntity;
import org.br.mineradora.entity.QuotationEntity;
import org.br.mineradora.repository.OpportunityRepository;
import org.br.mineradora.repository.QuotationRepository;
import org.br.mineradora.utils.CSVHelper;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@ApplicationScoped
public class OpportunityServiceImpl implements OpportunityService {

    private final QuotationRepository quotationRepository;
    private final OpportunityRepository opportunityRepository;

    @Override
    public void buildOpportunity(ProposalDTO proposal) {
        List<QuotationEntity> quotationEntities = quotationRepository.findAll().list();
        Collections.reverse(quotationEntities);

        OpportunityEntity opportunityEntity = new OpportunityEntity();
        opportunityEntity.setDate(new Date());
        opportunityEntity.setProposalId(proposal.getProposalId());
        opportunityEntity.setCustomer(proposal.getCustomer());
        opportunityEntity.setPriceTonne(proposal.getPriceTonne());
        opportunityEntity.setLastDollarQuotation(quotationEntities.get(0).getCurrencyPrice());

        opportunityRepository.persist(opportunityEntity);
    }

    @Override
    @Transactional
    public void saveQuotation(QuotationDTO quotation) {
        QuotationEntity createQuotation = new QuotationEntity();
        createQuotation.setDate(new Date());
        createQuotation.setCurrencyPrice(quotation.getCurrencyPrice());

        quotationRepository.persist(createQuotation);
    }

    @Override
    public List<OpportunityDTO> generateOpportunityData() {
        return List.of();
    }

    @Override
    public ByteArrayInputStream generateCSVOpportunityReport() {
        List<OpportunityDTO> opportunityList = new ArrayList<>();
        opportunityRepository.findAll().list().forEach(item -> {
            opportunityList.add(OpportunityDTO.builder()
                            .proposalId(item.getProposalId())
                            .customer(item.getCustomer())
                            .priceTonne(item.getPriceTonne())
                            .lastDollarQuotation(item.getLastDollarQuotation())
                    .build());
        });

        return CSVHelper.OpportunitiesToCSV(opportunityList);
    }
}
