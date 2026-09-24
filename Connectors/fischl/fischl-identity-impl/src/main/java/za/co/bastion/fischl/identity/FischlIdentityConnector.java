package za.co.bastion.fischl.identity;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import za.co.bastion.fischl.*;
import za.co.bastion.fischl.FischlRenewedTokenFilter;

import java.util.List;

@RegisterRestClient(configKey = "fischl-identity-api")
@RegisterProvider(FischlIdentityExceptionMapper.class)
@RegisterProvider(CustomHttpClientOptions.class)
@RegisterProvider(FischlEnvelopeBodyReader.class)
@RegisterProvider(FischlRenewedTokenFilter.class)
public interface FischlIdentityConnector {

    @POST
    @Path("/auth/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    FischlAuthResponse register(FischlRegisterRequest request) throws FischlIdentityException;

    @POST
    @Path("/auth/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    FischlAuthResponse login(FischlLoginRequest request) throws FischlIdentityException;

    @POST
    @Path("/auth/login/username")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    FischlAuthResponse loginByUsername(FischlLoginByUsernameRequest request) throws FischlIdentityException;

    @GET
    @Path("/auth/me")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    FischlUser getMe(@HeaderParam("Authorization") String authHeader) throws FischlIdentityException;

    @GET
    @Path("/users/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    FischlUser read(@PathParam("id") String id, @HeaderParam("Authorization") String authHeader) throws FischlIdentityException;

    @PATCH
    @Path("/users/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    FischlUser update(@PathParam("id") String id, @HeaderParam("Authorization") String authHeader, FischlUser user) throws FischlIdentityException;

    @POST
    @Path("/users/{id}/roles")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    FischlUser addRole(@PathParam("id") String id, @HeaderParam("Authorization") String authHeader, FischlAssignRoleRequest request) throws FischlIdentityException;

    @DELETE
    @Path("/users/{id}/roles/{name}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    FischlUser removeRole(@PathParam("id") String id, @PathParam("name") String name, @HeaderParam("Authorization") String authHeader) throws FischlIdentityException;

    @GET
    @Path("/roles")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    List<FischlRole> listRoles(@HeaderParam("Authorization") String authHeader) throws FischlIdentityException;

    @POST
    @Path("/roles")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    FischlRole createRole(@HeaderParam("Authorization") String authHeader, FischlRole role) throws FischlIdentityException;

    @GET
    @Path("/roles/{name}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    FischlRole getRole(@PathParam("name") String name, @HeaderParam("Authorization") String authHeader) throws FischlIdentityException;

    @PATCH
    @Path("/roles/{name}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    FischlRole updateRole(@PathParam("name") String name, @HeaderParam("Authorization") String authHeader, FischlRole role) throws FischlIdentityException;

    @DELETE
    @Path("/roles/{name}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    void deleteRole(@PathParam("name") String name, @HeaderParam("Authorization") String authHeader) throws FischlIdentityException;

    @GET
    @Path("/apps")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    List<FischlApp> listApps(@HeaderParam("Authorization") String authHeader) throws FischlIdentityException;

    @POST
    @Path("/apps")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    FischlApp createApp(@HeaderParam("Authorization") String authHeader, FischlApp app) throws FischlIdentityException;

    @GET
    @Path("/apps/{slug}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    FischlApp getApp(@PathParam("slug") String slug, @HeaderParam("Authorization") String authHeader) throws FischlIdentityException;

    @PATCH
    @Path("/apps/{slug}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    FischlApp updateApp(@PathParam("slug") String slug, @HeaderParam("Authorization") String authHeader, FischlApp app) throws FischlIdentityException;

    @DELETE
    @Path("/apps/{slug}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    void deleteApp(@PathParam("slug") String slug, @HeaderParam("Authorization") String authHeader) throws FischlIdentityException;
}