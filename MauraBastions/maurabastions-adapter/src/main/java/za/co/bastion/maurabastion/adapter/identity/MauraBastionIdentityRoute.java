package za.co.bastion.maurabastion.adapter.identity;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import za.co.bastion.common.dto.ApiResponse;
import za.co.bastion.maurabastion.api.identity.MauraBastionIdentityApi;

@Path("/maura/v1/identity/users")
public class MauraBastionIdentityRoute {

    @Inject
    private MauraBastionIdentityApi identityApi;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse createUser(MauraBastionIdentityApi.User user) {
        return identityApi.createUser(user);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse getUser(@PathParam("id") String id) {
        return identityApi.getUser(id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse getAllUsers() {
        return identityApi.getAllUsers();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse updateUser(@PathParam("id") String id, MauraBastionIdentityApi.User user) {
        return identityApi.updateUser(id, user);
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse deleteUser(@PathParam("id") String id) {
        return identityApi.deleteUser(id);
    }
}
