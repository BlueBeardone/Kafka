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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
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

        when(fischlHirelingConnector.create(anyString(), any(FischlHireling.class)))
                .thenReturn(new FischlHireling().setId("h-1"));

        ApiResponse response = hirelingApi.createHireling("Bearer token", hireling);
        assertTrue(response.isSuccess());
    }

    @Test
    void createHireling_forwardsAuthHeaderToConnector() throws Exception {
        TsaritsaHirelingApi.Hireling hireling = new TsaritsaHirelingApi.Hireling()
                .setName("Smith").setBastionId("b-1");

        when(fischlHirelingConnector.create(eq("Bearer token"), any(FischlHireling.class)))
                .thenReturn(new FischlHireling().setId("h-1"));

        hirelingApi.createHireling("Bearer token", hireling);

        verify(fischlHirelingConnector).create(eq("Bearer token"), any(FischlHireling.class));
    }

    @Test
    void createHireling_nullName_throwsException() {
        assertThrows(InvalidRequestException.class, () ->
                hirelingApi.createHireling("Bearer token",
                        new TsaritsaHirelingApi.Hireling().setBastionId("b-1")));
    }

    @Test
    void createHireling_nullBastionId_throwsException() {
        assertThrows(InvalidRequestException.class, () ->
                hirelingApi.createHireling("Bearer token",
                        new TsaritsaHirelingApi.Hireling().setName("Smith")));
    }

    @Test
    void getHireling_returnsSuccess() throws Exception {
        when(fischlHirelingConnector.read(anyString(), eq("h-1")))
                .thenReturn(new FischlHireling().setId("h-1").setName("Smith"));

        ApiResponse response = hirelingApi.getHireling("Bearer token", "h-1");
        assertTrue(response.isSuccess());
    }

    @Test
    void getHireling_forwardsAuthHeaderToConnector() throws Exception {
        when(fischlHirelingConnector.read(eq("Bearer token"), eq("h-1")))
                .thenReturn(new FischlHireling().setId("h-1"));

        hirelingApi.getHireling("Bearer token", "h-1");

        verify(fischlHirelingConnector).read("Bearer token", "h-1");
    }

    @Test
    void getAllHirelings_returnsSuccess() throws Exception {
        when(fischlHirelingConnector.readAll(anyString()))
                .thenReturn(List.of(new FischlHireling().setId("h-1").setName("Smith")));

        ApiResponse response = hirelingApi.getAllHirelings("Bearer token");
        assertTrue(response.isSuccess());

        verify(fischlHirelingConnector).readAll("Bearer token");
    }

    @Test
    void updateHireling_returnsSuccess() throws Exception {
        when(fischlHirelingConnector.update(anyString(), eq("h-1"), any(FischlHireling.class)))
                .thenReturn(new FischlHireling().setId("h-1"));

        ApiResponse response = hirelingApi.updateHireling("Bearer token", "h-1",
                new TsaritsaHirelingApi.Hireling().setName("Smith").setBastionId("b-1"));
        assertTrue(response.isSuccess());
    }

    @Test
    void updateHireling_forwardsAuthHeaderToConnector() throws Exception {
        when(fischlHirelingConnector.update(eq("Bearer token"), eq("h-1"), any(FischlHireling.class)))
                .thenReturn(new FischlHireling().setId("h-1"));

        hirelingApi.updateHireling("Bearer token", "h-1",
                new TsaritsaHirelingApi.Hireling().setName("Smith").setBastionId("b-1"));

        verify(fischlHirelingConnector).update(eq("Bearer token"), eq("h-1"), any(FischlHireling.class));
    }

    @Test
    void deleteHireling_returnsSuccess() throws Exception {
        doNothing().when(fischlHirelingConnector).delete(anyString(), eq("h-1"));

        ApiResponse response = hirelingApi.deleteHireling("Bearer token", "h-1");
        assertTrue(response.isSuccess());
        verify(fischlHirelingConnector).delete("Bearer token", "h-1");
    }
}
