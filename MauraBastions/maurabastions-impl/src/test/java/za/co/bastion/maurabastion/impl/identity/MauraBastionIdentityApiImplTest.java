package za.co.bastion.maurabastion.impl.identity;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import za.co.bastion.common.dto.ApiResponse;
import za.co.bastion.fischl.FischlUser;
import za.co.bastion.fischl.identity.FischlIdentityConnector;
import za.co.bastion.maurabastion.api.identity.MauraBastionIdentityApi;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@QuarkusTest
class MauraBastionIdentityApiImplTest {

    @Inject
    MauraBastionIdentityApi identityApi;

    @InjectMock
    FischlIdentityConnector fischlIdentityConnector;

    @Test
    void createUser_withValidData_returnsSuccess() throws Exception {
        MauraBastionIdentityApi.User user = new MauraBastionIdentityApi.User()
                .setId("user-1").setDisplayName("Test User").setBastionId("bastion-1");

        when(fischlIdentityConnector.create(any(FischlUser.class)))
                .thenReturn(new FischlUser().setId("user-1").setDisplayName("Test User"));

        ApiResponse response = identityApi.createUser(user);
        assertTrue(response.isSuccess());
    }

    @Test
    void createUser_withBlankId_throwsException() {
        MauraBastionIdentityApi.User user = new MauraBastionIdentityApi.User()
                .setId("").setDisplayName("Test User");

        assertThrows(RuntimeException.class, () -> identityApi.createUser(user));
    }

    @Test
    void getUser_withValidId_returnsSuccess() throws Exception {
        when(fischlIdentityConnector.read("user-1"))
                .thenReturn(new FischlUser().setId("user-1").setDisplayName("Test User"));

        ApiResponse response = identityApi.getUser("user-1");
        assertTrue(response.isSuccess());
    }

    @Test
    void getAllUsers_returnsSuccess() throws Exception {
        when(fischlIdentityConnector.readAll())
                .thenReturn(List.of(new FischlUser().setId("user-1").setDisplayName("Test User")));

        ApiResponse response = identityApi.getAllUsers();
        assertTrue(response.isSuccess());
    }

    @Test
    void updateUser_withValidData_returnsSuccess() throws Exception {
        MauraBastionIdentityApi.User user = new MauraBastionIdentityApi.User()
                .setId("user-1").setDisplayName("Updated User");

        when(fischlIdentityConnector.update(eq("user-1"), any(FischlUser.class)))
                .thenReturn(new FischlUser().setId("user-1").setDisplayName("Updated User"));

        ApiResponse response = identityApi.updateUser("user-1", user);
        assertTrue(response.isSuccess());
    }

    @Test
    void deleteUser_returnsSuccess() throws Exception {
        doNothing().when(fischlIdentityConnector).delete("user-1");

        ApiResponse response = identityApi.deleteUser("user-1");
        assertTrue(response.isSuccess());
    }
}
