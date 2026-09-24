package za.co.bastion.tsaritsa.impl.identity;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import lombok.Getter;
import lombok.Setter;
import za.co.bastion.common.dto.ApiResponse;
import za.co.bastion.common.exception.BackendException;
import za.co.bastion.common.exception.InvalidRequestException;
import za.co.bastion.fischl.*;
import za.co.bastion.fischl.identity.FischlIdentityConnector;
import za.co.bastion.tsaritsa.api.identity.TsaritsaIdentityApi;

import java.util.List;

@ApplicationScoped
public class TsaritsaIdentityApiImpl implements TsaritsaIdentityApi {

    @Inject
    @RestClient
    FischlIdentityConnector fischlIdentityConnector;

    @Override
    public ApiResponse registerUser(RegisterRequest request) {
        if (request == null) {
            throw new InvalidRequestException("request is required");
        }
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new InvalidRequestException("email is required");
        }
        if (request.getUsername() == null || request.getUsername().isBlank()) {
            throw new InvalidRequestException("username is required");
        }
        if (request.getUsername().length() < 3) {
            throw new InvalidRequestException("username must be at least 3 characters");
        }
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new InvalidRequestException("password is required");
        }
        if (request.getPassword().length() < 8) {
            throw new InvalidRequestException("password must be at least 8 characters");
        }
        try {
            FischlRegisterRequest fischlRequest = new FischlRegisterRequest(
                    request.getEmail(), request.getUsername(), request.getPassword());
            FischlAuthResponse authResponse = fischlIdentityConnector.register(fischlRequest);
            return ApiResponse.success("User registered", toAuthResult(authResponse));
        } catch (FischlIdentityException e) {
            throw new BackendException(e.getStatus(), "Identity backend error: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse loginUser(LoginRequest request) {
        if (request == null) {
            throw new InvalidRequestException("request is required");
        }
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new InvalidRequestException("email is required");
        }
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new InvalidRequestException("password is required");
        }
        try {
            FischlLoginRequest fischlRequest = new FischlLoginRequest(
                    request.getEmail(), request.getPassword(), request.getApp());
            FischlAuthResponse authResponse = fischlIdentityConnector.login(fischlRequest);
            return ApiResponse.success("User logged in", toAuthResult(authResponse));
        } catch (FischlIdentityException e) {
            throw new BackendException(e.getStatus(), "Identity backend error: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse getMe(String authHeader) {
        requireAuth(authHeader);
        try {
            FischlUser user = fischlIdentityConnector.getMe(authHeader);
            return ApiResponse.success("User found", toUser(user));
        } catch (FischlIdentityException e) {
            throw new BackendException(e.getStatus(), "Identity backend error: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse getUser(String id, String authHeader) {
        if (id == null || id.isBlank()) {
            throw new InvalidRequestException("id is required");
        }
        try {
            FischlUser result = fischlIdentityConnector.read(id, authHeader);
            return ApiResponse.success("User found", toUser(result));
        } catch (FischlIdentityException e) {
            throw new BackendException(e.getStatus(), "Identity backend error: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse updateUser(String id, User user, String authHeader) {
        if (id == null || id.isBlank()) {
            throw new InvalidRequestException("id is required");
        }
        if (user == null) {
            throw new InvalidRequestException("user is required");
        }
        if (user.getDisplayName() == null || user.getDisplayName().isBlank()) {
            throw new InvalidRequestException("displayName is required");
        }
        try {
            FischlUser result = fischlIdentityConnector.update(id, authHeader, toFischlUser(user));
            return ApiResponse.success("User updated", toUser(result));
        } catch (FischlIdentityException e) {
            throw new BackendException(e.getStatus(), "Identity backend error: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse addRole(String id, String authHeader, AssignRoleRequest request) {
        if (id == null || id.isBlank()) {
            throw new InvalidRequestException("id is required");
        }
        requireAuth(authHeader);
        if (request == null || request.getRole() == null || request.getRole().isBlank()) {
            throw new InvalidRequestException("role is required");
        }
        try {
            FischlAssignRoleRequest fischlRequest = new FischlAssignRoleRequest(request.getRole());
            FischlUser result = fischlIdentityConnector.addRole(id, authHeader, fischlRequest);
            return ApiResponse.success("Role added", toUser(result));
        } catch (FischlIdentityException e) {
            throw new BackendException(e.getStatus(), "Identity backend error: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse removeRole(String id, String name, String authHeader) {
        if (id == null || id.isBlank()) {
            throw new InvalidRequestException("id is required");
        }
        if (name == null || name.isBlank()) {
            throw new InvalidRequestException("role name is required");
        }
        requireAuth(authHeader);
        try {
            FischlUser result = fischlIdentityConnector.removeRole(id, name, authHeader);
            return ApiResponse.success("Role removed", toUser(result));
        } catch (FischlIdentityException e) {
            throw new BackendException(e.getStatus(), "Identity backend error: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse listRoles(String authHeader) {
        requireAuth(authHeader);
        try {
            List<FischlRole> results = fischlIdentityConnector.listRoles(authHeader);
            List<Role> roles = results.stream().map(this::toRole).toList();
            return ApiResponse.success("Roles found", roles);
        } catch (FischlIdentityException e) {
            throw new BackendException(e.getStatus(), "Identity backend error: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse createRole(String authHeader, Role role) {
        requireAuth(authHeader);
        if (role == null || role.getName() == null || role.getName().isBlank()) {
            throw new InvalidRequestException("role name is required");
        }
        try {
            FischlRole result = fischlIdentityConnector.createRole(authHeader, toFischlRole(role));
            return ApiResponse.success("Role created", toRole(result));
        } catch (FischlIdentityException e) {
            throw new BackendException(e.getStatus(), "Identity backend error: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse getRole(String name, String authHeader) {
        if (name == null || name.isBlank()) {
            throw new InvalidRequestException("role name is required");
        }
        requireAuth(authHeader);
        try {
            FischlRole result = fischlIdentityConnector.getRole(name, authHeader);
            return ApiResponse.success("Role found", toRole(result));
        } catch (FischlIdentityException e) {
            throw new BackendException(e.getStatus(), "Identity backend error: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse updateRole(String name, String authHeader, Role role) {
        if (name == null || name.isBlank()) {
            throw new InvalidRequestException("role name is required");
        }
        if (role == null) {
            throw new InvalidRequestException("role is required");
        }
        requireAuth(authHeader);
        try {
            FischlRole result = fischlIdentityConnector.updateRole(name, authHeader, toFischlRole(role));
            return ApiResponse.success("Role updated", toRole(result));
        } catch (FischlIdentityException e) {
            throw new BackendException(e.getStatus(), "Identity backend error: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse deleteRole(String name, String authHeader) {
        if (name == null || name.isBlank()) {
            throw new InvalidRequestException("role name is required");
        }
        requireAuth(authHeader);
        try {
            fischlIdentityConnector.deleteRole(name, authHeader);
            return ApiResponse.success("Role deleted", null);
        } catch (FischlIdentityException e) {
            throw new BackendException(e.getStatus(), "Identity backend error: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse listApps(String authHeader) {
        requireAuth(authHeader);
        try {
            List<FischlApp> results = fischlIdentityConnector.listApps(authHeader);
            List<App> apps = results.stream().map(this::toApp).toList();
            return ApiResponse.success("Apps found", apps);
        } catch (FischlIdentityException e) {
            throw new BackendException(e.getStatus(), "Identity backend error: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse createApp(String authHeader, App app) {
        requireAuth(authHeader);
        if (app == null || app.getSlug() == null || app.getSlug().isBlank()) {
            throw new InvalidRequestException("app slug is required");
        }
        try {
            FischlApp result = fischlIdentityConnector.createApp(authHeader, toFischlApp(app));
            return ApiResponse.success("App created", toApp(result));
        } catch (FischlIdentityException e) {
            throw new BackendException(e.getStatus(), "Identity backend error: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse getApp(String slug, String authHeader) {
        if (slug == null || slug.isBlank()) {
            throw new InvalidRequestException("app slug is required");
        }
        requireAuth(authHeader);
        try {
            FischlApp result = fischlIdentityConnector.getApp(slug, authHeader);
            return ApiResponse.success("App found", toApp(result));
        } catch (FischlIdentityException e) {
            throw new BackendException(e.getStatus(), "Identity backend error: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse updateApp(String slug, String authHeader, App app) {
        if (slug == null || slug.isBlank()) {
            throw new InvalidRequestException("app slug is required");
        }
        if (app == null) {
            throw new InvalidRequestException("app is required");
        }
        requireAuth(authHeader);
        try {
            FischlApp result = fischlIdentityConnector.updateApp(slug, authHeader, toFischlApp(app));
            return ApiResponse.success("App updated", toApp(result));
        } catch (FischlIdentityException e) {
            throw new BackendException(e.getStatus(), "Identity backend error: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse deleteApp(String slug, String authHeader) {
        if (slug == null || slug.isBlank()) {
            throw new InvalidRequestException("app slug is required");
        }
        requireAuth(authHeader);
        try {
            fischlIdentityConnector.deleteApp(slug, authHeader);
            return ApiResponse.success("App deleted", null);
        } catch (FischlIdentityException e) {
            throw new BackendException(e.getStatus(), "Identity backend error: " + e.getMessage());
        }
    }

    private void requireAuth(String authHeader) {
        if (authHeader == null || authHeader.isBlank()) {
            throw new InvalidRequestException("Authorization header is required");
        }
    }

    private FischlUser toFischlUser(User user) {
        return new FischlUser()
                .setId(user.getId())
                .setDisplayName(user.getDisplayName())
                .setRoles(user.getRoles());
    }

    private User toUser(FischlUser fischlUser) {
        return new User()
                .setId(fischlUser.getId())
                .setDisplayName(fischlUser.getDisplayName())
                .setRoles(fischlUser.getRoles());
    }

    private FischlRole toFischlRole(Role role) {
        return new FischlRole()
                .setName(role.getName())
                .setDescription(role.getDescription());
    }

    private Role toRole(FischlRole fischlRole) {
        return new Role()
                .setName(fischlRole.getName())
                .setDescription(fischlRole.getDescription());
    }

    private FischlApp toFischlApp(App app) {
        return new FischlApp()
                .setSlug(app.getSlug())
                .setName(app.getName())
                .setAllowedRoles(app.getAllowedRoles());
    }

    private App toApp(FischlApp fischlApp) {
        return new App()
                .setSlug(fischlApp.getSlug())
                .setName(fischlApp.getName())
                .setAllowedRoles(fischlApp.getAllowedRoles());
    }

    private Object toAuthResult(FischlAuthResponse authResponse) {
        return new AuthResult()
                .setUser(toUser(authResponse.getUser()))
                .setToken(authResponse.getToken());
    }

    @Getter
    @Setter
    @lombok.experimental.Accessors(chain = true)
    static class AuthResult {
        private User user;
        private String token;
    }
}