package za.co.bastion.maurabastion.impl.bastion;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import za.co.bastion.common.dto.ApiResponse;
import za.co.bastion.fischl.FischlBastion;
import za.co.bastion.fischl.bastion.FischlBastionConnector;
import za.co.bastion.maurabastion.api.bastion.MauraBastionBastionApi;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@QuarkusTest
class MauraBastionBastionApiImplTest {

    @Inject
    MauraBastionBastionApi bastionApi;

    @InjectMock
    FischlBastionConnector fischlBastionConnector;

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
