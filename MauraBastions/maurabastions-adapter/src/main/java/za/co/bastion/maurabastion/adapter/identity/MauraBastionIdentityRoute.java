package za.co.bastion.maurabastion.adapter.identity;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import za.co.bastion.common.dto.ApiResponse;
import za.co.bastion.maurabastion.api.identity.MauraBastionIdentityApi;

@Path("/maura/v1/identity")
public class MauraBastionIdentityRoute {

    @Inject
    private MauraBastionIdentityApi identityApi;

    @POST
    @Path("/auth/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse registerUser(MauraBastionIdentityApi.RegisterRequest request) {
        return identityApi.registerUser(request);
    }

    @POST
    @Path("/auth/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse loginUser(MauraBastionIdentityApi.LoginRequest request) {
        return identityApi.loginUser(request);
    }

    @GET
    @Path("/auth/me")
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse getMe(@HeaderParam("Authorization") String authHeader) {
        return identityApi.getMe(authHeader);
    }

    @GET
    @Path("/users/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse getUser(@PathParam("id") String id, @HeaderParam("Authorization") String authHeader) {
        return identityApi.getUser(id, authHeader);
    }

    @PUT
    @Path("/users/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse updateUser(@PathParam("id") String id, @HeaderParam("Authorization") String authHeader,
                                  MauraBastionIdentityApi.User user) {
        return identityApi.updateUser(id, user, authHeader);
    }

    @GET
    @Path("/roles")
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse listRoles(@HeaderParam("Authorization") String authHeader) {
        return identityApi.listRoles(authHeader);
    }

    @GET
    @Path("/roles/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse getRole(@PathParam("name") String name, @HeaderParam("Authorization") String authHeader) {
        return identityApi.getRole(name, authHeader);
    }

    @GET
    @Path("/apps")
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse listApps(@HeaderParam("Authorization") String authHeader) {
        return identityApi.listApps(authHeader);
    }

    @GET
    @Path("/apps/{slug}")
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse getApp(@PathParam("slug") String slug, @HeaderParam("Authorization") String authHeader) {
        return identityApi.getApp(slug, authHeader);
    }
}