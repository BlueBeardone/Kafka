package za.co.bastion.maurabastion.adapter.bastion;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import za.co.bastion.common.dto.ApiResponse;
import za.co.bastion.maurabastion.api.bastion.MauraBastionBastionApi;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MauraBastionBastionRouteTest {

    @Mock
    MauraBastionBastionApi bastionApi;

    MauraBastionBastionRoute route;

    @BeforeEach
    void setUp() throws Exception {
        route = new MauraBastionBastionRoute();
        Field field = MauraBastionBastionRoute.class.getDeclaredField("bastionApi");
        field.setAccessible(true);
        field.set(route, bastionApi);
    }

    @Test
    void browseBastions_delegatesToApiWithIdenticalArguments() {
        ApiResponse expected = ApiResponse.success("ok", null);

        when(bastionApi.browseBastions("Bearer token", 2, 20)).thenReturn(expected);

        ApiResponse response = route.browseBastions("Bearer token", 2, 20);

        assertSame(expected, response);
        verify(bastionApi).browseBastions(eq("Bearer token"), eq(2), eq(20));
        verify(bastionApi, times(1)).browseBastions(any(), any(), any());
    }

    @Test
    void browseBastions_withNullPageAndLimit_delegatesNulls() {
        ApiResponse expected = ApiResponse.success("ok", null);

        when(bastionApi.browseBastions("Bearer token", null, null)).thenReturn(expected);

        ApiResponse response = route.browseBastions("Bearer token", null, null);

        assertSame(expected, response);
        verify(bastionApi).browseBastions("Bearer token", null, null);
    }

    @Test
    void getAllBastions_forwardsAuthorizationHeaderAndAllFlag() {
        ApiResponse expected = ApiResponse.success("ok", null);

        when(bastionApi.getAllBastions("Bearer token", true)).thenReturn(expected);

        ApiResponse response = route.getAllBastions("Bearer token", true);

        assertSame(expected, response);
        verify(bastionApi).getAllBastions("Bearer token", true);
        verify(bastionApi, times(1)).getAllBastions(any(), any());
    }

    @Test
    void getAllBastions_withoutAllFlag_forwardsNull() {
        ApiResponse expected = ApiResponse.success("ok", null);

        when(bastionApi.getAllBastions("Bearer token", null)).thenReturn(expected);

        ApiResponse response = route.getAllBastions("Bearer token", null);

        assertSame(expected, response);
        verify(bastionApi).getAllBastions("Bearer token", null);
    }
}
