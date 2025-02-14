package org.br.mineradora.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.br.mineradora.dto.ProposalDTO;
import org.br.mineradora.dto.ProposalDetailsDTO;
import org.br.mineradora.entity.ProposalEntity;
import org.br.mineradora.message.KafkaEvent;
import org.br.mineradora.repository.ProposalRepository;

import java.util.Date;

@ApplicationScoped
@RequiredArgsConstructor
public class ProposalServiceImpl implements ProposalService {

    private final ProposalRepository proposalRepository;

    private final KafkaEvent kafkaMessages;

//    private final ProposalDetailsMapper proposalDetailsMapper;

    @Override
    public ProposalDetailsDTO findFullProposal(long id) {
        ProposalEntity proposal = proposalRepository.findById(id);

        return ProposalDetailsDTO.builder()
                .proposalId(proposal.getId())
                .customer(proposal.getCustomer())
                .priceTonne(proposal.getPriceTonne())
                .tonnes(proposal.getTonnes())
                .country(proposal.getCountry())
                .proposalValidityDays(proposal.getProposalValidityDays())
                .build();

//        return proposalDetailsMapper.parserProposalDetailsDTO(proposal);
    }

    @Override
    @Transactional
    public void createNewProposal(ProposalDetailsDTO proposalDetailsDTO) {
        ProposalDTO proposalDTO = buildAndSaveProposal(proposalDetailsDTO);
        kafkaMessages.sendNewKafkaEvent(proposalDTO);
    }

    @Override
    @Transactional
    public void removeProposal(long id) {
        proposalRepository.deleteById(id);
    }

//    @Transactional
    private ProposalDTO buildAndSaveProposal(ProposalDetailsDTO proposalDetailsDTO) {

        try {
            var proposal = new ProposalEntity();

            proposal.setCreated(new Date());
            proposal.setProposalValidityDays(proposalDetailsDTO.getProposalValidityDays());
            proposal.setCountry(proposalDetailsDTO.getCountry());
            proposal.setCustomer(proposalDetailsDTO.getCustomer());
            proposal.setPriceTonne(proposalDetailsDTO.getPriceTonne());
            proposal.setTonnes(proposalDetailsDTO.getTonnes());

            proposalRepository.persist(proposal);

            return ProposalDTO.builder()
                    .proposalId(proposalRepository.findByCustomer(proposalDetailsDTO.getCustomer()).get().getId())
                    .priceTonne(proposal.getPriceTonne())
                    .customer(proposal.getCustomer())
                    .build();

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException();
        }
    }
}
