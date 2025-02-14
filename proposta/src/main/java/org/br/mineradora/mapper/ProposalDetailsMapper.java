package org.br.mineradora.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import org.br.mineradora.dto.ProposalDetailsDTO;
import org.br.mineradora.entity.ProposalEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@ApplicationScoped
@Mapper
public interface ProposalDetailsMapper {

    @Mapping(target = "proposalId", source = "id")
    ProposalDetailsDTO parserProposalDetailsDTO(ProposalEntity proposalEntity);

}
