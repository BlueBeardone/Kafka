package za.co.bastion.maurabastion.impl.bastion;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import za.co.bastion.common.dto.ApiResponse;
import za.co.bastion.fischl.FischlBastion;
import za.co.bastion.fischl.bastion.FischlBastionConnector;
import za.co.bastion.maurabastion.api.bastion.MauraBastionBastionApi;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MauraBastionBastionApiImplTest {

    @Mock
    FischlBastionConnector fischlBastionConnector;

    MauraBastionBastionApi bastionApi;

    @BeforeEach
    void setUp() throws Exception {
        bastionApi = new MauraBastionBastionApiImpl();
        Field field = MauraBastionBastionApiImpl.class.getDeclaredField("fischlBastionConnector");
        field.setAccessible(true);
        field.set(bastionApi, fischlBastionConnector);
    }

    @Test
    void createBastion_withValidData_returnsSuccess() throws Exception {
        MauraBastionBastionApi.Bastion bastion = new MauraBastionBastionApi.Bastion()
                .setId("b-1").setName("Test Bastion").setDescription("A test bastion");

        when(fischlBastionConnector.create(any(FischlBastion.class)))
                .thenReturn(new FischlBastion().setId("b-1").setName("Test Bastion"));

        ApiResponse response = bastionApi.createBastion(bastion);
        assertTrue(response.isSuccess());
    }

    @Test
    void createBastion_withBlankId_throwsException() {
        MauraBastionBastionApi.Bastion bastion = new MauraBastionBastionApi.Bastion()
                .setId("").setName("Test").setDescription("A test");

        assertThrows(RuntimeException.class, () -> bastionApi.createBastion(bastion));
    }

    @Test
    void getAllBastions_returnsSuccess() throws Exception {
        when(fischlBastionConnector.readAll())
                .thenReturn(List.of(new FischlBastion().setId("b-1").setName("Test")));

        ApiResponse response = bastionApi.getAllBastions();
        assertTrue(response.isSuccess());
    }

    @Test
    void deleteBastion_returnsSuccess() throws Exception {
        doNothing().when(fischlBastionConnector).delete("b-1");

        ApiResponse response = bastionApi.deleteBastion("b-1");
        assertTrue(response.isSuccess());
    }
}
