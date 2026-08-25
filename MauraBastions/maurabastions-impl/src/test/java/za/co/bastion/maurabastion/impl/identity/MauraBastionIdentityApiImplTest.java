package za.co.bastion.maurabastion.impl.identity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import za.co.bastion.common.dto.ApiResponse;
import za.co.bastion.common.exception.BackendException;
import za.co.bastion.common.exception.InvalidRequestException;
import za.co.bastion.fischl.FischlApp;
import za.co.bastion.fischl.FischlAuthResponse;
import za.co.bastion.fischl.FischlIdentityException;
import za.co.bastion.fischl.FischlLoginByUsernameRequest;
import za.co.bastion.fischl.FischlRegisterRequest;
import za.co.bastion.fischl.FischlRole;
import za.co.bastion.fischl.FischlUser;
import za.co.bastion.fischl.identity.FischlIdentityConnector;
import za.co.bastion.maurabastion.api.identity.MauraBastionIdentityApi;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MauraBastionIdentityApiImplTest {

    @Mock
    FischlIdentityConnector fischlIdentityConnector;

    MauraBastionIdentityApi identityApi;

    @BeforeEach
    void setUp() throws Exception {
        identityApi = new MauraBastionIdentityApiImpl();
        Field field = MauraBastionIdentityApiImpl.class.getDeclaredField("fischlIdentityConnector");
        field.setAccessible(true);
        field.set(identityApi, fischlIdentityConnector);
    }

    @Test
    void registerUser_withValidData_returnsSuccess() throws Exception {
        MauraBastionIdentityApi.RegisterRequest request = new MauraBastionIdentityApi.RegisterRequest()
                .setEmail("test@example.com").setUsername("testuser").setPassword("password123");

        FischlUser fischlUser = new FischlUser().setId("user-1").setDisplayName("testuser");
        when(fischlIdentityConnector.register(any(FischlRegisterRequest.class)))
                .thenReturn(new FischlAuthResponse(fischlUser, "token-123"));

        ApiResponse response = identityApi.registerUser(request);
        assertTrue(response.isSuccess());
        assertNotNull(response.getData());
    }

    @Test
    void registerUser_withBlankEmail_throwsException() {
        MauraBastionIdentityApi.RegisterRequest request = new MauraBastionIdentityApi.RegisterRequest()
                .setEmail("").setUsername("testuser").setPassword("password123");

        assertThrows(InvalidRequestException.class, () -> identityApi.registerUser(request));
    }

    @Test
    void registerUser_withBlankUsername_throwsException() {
        MauraBastionIdentityApi.RegisterRequest request = new MauraBastionIdentityApi.RegisterRequest()
                .setEmail("test@example.com").setUsername("").setPassword("password123");

        assertThrows(InvalidRequestException.class, () -> identityApi.registerUser(request));
    }

    @Test
    void registerUser_withShortUsername_throwsException() {
        MauraBastionIdentityApi.RegisterRequest request = new MauraBastionIdentityApi.RegisterRequest()
                .setEmail("test@example.com").setUsername("ab").setPassword("password123");

        assertThrows(InvalidRequestException.class, () -> identityApi.registerUser(request));
    }

    @Test
    void registerUser_withBlankPassword_throwsException() {
        MauraBastionIdentityApi.RegisterRequest request = new MauraBastionIdentityApi.RegisterRequest()
                .setEmail("test@example.com").setUsername("testuser").setPassword("");

        assertThrows(InvalidRequestException.class, () -> identityApi.registerUser(request));
    }

    @Test
    void registerUser_withShortPassword_throwsException() {
        MauraBastionIdentityApi.RegisterRequest request = new MauraBastionIdentityApi.RegisterRequest()
                .setEmail("test@example.com").setUsername("testuser").setPassword("short");

        assertThrows(InvalidRequestException.class, () -> identityApi.registerUser(request));
    }

    @Test
    void loginUser_withValidData_returnsSuccess() throws Exception {
        MauraBastionIdentityApi.LoginRequest request = new MauraBastionIdentityApi.LoginRequest()
                .setUsername("testuser").setPassword("password123");

        FischlUser fischlUser = new FischlUser().setId("user-1").setDisplayName("testuser");
        when(fischlIdentityConnector.loginByUsername(any(FischlLoginByUsernameRequest.class)))
                .thenReturn(new FischlAuthResponse(fischlUser, "token-123"));

        ApiResponse response = identityApi.loginUser(request);
        assertTrue(response.isSuccess());
        assertNotNull(response.getData());

        ArgumentCaptor<FischlLoginByUsernameRequest> captor = ArgumentCaptor.forClass(FischlLoginByUsernameRequest.class);
        verify(fischlIdentityConnector).loginByUsername(captor.capture());
        assertEquals("maurabastions", captor.getValue().getApp());
        assertEquals("testuser", captor.getValue().getUsername());
    }

    @Test
    void loginUser_withBlankUsername_throwsException() {
        MauraBastionIdentityApi.LoginRequest request = new MauraBastionIdentityApi.LoginRequest()
                .setUsername("").setPassword("password123");

        assertThrows(InvalidRequestException.class, () -> identityApi.loginUser(request));
    }

    @Test
    void loginUser_withBlankPassword_throwsException() {
        MauraBastionIdentityApi.LoginRequest request = new MauraBastionIdentityApi.LoginRequest()
                .setUsername("testuser").setPassword("");

        assertThrows(InvalidRequestException.class, () -> identityApi.loginUser(request));
    }

    @Test
    void getMe_withValidToken_returnsSuccess() throws Exception {
        when(fischlIdentityConnector.getMe("Bearer token-123"))
                .thenReturn(new FischlUser().setId("user-1").setDisplayName("Test User"));

        ApiResponse response = identityApi.getMe("Bearer token-123");
        assertTrue(response.isSuccess());
        assertNotNull(response.getData());
    }

    @Test
    void getMe_withBlankAuthHeader_throwsException() {
        assertThrows(InvalidRequestException.class, () -> identityApi.getMe(""));
    }

    @Test
    void getUser_withValidId_returnsSuccess() throws Exception {
        when(fischlIdentityConnector.read("user-1"))
                .thenReturn(new FischlUser().setId("user-1").setDisplayName("Test User"));

        ApiResponse response = identityApi.getUser("user-1");
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
    void listRoles_returnsSuccess() throws Exception {
        when(fischlIdentityConnector.listRoles("Bearer token"))
                .thenReturn(List.of(new FischlRole().setName("admin").setDescription("Admin role")));

        ApiResponse response = identityApi.listRoles("Bearer token");
        assertTrue(response.isSuccess());
        assertNotNull(response.getData());
    }

    @Test
    void listRoles_withBlankAuthHeader_throwsException() {
        assertThrows(InvalidRequestException.class, () -> identityApi.listRoles(""));
    }

    @Test
    void getRole_returnsSuccess() throws Exception {
        when(fischlIdentityConnector.getRole("admin", "Bearer token"))
                .thenReturn(new FischlRole().setName("admin").setDescription("Admin role"));

        ApiResponse response = identityApi.getRole("admin", "Bearer token");
        assertTrue(response.isSuccess());
    }

    @Test
    void listApps_returnsSuccess() throws Exception {
        when(fischlIdentityConnector.listApps("Bearer token"))
                .thenReturn(List.of(new FischlApp().setSlug("fischl").setName("Fischl").setAllowedRoles(List.of("user"))));

        ApiResponse response = identityApi.listApps("Bearer token");
        assertTrue(response.isSuccess());
        assertNotNull(response.getData());
    }

    @Test
    void listApps_withBlankAuthHeader_throwsException() {
        assertThrows(InvalidRequestException.class, () -> identityApi.listApps(""));
    }

    @Test
    void getApp_returnsSuccess() throws Exception {
        when(fischlIdentityConnector.getApp("fischl", "Bearer token"))
                .thenReturn(new FischlApp().setSlug("fischl").setName("Fischl"));

        ApiResponse response = identityApi.getApp("fischl", "Bearer token");
        assertTrue(response.isSuccess());
    }

    @Test
    void registerUser_whenBackendFails_throwsBackendException() throws Exception {
        MauraBastionIdentityApi.RegisterRequest request = new MauraBastionIdentityApi.RegisterRequest()
                .setEmail("test@example.com").setUsername("testuser").setPassword("password123");

        when(fischlIdentityConnector.register(any(FischlRegisterRequest.class)))
                .thenThrow(new FischlIdentityException().setStatus(409).setMessage("Conflict"));

        BackendException ex = assertThrows(BackendException.class, () -> identityApi.registerUser(request));
        assertEquals(409, ex.getBackendStatus());
    }
}