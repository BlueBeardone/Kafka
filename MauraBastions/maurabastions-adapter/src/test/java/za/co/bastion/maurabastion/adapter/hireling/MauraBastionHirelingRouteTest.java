package za.co.bastion.maurabastion.adapter.hireling;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import za.co.bastion.common.dto.ApiResponse;
import za.co.bastion.maurabastion.api.hireling.MauraBastionHirelingApi;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MauraBastionHirelingRouteTest {

    @Mock
    MauraBastionHirelingApi hirelingApi;

    MauraBastionHirelingRoute route;

    @BeforeEach
    void setUp() throws Exception {
        route = new MauraBastionHirelingRoute();
        Field field = MauraBastionHirelingRoute.class.getDeclaredField("hirelingApi");
        field.setAccessible(true);
        field.set(route, hirelingApi);
    }

    @Test
    void createHireling_forwardsAuthorizationHeader() {
        MauraBastionHirelingApi.Hireling hireling = new MauraBastionHirelingApi.Hireling().setName("Guard");
        ApiResponse expected = ApiResponse.success("ok", null);
        when(hirelingApi.createHireling("Bearer token", hireling)).thenReturn(expected);

        ApiResponse response = route.createHireling("Bearer token", hireling);

        assertSame(expected, response);
        verify(hirelingApi).createHireling("Bearer token", hireling);
    }

    @Test
    void getHireling_forwardsAuthorizationHeader() {
        ApiResponse expected = ApiResponse.success("ok", null);
        when(hirelingApi.getHireling("Bearer token", "h-1")).thenReturn(expected);

        ApiResponse response = route.getHireling("Bearer token", "h-1");

        assertSame(expected, response);
        verify(hirelingApi).getHireling("Bearer token", "h-1");
    }

    @Test
    void getAllHirelings_forwardsAuthorizationHeader() {
        ApiResponse expected = ApiResponse.success("ok", null);
        when(hirelingApi.getAllHirelings("Bearer token")).thenReturn(expected);

        ApiResponse response = route.getAllHirelings("Bearer token");

        assertSame(expected, response);
        verify(hirelingApi).getAllHirelings("Bearer token");
    }

    @Test
    void updateHireling_forwardsAuthorizationHeader() {
        MauraBastionHirelingApi.Hireling hireling = new MauraBastionHirelingApi.Hireling().setName("Guard");
        ApiResponse expected = ApiResponse.success("ok", null);
        when(hirelingApi.updateHireling(eq("Bearer token"), eq("h-1"), any())).thenReturn(expected);

        ApiResponse response = route.updateHireling("Bearer token", "h-1", hireling);

        assertSame(expected, response);
        verify(hirelingApi).updateHireling("Bearer token", "h-1", hireling);
    }

    @Test
    void deleteHireling_forwardsAuthorizationHeader() {
        ApiResponse expected = ApiResponse.success("ok", null);
        when(hirelingApi.deleteHireling("Bearer token", "h-1")).thenReturn(expected);

        ApiResponse response = route.deleteHireling("Bearer token", "h-1");

        assertSame(expected, response);
        verify(hirelingApi).deleteHireling("Bearer token", "h-1");
    }
}