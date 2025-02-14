package org.br.mineradora.controller;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.ServerErrorException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.br.mineradora.service.OpportunityService;

import java.util.Date;

@Path("/api/opportunity")
@RequiredArgsConstructor
public class OpportunityController {

    private final OpportunityService opportunityService;


    @GET
    @Path("/report")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response generateReport() {

        try{
            return Response.ok(opportunityService.generateCSVOpportunityReport(), MediaType.APPLICATION_OCTET_STREAM)
                    .header("content-disposition", "attachment; filename = " + new Date() + "--oportunidade-venda.csv")
                    .build();
        } catch (ServerErrorException errorException) {
            return Response.serverError().build();
        }
    }
}
