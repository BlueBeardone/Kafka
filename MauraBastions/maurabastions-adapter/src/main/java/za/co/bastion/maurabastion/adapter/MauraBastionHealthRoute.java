package za.co.bastion.maurabastion.adapter;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import za.co.bastion.common.dto.ApiResponse;
import za.co.bastion.maurabastion.api.MauraBastionHealthApi;

@Path("/maura/v1/health")
public class MauraBastionHealthRoute {
    @Inject
    private MauraBastionHealthApi healthApi;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse health() {
        return healthApi.health();
    }
}
