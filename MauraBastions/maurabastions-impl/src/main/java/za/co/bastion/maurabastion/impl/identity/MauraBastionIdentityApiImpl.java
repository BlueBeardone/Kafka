package za.co.bastion.maurabastion.impl.identity;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import za.co.bastion.common.dto.ApiResponse;
import za.co.bastion.common.exception.InvalidRequestException;
import za.co.bastion.fischl.FischlIdentityException;
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
    public ApiResponse createUser(MauraBastionIdentityApi.User user) {
        if (user.getId() == null || user.getId().isBlank()) {
            throw new InvalidRequestException("id is required");
        }
        if (user.getDisplayName() == null || user.getDisplayName().isBlank()) {
            throw new InvalidRequestException("displayName is required");
        }

        try {
            FischlUser fischlUser = toFischlUser(user);
            FischlUser result = fischlIdentityConnector.create(fischlUser);
            return ApiResponse.success("User created", toUser(result));
        } catch (FischlIdentityException e) {
            throw new RuntimeException("Identity backend error: " + e.getMessage(), e);
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
            throw new RuntimeException("Identity backend error: " + e.getMessage(), e);
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
            throw new RuntimeException("Identity backend error: " + e.getMessage(), e);
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
            throw new RuntimeException("Identity backend error: " + e.getMessage(), e);
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
            throw new RuntimeException("Identity backend error: " + e.getMessage(), e);
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
}
