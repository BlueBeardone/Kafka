package za.co.bastion.tsaritsa.impl.hireling;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import za.co.bastion.common.dto.ApiResponse;
import za.co.bastion.common.exception.InvalidRequestException;
import za.co.bastion.fischl.FischlHireling;
import za.co.bastion.fischl.hireling.FischlHirelingConnector;
import za.co.bastion.tsaritsa.api.hireling.TsaritsaHirelingApi;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TsaritsaHirelingApiImplTest {

    @Mock
    FischlHirelingConnector fischlHirelingConnector;

    TsaritsaHirelingApi hirelingApi;

    @BeforeEach
    void setUp() throws Exception {
        hirelingApi = new TsaritsaHirelingApiImpl();
        Field field = TsaritsaHirelingApiImpl.class.getDeclaredField("fischlHirelingConnector");
        field.setAccessible(true);
        field.set(hirelingApi, fischlHirelingConnector);
    }

    @Test
    void createHireling_returnsSuccess() throws Exception {
        TsaritsaHirelingApi.Hireling hireling = new TsaritsaHirelingApi.Hireling()
                .setName("Smith").setBastionId("b-1");

        when(fischlHirelingConnector.create(any(FischlHireling.class)))
                .thenReturn(new FischlHireling().setId("h-1"));

        ApiResponse response = hirelingApi.createHireling(hireling);
        assertTrue(response.isSuccess());
    }

    @Test
    void createHireling_nullName_throwsException() {
        assertThrows(InvalidRequestException.class, () ->
                hirelingApi.createHireling(new TsaritsaHirelingApi.Hireling().setBastionId("b-1")));
    }

    @Test
    void createHireling_nullBastionId_throwsException() {
        assertThrows(InvalidRequestException.class, () ->
                hirelingApi.createHireling(new TsaritsaHirelingApi.Hireling().setName("Smith")));
    }

    @Test
    void getHireling_returnsSuccess() throws Exception {
        when(fischlHirelingConnector.read("h-1"))
                .thenReturn(new FischlHireling().setId("h-1").setName("Smith"));

        ApiResponse response = hirelingApi.getHireling("h-1");
        assertTrue(response.isSuccess());
    }

    @Test
    void updateHireling_returnsSuccess() throws Exception {
        when(fischlHirelingConnector.update(eq("h-1"), any(FischlHireling.class)))
                .thenReturn(new FischlHireling().setId("h-1"));

        ApiResponse response = hirelingApi.updateHireling("h-1",
                new TsaritsaHirelingApi.Hireling().setName("Smith").setBastionId("b-1"));
        assertTrue(response.isSuccess());
    }

    @Test
    void deleteHireling_returnsSuccess() throws Exception {
        doNothing().when(fischlHirelingConnector).delete("h-1");

        ApiResponse response = hirelingApi.deleteHireling("h-1");
        assertTrue(response.isSuccess());
        verify(fischlHirelingConnector).delete("h-1");
    }
}