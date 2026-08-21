package za.co.bastion.tsaritsa.adapter.identity;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import za.co.bastion.common.dto.ApiResponse;
import za.co.bastion.tsaritsa.api.identity.TsaritsaIdentityApi;

@Path("/tsaritsa/v1/identity")
public class TsaritsaIdentityRoute {

    @Inject
    private TsaritsaIdentityApi identityApi;

    @POST
    @Path("/auth/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse registerUser(TsaritsaIdentityApi.RegisterRequest request) {
        return identityApi.registerUser(request);
    }

    @POST
    @Path("/auth/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse loginUser(TsaritsaIdentityApi.LoginRequest request) {
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
    public ApiResponse getUser(@PathParam("id") String id) {
        return identityApi.getUser(id);
    }

    @PUT
    @Path("/users/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse updateUser(@PathParam("id") String id, TsaritsaIdentityApi.User user) {
        return identityApi.updateUser(id, user);
    }

    @POST
    @Path("/users/{id}/roles")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse addRole(@PathParam("id") String id, @HeaderParam("Authorization") String authHeader, TsaritsaIdentityApi.AssignRoleRequest request) {
        return identityApi.addRole(id, authHeader, request);
    }

    @DELETE
    @Path("/users/{id}/roles/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse removeRole(@PathParam("id") String id, @PathParam("name") String name, @HeaderParam("Authorization") String authHeader) {
        return identityApi.removeRole(id, name, authHeader);
    }

    @GET
    @Path("/roles")
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse listRoles(@HeaderParam("Authorization") String authHeader) {
        return identityApi.listRoles(authHeader);
    }

    @POST
    @Path("/roles")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse createRole(@HeaderParam("Authorization") String authHeader, TsaritsaIdentityApi.Role role) {
        return identityApi.createRole(authHeader, role);
    }

    @GET
    @Path("/roles/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse getRole(@PathParam("name") String name, @HeaderParam("Authorization") String authHeader) {
        return identityApi.getRole(name, authHeader);
    }

    @PUT
    @Path("/roles/{name}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse updateRole(@PathParam("name") String name, @HeaderParam("Authorization") String authHeader, TsaritsaIdentityApi.Role role) {
        return identityApi.updateRole(name, authHeader, role);
    }

    @DELETE
    @Path("/roles/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse deleteRole(@PathParam("name") String name, @HeaderParam("Authorization") String authHeader) {
        return identityApi.deleteRole(name, authHeader);
    }

    @GET
    @Path("/apps")
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse listApps(@HeaderParam("Authorization") String authHeader) {
        return identityApi.listApps(authHeader);
    }

    @POST
    @Path("/apps")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse createApp(@HeaderParam("Authorization") String authHeader, TsaritsaIdentityApi.App app) {
        return identityApi.createApp(authHeader, app);
    }

    @GET
    @Path("/apps/{slug}")
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse getApp(@PathParam("slug") String slug, @HeaderParam("Authorization") String authHeader) {
        return identityApi.getApp(slug, authHeader);
    }

    @PUT
    @Path("/apps/{slug}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse updateApp(@PathParam("slug") String slug, @HeaderParam("Authorization") String authHeader, TsaritsaIdentityApi.App app) {
        return identityApi.updateApp(slug, authHeader, app);
    }

    @DELETE
    @Path("/apps/{slug}")
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse deleteApp(@PathParam("slug") String slug, @HeaderParam("Authorization") String authHeader) {
        return identityApi.deleteApp(slug, authHeader);
    }
}