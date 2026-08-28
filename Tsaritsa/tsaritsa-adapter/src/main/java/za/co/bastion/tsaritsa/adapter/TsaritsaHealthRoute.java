package za.co.bastion.tsaritsa.adapter;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import za.co.bastion.common.dto.ApiResponse;
import za.co.bastion.tsaritsa.api.TsaritsaHealthApi;

@Path("/tsaritsa/v1/health")
public class TsaritsaHealthRoute {
    @Inject
    private TsaritsaHealthApi healthApi;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse health() {
        return healthApi.health();
    }
}