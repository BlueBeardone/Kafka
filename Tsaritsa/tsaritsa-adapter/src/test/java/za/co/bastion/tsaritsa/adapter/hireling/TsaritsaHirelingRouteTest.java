package za.co.bastion.tsaritsa.adapter.hireling;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import za.co.bastion.common.dto.ApiResponse;
import za.co.bastion.tsaritsa.api.hireling.TsaritsaHirelingApi;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TsaritsaHirelingRouteTest {

    @Mock
    TsaritsaHirelingApi hirelingApi;

    TsaritsaHirelingRoute route;

    @BeforeEach
    void setUp() throws Exception {
        route = new TsaritsaHirelingRoute();
        Field field = TsaritsaHirelingRoute.class.getDeclaredField("hirelingApi");
        field.setAccessible(true);
        field.set(route, hirelingApi);
    }

    @Test
    void createHireling_forwardsAuthorizationHeader() {
        TsaritsaHirelingApi.Hireling hireling = new TsaritsaHirelingApi.Hireling().setName("Guard");
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
        TsaritsaHirelingApi.Hireling hireling = new TsaritsaHirelingApi.Hireling().setName("Guard");
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
