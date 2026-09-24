package za.co.bastion.tsaritsa.adapter.facilities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import za.co.bastion.common.dto.ApiResponse;
import za.co.bastion.tsaritsa.api.facilities.TsaritsaFacilitiesApi;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TsaritsaFacilitiesRouteTest {

    @Mock
    TsaritsaFacilitiesApi facilitiesApi;

    TsaritsaFacilitiesRoute route;

    @BeforeEach
    void setUp() throws Exception {
        route = new TsaritsaFacilitiesRoute();
        Field field = TsaritsaFacilitiesRoute.class.getDeclaredField("facilitiesApi");
        field.setAccessible(true);
        field.set(route, facilitiesApi);
    }

    @Test
    void createFacility_forwardsAuthorizationHeader() {
        TsaritsaFacilitiesApi.Facility facility = new TsaritsaFacilitiesApi.Facility().setName("Barracks");
        ApiResponse expected = ApiResponse.success("ok", null);
        when(facilitiesApi.createFacility("Bearer token", facility)).thenReturn(expected);

        ApiResponse response = route.createFacility("Bearer token", facility);

        assertSame(expected, response);
        verify(facilitiesApi).createFacility("Bearer token", facility);
    }

    @Test
    void getFacility_forwardsAuthorizationHeader() {
        ApiResponse expected = ApiResponse.success("ok", null);
        when(facilitiesApi.getFacility("Bearer token", "f-1")).thenReturn(expected);

        ApiResponse response = route.getFacility("Bearer token", "f-1");

        assertSame(expected, response);
        verify(facilitiesApi).getFacility("Bearer token", "f-1");
    }

    @Test
    void getAllFacilities_forwardsAuthorizationHeader() {
        ApiResponse expected = ApiResponse.success("ok", null);
        when(facilitiesApi.getAllFacilities("Bearer token")).thenReturn(expected);

        ApiResponse response = route.getAllFacilities("Bearer token");

        assertSame(expected, response);
        verify(facilitiesApi).getAllFacilities("Bearer token");
    }

    @Test
    void updateFacility_forwardsAuthorizationHeader() {
        TsaritsaFacilitiesApi.Facility facility = new TsaritsaFacilitiesApi.Facility().setName("Barracks");
        ApiResponse expected = ApiResponse.success("ok", null);
        when(facilitiesApi.updateFacility(eq("Bearer token"), eq("f-1"), any())).thenReturn(expected);

        ApiResponse response = route.updateFacility("Bearer token", "f-1", facility);

        assertSame(expected, response);
        verify(facilitiesApi).updateFacility("Bearer token", "f-1", facility);
    }

    @Test
    void deleteFacility_forwardsAuthorizationHeader() {
        ApiResponse expected = ApiResponse.success("ok", null);
        when(facilitiesApi.deleteFacility("Bearer token", "f-1")).thenReturn(expected);

        ApiResponse response = route.deleteFacility("Bearer token", "f-1");

        assertSame(expected, response);
        verify(facilitiesApi).deleteFacility("Bearer token", "f-1");
    }
}
