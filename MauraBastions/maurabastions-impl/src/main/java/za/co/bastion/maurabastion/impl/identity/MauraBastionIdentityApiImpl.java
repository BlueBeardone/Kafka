package za.co.bastion.maurabastion.impl.identity;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import za.co.bastion.common.dto.ApiResponse;
import za.co.bastion.common.exception.BackendException;
import za.co.bastion.common.exception.InvalidRequestException;
import za.co.bastion.fischl.FischlAuthResponse;
import za.co.bastion.fischl.FischlIdentityException;
import za.co.bastion.fischl.FischlLoginRequest;
import za.co.bastion.fischl.FischlRegisterRequest;
import za.co.bastion.fischl.FischlUser;
import za.co.bastion.fischl.identity.FischlIdentityConnector;
import za.co.bastion.maurabastion.api.identity.MauraBastionIdentityApi;

import java.util.List;

@ApplicationScoped
public class MauraBastionIdentityApiImpl implements MauraBastionIdentityApi {

    @Inject
    @RestClient
    FischlIdentityConnector fischlIdentityConnector;

    @Override
    public ApiResponse registerUser(MauraBastionIdentityApi.RegisterRequest request) {
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
    public ApiResponse loginUser(MauraBastionIdentityApi.LoginRequest request) {
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new InvalidRequestException("email is required");
        }
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new InvalidRequestException("password is required");
        }

        try {
            FischlLoginRequest fischlRequest = new FischlLoginRequest(
                    request.getEmail(), request.getPassword());
            FischlAuthResponse authResponse = fischlIdentityConnector.login(fischlRequest);
            return ApiResponse.success("User logged in", toAuthResult(authResponse));
        } catch (FischlIdentityException e) {
            throw new BackendException(e.getStatus(), "Identity backend error: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse getMe(String authHeader) {
        if (authHeader == null || authHeader.isBlank()) {
            throw new InvalidRequestException("Authorization header is required");
        }

        try {
            FischlUser user = fischlIdentityConnector.getMe(authHeader);
            return ApiResponse.success("User found", toUser(user));
        } catch (FischlIdentityException e) {
            throw new BackendException(e.getStatus(), "Identity backend error: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse getUser(String id) {
        if (id == null || id.isBlank()) {
            throw new InvalidRequestException("id is required");
        }
        try {
            FischlUser result = fischlIdentityConnector.read(id);
            return ApiResponse.success("User found", toUser(result));
        } catch (FischlIdentityException e) {
            throw new BackendException(e.getStatus(), "Identity backend error: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse getAllUsers() {
        try {
            List<FischlUser> result = fischlIdentityConnector.readAll();
            List<MauraBastionIdentityApi.User> users = result.stream()
                    .map(this::toUser)
                    .toList();
            return ApiResponse.success("Users found", users);
        } catch (FischlIdentityException e) {
            throw new BackendException(e.getStatus(), "Identity backend error: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse updateUser(String id, MauraBastionIdentityApi.User user) {
        if (id == null || id.isBlank()) {
            throw new InvalidRequestException("id is required");
        }
        if (user.getDisplayName() == null || user.getDisplayName().isBlank()) {
            throw new InvalidRequestException("displayName is required");
        }

        try {
            FischlUser fischlUser = toFischlUser(user);
            FischlUser result = fischlIdentityConnector.update(id, fischlUser);
            return ApiResponse.success("User updated", toUser(result));
        } catch (FischlIdentityException e) {
            throw new BackendException(e.getStatus(), "Identity backend error: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse deleteUser(String id) {
        if (id == null || id.isBlank()) {
            throw new InvalidRequestException("id is required");
        }
        try {
            fischlIdentityConnector.delete(id);
            return ApiResponse.success("User deleted", null);
        } catch (FischlIdentityException e) {
            throw new BackendException(e.getStatus(), "Identity backend error: " + e.getMessage());
        }
    }

    private FischlUser toFischlUser(MauraBastionIdentityApi.User user) {
        return new FischlUser()
                .setId(user.getId())
                .setDisplayName(user.getDisplayName())
                .setBastionId(user.getBastionId());
    }

    private MauraBastionIdentityApi.User toUser(FischlUser fischlUser) {
        return new MauraBastionIdentityApi.User()
                .setId(fischlUser.getId())
                .setDisplayName(fischlUser.getDisplayName())
                .setBastionId(fischlUser.getBastionId());
    }

    private Object toAuthResult(FischlAuthResponse authResponse) {
        return new AuthResult()
                .setUser(toUser(authResponse.getUser()))
                .setToken(authResponse.getToken());
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    static class AuthResult {
        private MauraBastionIdentityApi.User user;
        private String token;
    }
}