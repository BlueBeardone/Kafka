package za.co.bastion.maurabastion.api.identity;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import za.co.bastion.common.dto.ApiResponse;

public interface MauraBastionIdentityApi {
    ApiResponse registerUser(RegisterRequest request);
    ApiResponse loginUser(LoginRequest request);
    ApiResponse getMe(String authHeader);
    ApiResponse getUser(String id);
    ApiResponse updateUser(String id, User user);

    ApiResponse listRoles(String authHeader);
    ApiResponse getRole(String name, String authHeader);

    ApiResponse listApps(String authHeader);
    ApiResponse getApp(String slug, String authHeader);

    @Getter
    @Setter
    @Accessors(chain = true)
    class User {
        private String id;
        private String displayName;
        private List<String> roles;
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
        private String username;
        private String password;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    class Role {
        private String name;
        private String description;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    class App {
        private String slug;
        private String name;
        private List<String> allowedRoles;
    }
}