package za.co.bastion.maurabastion.adapter.newspapers;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import za.co.bastion.common.dto.ApiResponse;
import za.co.bastion.maurabastion.api.newspapers.MauraBastionNewspapersApi;

@Path("/maura/v1/newspapers")
public class MauraBastionNewspapersRoute {

    @Inject
    private MauraBastionNewspapersApi newspapersApi;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse createNewspaper(MauraBastionNewspapersApi.Newspaper newspaper) {
        return newspapersApi.createNewspaper(newspaper);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse getNewspaper(@PathParam("id") String id) {
        return newspapersApi.getNewspaper(id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse getAllNewspapers() {
        return newspapersApi.getAllNewspapers();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse updateNewspaper(@PathParam("id") String id, MauraBastionNewspapersApi.Newspaper newspaper) {
        return newspapersApi.updateNewspaper(id, newspaper);
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse deleteNewspaper(@PathParam("id") String id) {
        return newspapersApi.deleteNewspaper(id);
    }
}
