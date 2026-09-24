package za.co.bastion.tsaritsa.adapter.identity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import za.co.bastion.common.dto.ApiResponse;
import za.co.bastion.tsaritsa.api.identity.TsaritsaIdentityApi;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TsaritsaIdentityRouteTest {

    @Mock
    TsaritsaIdentityApi identityApi;

    TsaritsaIdentityRoute route;

    @BeforeEach
    void setUp() throws Exception {
        route = new TsaritsaIdentityRoute();
        Field field = TsaritsaIdentityRoute.class.getDeclaredField("identityApi");
        field.setAccessible(true);
        field.set(route, identityApi);
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
        TsaritsaIdentityApi.User user = new TsaritsaIdentityApi.User()
                .setId("user-1").setDisplayName("Updated User");
        ApiResponse expected = ApiResponse.success("ok", null);
        when(identityApi.updateUser(eq("user-1"), any(), eq("Bearer token"))).thenReturn(expected);

        ApiResponse response = route.updateUser("user-1", "Bearer token", user);

        assertSame(expected, response);
        verify(identityApi).updateUser("user-1", user, "Bearer token");
    }
}
