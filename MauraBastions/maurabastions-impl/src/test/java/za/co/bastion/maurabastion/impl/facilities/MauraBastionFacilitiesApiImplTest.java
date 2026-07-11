package za.co.bastion.maurabastion.impl.facilities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import za.co.bastion.common.dto.ApiResponse;
import za.co.bastion.common.dto.Rank;
import za.co.bastion.fischl.FischlFacility;
import za.co.bastion.fischl.facilities.FischlFacilitiesConnector;
import za.co.bastion.maurabastion.api.facilities.MauraBastionFacilitiesApi;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MauraBastionFacilitiesApiImplTest {

    @Mock
    FischlFacilitiesConnector fischlFacilitiesConnector;

    MauraBastionFacilitiesApi facilitiesApi;

    @BeforeEach
    void setUp() throws Exception {
        facilitiesApi = new MauraBastionFacilitiesApiImpl();
        Field field = MauraBastionFacilitiesApiImpl.class.getDeclaredField("fischlFacilitiesConnector");
        field.setAccessible(true);
        field.set(facilitiesApi, fischlFacilitiesConnector);
    }

    @Test
    void createFacility_withValidData_returnsSuccess() throws Exception {
        MauraBastionFacilitiesApi.Facility facility = new MauraBastionFacilitiesApi.Facility()
                .setId("f-1").setName("Barracks").setRank(Rank.A).setHirelingAmount(5);

        when(fischlFacilitiesConnector.create(any(FischlFacility.class)))
                .thenReturn(new FischlFacility().setId("f-1").setName("Barracks"));

        ApiResponse response = facilitiesApi.createFacility(facility);
        assertTrue(response.isSuccess());
    }

    @Test
    void createFacility_withBlankId_throwsException() {
        MauraBastionFacilitiesApi.Facility facility = new MauraBastionFacilitiesApi.Facility()
                .setId("").setName("Test").setRank(Rank.A).setHirelingAmount(1);

        assertThrows(RuntimeException.class, () -> facilitiesApi.createFacility(facility));
    }

    @Test
    void getAllFacilities_returnsSuccess() throws Exception {
        when(fischlFacilitiesConnector.readAll())
                .thenReturn(List.of(new FischlFacility().setId("f-1").setName("Barracks")));

        ApiResponse response = facilitiesApi.getAllFacilities();
        assertTrue(response.isSuccess());
    }

    @Test
    void deleteFacility_returnsSuccess() throws Exception {
        doNothing().when(fischlFacilitiesConnector).delete("f-1");

        ApiResponse response = facilitiesApi.deleteFacility("f-1");
        assertTrue(response.isSuccess());
    }
}
