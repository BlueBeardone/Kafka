package za.co.bastion.tsaritsa.impl.identity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import za.co.bastion.common.dto.ApiResponse;
import za.co.bastion.common.exception.InvalidRequestException;
import za.co.bastion.fischl.*;
import za.co.bastion.fischl.identity.FischlIdentityConnector;
import za.co.bastion.tsaritsa.api.identity.TsaritsaIdentityApi;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TsaritsaIdentityApiImplTest {

    @Mock
    FischlIdentityConnector fischlIdentityConnector;

    @Captor
    ArgumentCaptor<FischlRole> roleCaptor;

    TsaritsaIdentityApi identityApi;

    @BeforeEach
    void setUp() throws Exception {
        identityApi = new TsaritsaIdentityApiImpl();
        Field field = TsaritsaIdentityApiImpl.class.getDeclaredField("fischlIdentityConnector");
        field.setAccessible(true);
        field.set(identityApi, fischlIdentityConnector);
    }

    @Test
    void createRole_returnsSuccess() throws Exception {
        when(fischlIdentityConnector.createRole(eq("Bearer t"), any(FischlRole.class)))
                .thenReturn(new FischlRole("admin", "Administrator"));

        ApiResponse response = identityApi.createRole("Bearer t",
                new TsaritsaIdentityApi.Role().setName("admin").setDescription("Administrator"));
        assertTrue(response.isSuccess());
    }

    @Test
    void createRole_mapsToFischlCorrectly() throws Exception {
        when(fischlIdentityConnector.createRole(eq("Bearer t"), roleCaptor.capture()))
                .thenReturn(new FischlRole());

        identityApi.createRole("Bearer t",
                new TsaritsaIdentityApi.Role().setName("moderator").setDescription("Moderates"));

        FischlRole captured = roleCaptor.getValue();
        assertEquals("moderator", captured.getName());
        assertEquals("Moderates", captured.getDescription());
    }

    @Test
    void createRole_blankName_throwsException() {
        assertThrows(InvalidRequestException.class, () ->
                identityApi.createRole("Bearer t", new TsaritsaIdentityApi.Role().setDescription("x")));
    }

    @Test
    void updateRole_returnsSuccess() throws Exception {
        when(fischlIdentityConnector.updateRole(eq("admin"), eq("Bearer t"), any(FischlRole.class)))
                .thenReturn(new FischlRole("admin", "Administrator v2"));

        ApiResponse response = identityApi.updateRole("admin", "Bearer t",
                new TsaritsaIdentityApi.Role().setName("admin").setDescription("Administrator v2"));
        assertTrue(response.isSuccess());
    }

    @Test
    void updateRole_nullName_throwsException() {
        assertThrows(InvalidRequestException.class, () ->
                identityApi.updateRole(null, "Bearer t", new TsaritsaIdentityApi.Role()));
    }

    @Test
    void deleteRole_delegates() throws Exception {
        doNothing().when(fischlIdentityConnector).deleteRole("admin", "Bearer t");

        ApiResponse response = identityApi.deleteRole("admin", "Bearer t");
        assertTrue(response.isSuccess());
        verify(fischlIdentityConnector).deleteRole("admin", "Bearer t");
    }

    @Test
    void deleteRole_blankName_throwsException() {
        assertThrows(InvalidRequestException.class, () -> identityApi.deleteRole("", "Bearer t"));
    }

    @Test
    void addRole_delegates() throws Exception {
        when(fischlIdentityConnector.addRole(eq("u-1"), eq("Bearer t"), any(FischlAssignRoleRequest.class)))
                .thenReturn(new FischlUser().setId("u-1").setRoles(List.of("admin")));

        ApiResponse response = identityApi.addRole("u-1", "Bearer t",
                new TsaritsaIdentityApi.AssignRoleRequest().setRole("admin"));
        assertTrue(response.isSuccess());
    }

    @Test
    void removeRole_delegates() throws Exception {
        when(fischlIdentityConnector.removeRole("u-1", "admin", "Bearer t"))
                .thenReturn(new FischlUser().setId("u-1"));

        ApiResponse response = identityApi.removeRole("u-1", "admin", "Bearer t");
        assertTrue(response.isSuccess());
        verify(fischlIdentityConnector).removeRole("u-1", "admin", "Bearer t");
    }

    @Test
    void createApp_delegates() throws Exception {
        when(fischlIdentityConnector.createApp(eq("Bearer t"), any(FischlApp.class)))
                .thenReturn(new FischlApp("maura", "Maura Bastion", List.of("admin")));

        ApiResponse response = identityApi.createApp("Bearer t",
                new TsaritsaIdentityApi.App().setSlug("maura").setName("Maura Bastion").setAllowedRoles(List.of("admin")));
        assertTrue(response.isSuccess());
    }

    @Test
    void updateApp_delegates() throws Exception {
        when(fischlIdentityConnector.updateApp(eq("maura"), eq("Bearer t"), any(FischlApp.class)))
                .thenReturn(new FischlApp("maura", "Maura", List.of("admin")));

        ApiResponse response = identityApi.updateApp("maura", "Bearer t",
                new TsaritsaIdentityApi.App().setName("Maura"));
        assertTrue(response.isSuccess());
    }

    @Test
    void deleteApp_delegates() throws Exception {
        doNothing().when(fischlIdentityConnector).deleteApp("maura", "Bearer t");

        ApiResponse response = identityApi.deleteApp("maura", "Bearer t");
        assertTrue(response.isSuccess());
        verify(fischlIdentityConnector).deleteApp("maura", "Bearer t");
    }

    @Test
    void getApp_mapsResult() throws Exception {
        when(fischlIdentityConnector.getApp("maura", "Bearer t"))
                .thenReturn(new FischlApp("maura", "Maura Bastion", List.of("admin")));

        ApiResponse response = identityApi.getApp("maura", "Bearer t");
        assertTrue(response.isSuccess());
        TsaritsaIdentityApi.App result = (TsaritsaIdentityApi.App) response.getData();
        assertEquals("maura", result.getSlug());
        assertEquals("Maura Bastion", result.getName());
        assertEquals(List.of("admin"), result.getAllowedRoles());
    }
}