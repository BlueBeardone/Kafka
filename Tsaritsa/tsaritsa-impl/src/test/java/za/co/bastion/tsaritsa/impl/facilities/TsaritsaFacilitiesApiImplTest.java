package za.co.bastion.tsaritsa.impl.facilities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import za.co.bastion.common.dto.ApiResponse;
import za.co.bastion.common.dto.Rank;
import za.co.bastion.common.exception.InvalidRequestException;
import za.co.bastion.fischl.FischlFacility;
import za.co.bastion.fischl.facilities.FischlFacilitiesConnector;
import za.co.bastion.tsaritsa.api.facilities.TsaritsaFacilitiesApi;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TsaritsaFacilitiesApiImplTest {

    @Mock
    FischlFacilitiesConnector fischlFacilitiesConnector;

    TsaritsaFacilitiesApi facilitiesApi;

    @BeforeEach
    void setUp() throws Exception {
        facilitiesApi = new TsaritsaFacilitiesApiImpl();
        Field field = TsaritsaFacilitiesApiImpl.class.getDeclaredField("fischlFacilitiesConnector");
        field.setAccessible(true);
        field.set(facilitiesApi, fischlFacilitiesConnector);
    }

    @Test
    void createFacility_returnsSuccess() throws Exception {
        TsaritsaFacilitiesApi.Facility facility = new TsaritsaFacilitiesApi.Facility()
                .setName("Barracks").setDescription("Trains defenders").setRank(Rank.b);

        when(fischlFacilitiesConnector.create(anyString(), any(FischlFacility.class)))
                .thenReturn(new FischlFacility().setId("f-1"));

        ApiResponse response = facilitiesApi.createFacility("Bearer token", facility);
        assertTrue(response.isSuccess());
    }

    @Test
    void createFacility_forwardsAuthHeaderToConnector() throws Exception {
        TsaritsaFacilitiesApi.Facility facility = new TsaritsaFacilitiesApi.Facility()
                .setName("Barracks").setDescription("Trains defenders").setRank(Rank.b);

        when(fischlFacilitiesConnector.create(eq("Bearer token"), any(FischlFacility.class)))
                .thenReturn(new FischlFacility().setId("f-1"));

        facilitiesApi.createFacility("Bearer token", facility);

        verify(fischlFacilitiesConnector).create(eq("Bearer token"), any(FischlFacility.class));
    }

    @Test
    void createFacility_nullRank_throwsException() {
        assertThrows(InvalidRequestException.class, () ->
                facilitiesApi.createFacility("Bearer token", new TsaritsaFacilitiesApi.Facility()
                        .setName("Barracks").setDescription("d")));
    }

    @Test
    void createFacility_negativeHirelings_throwsException() {
        assertThrows(InvalidRequestException.class, () ->
                facilitiesApi.createFacility("Bearer token", new TsaritsaFacilitiesApi.Facility()
                        .setName("Barracks").setDescription("d").setRank(Rank.b)
                        .setMinimumRequiredHirelings(-1)));
    }

    @Test
    void getFacility_returnsSuccess() throws Exception {
        when(fischlFacilitiesConnector.read(anyString(), eq("f-1")))
                .thenReturn(new FischlFacility().setId("f-1").setName("Barracks"));

        ApiResponse response = facilitiesApi.getFacility("Bearer token", "f-1");
        assertTrue(response.isSuccess());
    }

    @Test
    void getFacility_forwardsAuthHeaderToConnector() throws Exception {
        when(fischlFacilitiesConnector.read(eq("Bearer token"), eq("f-1")))
                .thenReturn(new FischlFacility().setId("f-1"));

        facilitiesApi.getFacility("Bearer token", "f-1");

        verify(fischlFacilitiesConnector).read("Bearer token", "f-1");
    }

    @Test
    void getAllFacilities_returnsSuccess() throws Exception {
        when(fischlFacilitiesConnector.readAll(anyString()))
                .thenReturn(List.of(new FischlFacility().setId("f-1").setName("Barracks")));

        ApiResponse response = facilitiesApi.getAllFacilities("Bearer token");
        assertTrue(response.isSuccess());

        verify(fischlFacilitiesConnector).readAll("Bearer token");
    }

    @Test
    void updateFacility_returnsSuccess() throws Exception {
        when(fischlFacilitiesConnector.update(anyString(), eq("f-1"), any(FischlFacility.class)))
                .thenReturn(new FischlFacility().setId("f-1"));

        ApiResponse response = facilitiesApi.updateFacility("Bearer token", "f-1",
                new TsaritsaFacilitiesApi.Facility().setName("Barracks").setDescription("d").setRank(Rank.b));
        assertTrue(response.isSuccess());
    }

    @Test
    void updateFacility_forwardsAuthHeaderToConnector() throws Exception {
        when(fischlFacilitiesConnector.update(eq("Bearer token"), eq("f-1"), any(FischlFacility.class)))
                .thenReturn(new FischlFacility().setId("f-1"));

        facilitiesApi.updateFacility("Bearer token", "f-1",
                new TsaritsaFacilitiesApi.Facility().setName("Barracks").setDescription("d").setRank(Rank.b));

        verify(fischlFacilitiesConnector).update(eq("Bearer token"), eq("f-1"), any(FischlFacility.class));
    }

    @Test
    void deleteFacility_returnsSuccess() throws Exception {
        doNothing().when(fischlFacilitiesConnector).delete(anyString(), eq("f-1"));

        ApiResponse response = facilitiesApi.deleteFacility("Bearer token", "f-1");
        assertTrue(response.isSuccess());
        verify(fischlFacilitiesConnector).delete("Bearer token", "f-1");
    }
}
