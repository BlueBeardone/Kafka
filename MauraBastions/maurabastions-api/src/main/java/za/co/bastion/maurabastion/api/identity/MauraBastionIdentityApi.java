package za.co.bastion.maurabastion.api.identity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import za.co.bastion.common.dto.ApiResponse;

public interface MauraBastionIdentityApi {
    ApiResponse registerUser(RegisterRequest request);
    ApiResponse loginUser(LoginRequest request);
    ApiResponse getMe(String authHeader);
    ApiResponse getUser(String id);
    ApiResponse getAllUsers();
    ApiResponse updateUser(String id, User user);
    ApiResponse deleteUser(String id);

    @Getter
    @Setter
    @Accessors(chain = true)
    class User {
        private String id;
        private String displayName;
        private String bastionId;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    class RegisterRequest {
        private String email;
        private String username;
        private String password;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    class LoginRequest {
        private String email;
        private String password;
    }
}