package za.co.bastion.tsaritsa.adapter.defender;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import za.co.bastion.common.dto.ApiResponse;
import za.co.bastion.tsaritsa.api.defender.TsaritsaDefenderApi;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TsaritsaDefenderRouteTest {

    @Mock
    TsaritsaDefenderApi defenderApi;

    TsaritsaDefenderRoute route;

    @BeforeEach
    void setUp() throws Exception {
        route = new TsaritsaDefenderRoute();
        Field field = TsaritsaDefenderRoute.class.getDeclaredField("defenderApi");
        field.setAccessible(true);
        field.set(route, defenderApi);
    }

    @Test
    void createDefender_forwardsAuthorizationHeader() {
        TsaritsaDefenderApi.Defender defender = new TsaritsaDefenderApi.Defender().setType("knight");
        ApiResponse expected = ApiResponse.success("ok", null);
        when(defenderApi.createDefender("Bearer token", defender)).thenReturn(expected);

        ApiResponse response = route.createDefender("Bearer token", defender);

        assertSame(expected, response);
        verify(defenderApi).createDefender("Bearer token", defender);
    }

    @Test
    void getDefender_forwardsAuthorizationHeader() {
        ApiResponse expected = ApiResponse.success("ok", null);
        when(defenderApi.getDefender("Bearer token", "d-1")).thenReturn(expected);

        ApiResponse response = route.getDefender("Bearer token", "d-1");

        assertSame(expected, response);
        verify(defenderApi).getDefender("Bearer token", "d-1");
    }

    @Test
    void getAllDefenders_forwardsAuthorizationHeader() {
        ApiResponse expected = ApiResponse.success("ok", null);
        when(defenderApi.getAllDefenders("Bearer token")).thenReturn(expected);

        ApiResponse response = route.getAllDefenders("Bearer token");

        assertSame(expected, response);
        verify(defenderApi).getAllDefenders("Bearer token");
    }

    @Test
    void updateDefender_forwardsAuthorizationHeader() {
        TsaritsaDefenderApi.Defender defender = new TsaritsaDefenderApi.Defender().setType("knight");
        ApiResponse expected = ApiResponse.success("ok", null);
        when(defenderApi.updateDefender(eq("Bearer token"), eq("d-1"), any())).thenReturn(expected);

        ApiResponse response = route.updateDefender("Bearer token", "d-1", defender);

        assertSame(expected, response);
        verify(defenderApi).updateDefender("Bearer token", "d-1", defender);
    }

    @Test
    void deleteDefender_forwardsAuthorizationHeader() {
        ApiResponse expected = ApiResponse.success("ok", null);
        when(defenderApi.deleteDefender("Bearer token", "d-1")).thenReturn(expected);

        ApiResponse response = route.deleteDefender("Bearer token", "d-1");

        assertSame(expected, response);
        verify(defenderApi).deleteDefender("Bearer token", "d-1");
    }
}
