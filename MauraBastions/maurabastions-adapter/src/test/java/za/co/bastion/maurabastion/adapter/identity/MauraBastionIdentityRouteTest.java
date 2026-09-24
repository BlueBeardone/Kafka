package za.co.bastion.maurabastion.adapter.identity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import za.co.bastion.common.dto.ApiResponse;
import za.co.bastion.maurabastion.api.identity.MauraBastionIdentityApi;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MauraBastionIdentityRouteTest {

    @Mock
    MauraBastionIdentityApi identityApi;

    MauraBastionIdentityRoute route;

    @BeforeEach
    void setUp() throws Exception {
        route = new MauraBastionIdentityRoute();
        Field field = MauraBastionIdentityRoute.class.getDeclaredField("identityApi");
        field.setAccessible(true);
        field.set(route, identityApi);
    }

    @Test
    void registerUser_delegatesToApi() {
        MauraBastionIdentityApi.RegisterRequest request = new MauraBastionIdentityApi.RegisterRequest()
                .setEmail("test@example.com").setUsername("testuser").setPassword("password123");
        ApiResponse expected = ApiResponse.success("ok", null);
        when(identityApi.registerUser(request)).thenReturn(expected);

        ApiResponse response = route.registerUser(request);

        assertSame(expected, response);
        verify(identityApi).registerUser(request);
    }

    @Test
    void loginUser_delegatesToApi() {
        MauraBastionIdentityApi.LoginRequest request = new MauraBastionIdentityApi.LoginRequest()
                .setUsername("testuser").setPassword("password123");
        ApiResponse expected = ApiResponse.success("ok", null);
        when(identityApi.loginUser(request)).thenReturn(expected);

        ApiResponse response = route.loginUser(request);

        assertSame(expected, response);
        verify(identityApi).loginUser(request);
    }

    @Test
    void getMe_forwardsAuthorizationHeader() {
        ApiResponse expected = ApiResponse.success("ok", null);
        when(identityApi.getMe("Bearer token")).thenReturn(expected);

        ApiResponse response = route.getMe("Bearer token");

        assertSame(expected, response);
        verify(identityApi).getMe("Bearer token");
    }

    @Test
    void getUser_forwardsAuthorizationHeader() {
        ApiResponse expected = ApiResponse.success("ok", null);
        when(identityApi.getUser("user-1", "Bearer token")).thenReturn(expected);

        ApiResponse response = route.getUser("user-1", "Bearer token");

        assertSame(expected, response);
        verify(identityApi).getUser("user-1", "Bearer token");
    }

    @Test
    void updateUser_forwardsAuthorizationHeader() {
        MauraBastionIdentityApi.User user = new MauraBastionIdentityApi.User()
                .setId("user-1").setDisplayName("Updated User");
        ApiResponse expected = ApiResponse.success("ok", null);
        when(identityApi.updateUser(eq("user-1"), any(), eq("Bearer token"))).thenReturn(expected);

        ApiResponse response = route.updateUser("user-1", "Bearer token", user);

        assertSame(expected, response);
        verify(identityApi).updateUser("user-1", user, "Bearer token");
    }

    @Test
    void listRoles_forwardsAuthorizationHeader() {
        ApiResponse expected = ApiResponse.success("ok", null);
        when(identityApi.listRoles("Bearer token")).thenReturn(expected);

        ApiResponse response = route.listRoles("Bearer token");

        assertSame(expected, response);
        verify(identityApi).listRoles("Bearer token");
    }

    @Test
    void getRole_forwardsAuthorizationHeader() {
        ApiResponse expected = ApiResponse.success("ok", null);
        when(identityApi.getRole("admin", "Bearer token")).thenReturn(expected);

        ApiResponse response = route.getRole("admin", "Bearer token");

        assertSame(expected, response);
        verify(identityApi).getRole("admin", "Bearer token");
    }

    @Test
    void listApps_forwardsAuthorizationHeader() {
        ApiResponse expected = ApiResponse.success("ok", null);
        when(identityApi.listApps("Bearer token")).thenReturn(expected);

        ApiResponse response = route.listApps("Bearer token");

        assertSame(expected, response);
        verify(identityApi).listApps("Bearer token");
    }

    @Test
    void getApp_forwardsAuthorizationHeader() {
        ApiResponse expected = ApiResponse.success("ok", null);
        when(identityApi.getApp("fischl", "Bearer token")).thenReturn(expected);

        ApiResponse response = route.getApp("fischl", "Bearer token");

        assertSame(expected, response);
        verify(identityApi).getApp("fischl", "Bearer token");
    }
}