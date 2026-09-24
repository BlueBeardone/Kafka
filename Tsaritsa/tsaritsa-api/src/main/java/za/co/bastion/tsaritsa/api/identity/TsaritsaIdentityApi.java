package za.co.bastion.tsaritsa.api.identity;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import za.co.bastion.common.dto.ApiResponse;

public interface TsaritsaIdentityApi {
    ApiResponse registerUser(RegisterRequest request);
    ApiResponse loginUser(LoginRequest request);
    ApiResponse getMe(String authHeader);
    ApiResponse getUser(String id, String authHeader);
    ApiResponse updateUser(String id, User user, String authHeader);
    ApiResponse addRole(String id, String authHeader, AssignRoleRequest request);
    ApiResponse removeRole(String id, String name, String authHeader);

    ApiResponse listRoles(String authHeader);
    ApiResponse createRole(String authHeader, Role role);
    ApiResponse getRole(String name, String authHeader);
    ApiResponse updateRole(String name, String authHeader, Role role);
    ApiResponse deleteRole(String name, String authHeader);

    ApiResponse listApps(String authHeader);
    ApiResponse createApp(String authHeader, App app);
    ApiResponse getApp(String slug, String authHeader);
    ApiResponse updateApp(String slug, String authHeader, App app);
    ApiResponse deleteApp(String slug, String authHeader);

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
        private String email;
        private String password;
        private String app;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    class AssignRoleRequest {
        private String role;
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