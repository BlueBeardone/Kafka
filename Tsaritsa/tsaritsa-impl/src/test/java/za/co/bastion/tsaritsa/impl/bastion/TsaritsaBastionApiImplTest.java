package za.co.bastion.tsaritsa.impl.bastion;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import za.co.bastion.common.dto.ApiResponse;
import za.co.bastion.common.exception.InvalidRequestException;
import za.co.bastion.fischl.FischlBastion;
import za.co.bastion.fischl.bastion.FischlBastionConnector;
import za.co.bastion.tsaritsa.api.bastion.TsaritsaBastionApi;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TsaritsaBastionApiImplTest {

    @Mock
    FischlBastionConnector fischlBastionConnector;

    TsaritsaBastionApi bastionApi;

    @BeforeEach
    void setUp() throws Exception {
        bastionApi = new TsaritsaBastionApiImpl();
        Field field = TsaritsaBastionApiImpl.class.getDeclaredField("fischlBastionConnector");
        field.setAccessible(true);
        field.set(bastionApi, fischlBastionConnector);
    }

    @Test
    void createBastion_returnsSuccess() throws Exception {
        TsaritsaBastionApi.Bastion bastion = new TsaritsaBastionApi.Bastion()
                .setName("Citadel").setDescription("The capital");

        when(fischlBastionConnector.create(eq("Bearer token"), any(FischlBastion.class)))
                .thenReturn(new FischlBastion().setName("Citadel"));

        ApiResponse response = bastionApi.createBastion("Bearer token", bastion);
        assertTrue(response.isSuccess());
    }

    @Test
    void createBastion_blankName_throwsException() {
        assertThrows(InvalidRequestException.class, () ->
                bastionApi.createBastion("Bearer token",
                        new TsaritsaBastionApi.Bastion().setDescription("d")));
    }

    @Test
    void getBastion_returnsSuccess() throws Exception {
        when(fischlBastionConnector.read(eq("Bearer token"), eq("b-1")))
                .thenReturn(new FischlBastion().setId("b-1").setName("Citadel"));

        ApiResponse response = bastionApi.getBastion("Bearer token", "b-1");
        assertTrue(response.isSuccess());
        TsaritsaBastionApi.Bastion result = (TsaritsaBastionApi.Bastion) response.getData();
        assertEquals("b-1", result.getId());
        assertEquals("Citadel", result.getName());
    }

    @Test
    void getAllBastions_returnsSuccess() throws Exception {
        when(fischlBastionConnector.readAll(eq("Bearer token"), isNull()))
                .thenReturn(List.of(new FischlBastion().setId("b-1").setName("Citadel")));

        ApiResponse response = bastionApi.getAllBastions("Bearer token");
        assertTrue(response.isSuccess());
    }

    @Test
    void updateBastion_returnsSuccess() throws Exception {
        when(fischlBastionConnector.update(eq("Bearer token"), eq("b-1"), any(FischlBastion.class)))
                .thenReturn(new FischlBastion().setId("b-1").setName("Renamed"));

        ApiResponse response = bastionApi.updateBastion("Bearer token", "b-1",
                new TsaritsaBastionApi.Bastion().setName("Renamed").setDescription("d"));
        assertTrue(response.isSuccess());
    }

    @Test
    void deleteBastion_returnsSuccess() throws Exception {
        doNothing().when(fischlBastionConnector).delete(eq("Bearer token"), eq("b-1"));

        ApiResponse response = bastionApi.deleteBastion("Bearer token", "b-1");
        assertTrue(response.isSuccess());
        verify(fischlBastionConnector).delete(eq("Bearer token"), eq("b-1"));
    }

    @Test
    void getBastion_nullId_throwsException() {
        assertThrows(InvalidRequestException.class, () -> bastionApi.getBastion("Bearer token", null));
    }
}