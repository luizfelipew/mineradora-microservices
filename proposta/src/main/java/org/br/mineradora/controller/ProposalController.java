package org.br.mineradora.controller;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.br.mineradora.dto.ProposalDetailsDTO;
import org.br.mineradora.service.ProposalService;

@RequiredArgsConstructor
@Slf4j
@Path("/api/proposal")
public class ProposalController {

    private final ProposalService proposalService;

    @GET
    @Path("/{id}")
    public ProposalDetailsDTO findDetailsProposal(@PathParam("id") final long id) {
        return proposalService.findFullProposal(id);
    }

    @POST
//    @RolesAllowed("proposal-customer")
    public Response createProposal(final ProposalDetailsDTO proposalDetails) {
        log.info("--- Recebendo Proposta de Compra ---");

        try {
            proposalService.createNewProposal(proposalDetails);
            return Response
                    .ok()
                    .build();
        } catch (Exception ex) {
            return Response.serverError().build();
        }
    }

    @DELETE
    @Path("/{id}")
//    @RolesAllowed("manager")
    public Response removeProposal(@PathParam("id") final long id) {
        try {
            proposalService.removeProposal(id);
            return Response
                    .ok()
                    .build();
        } catch (Exception ex) {
            return Response.serverError().build();
        }
    }
}
