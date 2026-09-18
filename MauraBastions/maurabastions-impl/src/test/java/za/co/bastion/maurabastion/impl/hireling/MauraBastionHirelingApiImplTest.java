package za.co.bastion.maurabastion.impl.hireling;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import za.co.bastion.common.dto.ApiResponse;
import za.co.bastion.common.exception.BackendException;
import za.co.bastion.common.exception.InvalidRequestException;
import za.co.bastion.fischl.FischlBastion;
import za.co.bastion.fischl.FischlBastionException;
import za.co.bastion.fischl.FischlHireling;
import za.co.bastion.fischl.FischlHirelingException;
import za.co.bastion.fischl.bastion.FischlBastionConnector;
import za.co.bastion.fischl.hireling.FischlHirelingConnector;
import za.co.bastion.maurabastion.api.hireling.MauraBastionHirelingApi;
import za.co.bastion.maurabastion.impl.discord.DiscordWebhookSender;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MauraBastionHirelingApiImplTest {

    @Mock
    FischlHirelingConnector fischlHirelingConnector;

    @Mock
    FischlBastionConnector fischlBastionConnector;

    @Mock
    DiscordWebhookSender webhookSender;

    @Captor
    ArgumentCaptor<FischlHireling> fischlCaptor;

    MauraBastionHirelingApi hirelingApi;

    @BeforeEach
    void setUp() throws Exception {
        hirelingApi = new MauraBastionHirelingApiImpl();
        inject("fischlHirelingConnector", fischlHirelingConnector);
        inject("fischlBastionConnector", fischlBastionConnector);
        inject("webhookSender", webhookSender);
    }

    private void inject(String name, Object value) throws Exception {
        Field field = MauraBastionHirelingApiImpl.class.getDeclaredField(name);
        field.setAccessible(true);
        field.set(hirelingApi, value);
    }

    @Test
    void createHireling_withValidData_returnsSuccess() throws Exception {
        MauraBastionHirelingApi.Hireling hireling = new MauraBastionHirelingApi.Hireling()
                .setId("h-1").setName("Guard").setRole("guard").setBastionId("b-1");

        when(fischlHirelingConnector.create(any(FischlHireling.class)))
                .thenReturn(new FischlHireling().setId("h-1").setName("Guard"));

        ApiResponse response = hirelingApi.createHireling("Bearer token", hireling);
        assertTrue(response.isSuccess());
    }

    @Test
    void createHireling_mapsToFischlHirelingCorrectly() throws Exception {
        MauraBastionHirelingApi.Hireling hireling = new MauraBastionHirelingApi.Hireling()
                .setId("h-1").setName("Archer").setRole("ranged")
                .setDescription("A skilled archer").setImgUrl("http://example.com/img.png")
                .setBastionId("b-1").setFacilityId("f-1").setAcquisitionStory("Recruited");

        when(fischlHirelingConnector.create(fischlCaptor.capture()))
                .thenReturn(new FischlHireling());

        hirelingApi.createHireling("Bearer token", hireling);

        FischlHireling captured = fischlCaptor.getValue();
        assertEquals("h-1", captured.getId());
        assertEquals("Archer", captured.getName());
        assertEquals("ranged", captured.getRole());
        assertEquals("A skilled archer", captured.getDescription());
        assertEquals("http://example.com/img.png", captured.getImgUrl());
        assertEquals("b-1", captured.getBastionId());
        assertEquals("f-1", captured.getFacilityId());
        assertEquals("Recruited", captured.getAcquisitionStory());
    }

    @Test
    void createHireling_mapsHirelingFromFischlCorrectly() throws Exception {
        FischlHireling fischl = new FischlHireling()
                .setId("h-1").setName("Archer").setRole("ranged")
                .setDescription("A skilled archer").setImgUrl("http://example.com/img.png")
                .setBastionId("b-1").setFacilityId("f-1").setAcquisitionStory("Recruited");

        when(fischlHirelingConnector.create(any(FischlHireling.class)))
                .thenReturn(fischl);

        MauraBastionHirelingApi.Hireling input = new MauraBastionHirelingApi.Hireling()
                .setId("h-1").setName("Archer").setBastionId("b-1");

        ApiResponse response = hirelingApi.createHireling("Bearer token", input);
        assertTrue(response.isSuccess());

        MauraBastionHirelingApi.Hireling result = (MauraBastionHirelingApi.Hireling) response.getData();
        assertEquals("h-1", result.getId());
        assertEquals("Archer", result.getName());
        assertEquals("ranged", result.getRole());
        assertEquals("A skilled archer", result.getDescription());
        assertEquals("http://example.com/img.png", result.getImgUrl());
        assertEquals("b-1", result.getBastionId());
        assertEquals("f-1", result.getFacilityId());
        assertEquals("Recruited", result.getAcquisitionStory());
    }

    @Test
    void createHireling_nullName_throwsException() {
        MauraBastionHirelingApi.Hireling hireling = new MauraBastionHirelingApi.Hireling()
                .setId("h-1").setName(null).setBastionId("b-1");

        assertThrows(InvalidRequestException.class, () -> hirelingApi.createHireling("Bearer token", hireling));
    }

    @Test
    void createHireling_blankName_throwsException() {
        MauraBastionHirelingApi.Hireling hireling = new MauraBastionHirelingApi.Hireling()
                .setId("h-1").setName("").setBastionId("b-1");

        assertThrows(InvalidRequestException.class, () -> hirelingApi.createHireling("Bearer token", hireling));
    }

    @Test
    void createHireling_nullBastionId_throwsException() {
        MauraBastionHirelingApi.Hireling hireling = new MauraBastionHirelingApi.Hireling()
                .setId("h-1").setName("Guard").setBastionId(null);

        assertThrows(InvalidRequestException.class, () -> hirelingApi.createHireling("Bearer token", hireling));
    }

    @Test
    void createHireling_blankBastionId_passesThroughToBackend() throws Exception {
        MauraBastionHirelingApi.Hireling hireling = new MauraBastionHirelingApi.Hireling()
                .setId("h-1").setName("Guard").setBastionId("");

        when(fischlHirelingConnector.create(fischlCaptor.capture()))
                .thenReturn(new FischlHireling().setId("h-1").setName("Guard"));

        ApiResponse response = hirelingApi.createHireling("Bearer token", hireling);
        assertTrue(response.isSuccess());
        assertEquals("", fischlCaptor.getValue().getBastionId());
    }

    @Test
    void createHireling_optionalFields_canBeNull() throws Exception {
        MauraBastionHirelingApi.Hireling hireling = new MauraBastionHirelingApi.Hireling()
                .setId("h-2").setName("Guard").setBastionId("b-1");

        when(fischlHirelingConnector.create(any(FischlHireling.class)))
                .thenReturn(new FischlHireling().setId("h-2"));

        ApiResponse response = hirelingApi.createHireling("Bearer token", hireling);
        assertTrue(response.isSuccess());
    }

    @Test
    void getHireling_returnsSuccess() throws Exception {
        when(fischlHirelingConnector.read("h-1"))
                .thenReturn(new FischlHireling().setId("h-1").setName("Guard"));

        ApiResponse response = hirelingApi.getHireling("h-1");
        assertTrue(response.isSuccess());
    }

    @Test
    void getHireling_nullId_throwsException() {
        assertThrows(InvalidRequestException.class, () -> hirelingApi.getHireling(null));
    }

    @Test
    void getHireling_blankId_throwsException() {
        assertThrows(InvalidRequestException.class, () -> hirelingApi.getHireling(""));
    }

    @Test
    void getAllHirelings_returnsSuccess() throws Exception {
        when(fischlHirelingConnector.readAll())
                .thenReturn(List.of(new FischlHireling().setId("h-1").setName("Guard")));

        ApiResponse response = hirelingApi.getAllHirelings();
        assertTrue(response.isSuccess());
    }

    @Test
    void updateHireling_returnsSuccess() throws Exception {
        MauraBastionHirelingApi.Hireling hireling = new MauraBastionHirelingApi.Hireling()
                .setName("Updated").setBastionId("b-1");

        when(fischlHirelingConnector.update(eq("h-1"), any(FischlHireling.class)))
                .thenReturn(new FischlHireling().setId("h-1").setName("Updated"));

        ApiResponse response = hirelingApi.updateHireling("Bearer token", "h-1", hireling);
        assertTrue(response.isSuccess());
    }

    @Test
    void updateHireling_nullId_throwsException() {
        MauraBastionHirelingApi.Hireling hireling = new MauraBastionHirelingApi.Hireling()
                .setName("Guard").setBastionId("b-1");

        assertThrows(InvalidRequestException.class, () -> hirelingApi.updateHireling("Bearer token", null, hireling));
    }

    @Test
    void createHireling_whenBackendFails_throwsBackendException() throws Exception {
        MauraBastionHirelingApi.Hireling hireling = new MauraBastionHirelingApi.Hireling()
                .setName("Guard").setBastionId("b-1");

        when(fischlHirelingConnector.create(any(FischlHireling.class)))
                .thenThrow(new FischlHirelingException().setStatus(401).setMessage("Unauthorized"));

        BackendException ex = assertThrows(BackendException.class, () -> hirelingApi.createHireling("Bearer token", hireling));
        assertEquals(401, ex.getBackendStatus());
    }

    @Test
    void deleteHireling_returnsSuccess() throws Exception {
        doNothing().when(fischlHirelingConnector).delete("h-1");

        ApiResponse response = hirelingApi.deleteHireling("Bearer token", "h-1");
        assertTrue(response.isSuccess());
    }

    @Test
    void deleteHireling_nullId_throwsException() {
        assertThrows(InvalidRequestException.class, () -> hirelingApi.deleteHireling("Bearer token", null));
    }

    @Test
    void createHireling_withThreadId_refreshesForumPost() throws Exception {
        MauraBastionHirelingApi.Hireling hireling = new MauraBastionHirelingApi.Hireling()
                .setName("Smith").setBastionId("b-1");

        when(fischlHirelingConnector.create(any(FischlHireling.class)))
                .thenReturn(new FischlHireling().setId("h-1"));
        when(fischlBastionConnector.read("Bearer token", "b-1"))
                .thenReturn(new FischlBastion().setId("b-1").setDiscordThreadId("t-1")
                        .setHirelings(List.of(new FischlHireling().setName("Smith"))));

        ApiResponse response = hirelingApi.createHireling("Bearer token", hireling);

        assertTrue(response.isSuccess());
        verify(webhookSender).editPost(eq(DiscordWebhookSender.Channel.BASTION_CREATION),
                eq("t-1"), contains("Hirelings:** 1"));
    }

    @Test
    void createHireling_withoutThreadId_skipsForumEdit() throws Exception {
        MauraBastionHirelingApi.Hireling hireling = new MauraBastionHirelingApi.Hireling()
                .setName("Smith").setBastionId("b-1");

        when(fischlHirelingConnector.create(any(FischlHireling.class)))
                .thenReturn(new FischlHireling().setId("h-1"));
        when(fischlBastionConnector.read("Bearer token", "b-1"))
                .thenReturn(new FischlBastion().setId("b-1"));

        ApiResponse response = hirelingApi.createHireling("Bearer token", hireling);

        assertTrue(response.isSuccess());
        verify(webhookSender, never()).editPost(any(), anyString(), anyString());
    }

    @Test
    void createHireling_whenBastionReadFails_stillReturnsSuccess() throws Exception {
        MauraBastionHirelingApi.Hireling hireling = new MauraBastionHirelingApi.Hireling()
                .setName("Smith").setBastionId("b-1");

        when(fischlHirelingConnector.create(any(FischlHireling.class)))
                .thenReturn(new FischlHireling().setId("h-1"));
        when(fischlBastionConnector.read("Bearer token", "b-1"))
                .thenThrow(new FischlBastionException().setStatus(500).setMessage("boom"));

        ApiResponse response = hirelingApi.createHireling("Bearer token", hireling);

        assertTrue(response.isSuccess());
        verify(webhookSender, never()).editPost(any(), anyString(), anyString());
    }

    @Test
    void createHireling_whenForumEditFails_stillReturnsSuccess() throws Exception {
        MauraBastionHirelingApi.Hireling hireling = new MauraBastionHirelingApi.Hireling()
                .setName("Smith").setBastionId("b-1");

        when(fischlHirelingConnector.create(any(FischlHireling.class)))
                .thenReturn(new FischlHireling().setId("h-1"));
        when(fischlBastionConnector.read("Bearer token", "b-1"))
                .thenReturn(new FischlBastion().setId("b-1").setDiscordThreadId("t-1"));
        doThrow(new BackendException(502, "Discord webhook error: HTTP 404"))
                .when(webhookSender).editPost(any(), anyString(), anyString());

        ApiResponse response = hirelingApi.createHireling("Bearer token", hireling);

        assertTrue(response.isSuccess());
    }

    @Test
    void updateHireling_withThreadId_refreshesForumPost() throws Exception {
        MauraBastionHirelingApi.Hireling hireling = new MauraBastionHirelingApi.Hireling()
                .setName("Smith").setBastionId("b-1");

        when(fischlHirelingConnector.update(eq("h-1"), any(FischlHireling.class)))
                .thenReturn(new FischlHireling().setId("h-1"));
        when(fischlBastionConnector.read("Bearer token", "b-1"))
                .thenReturn(new FischlBastion().setId("b-1").setDiscordThreadId("t-1")
                        .setHirelings(List.of(new FischlHireling().setName("Smith"))));

        ApiResponse response = hirelingApi.updateHireling("Bearer token", "h-1", hireling);

        assertTrue(response.isSuccess());
        verify(webhookSender).editPost(eq(DiscordWebhookSender.Channel.BASTION_CREATION),
                eq("t-1"), contains("Hirelings:** 1"));
    }

    @Test
    void deleteHireling_withThreadId_refreshesForumPost() throws Exception {
        when(fischlHirelingConnector.read("h-1"))
                .thenReturn(new FischlHireling().setId("h-1").setBastionId("b-1"));
        doNothing().when(fischlHirelingConnector).delete("h-1");
        when(fischlBastionConnector.read("Bearer token", "b-1"))
                .thenReturn(new FischlBastion().setId("b-1").setDiscordThreadId("t-1"));

        ApiResponse response = hirelingApi.deleteHireling("Bearer token", "h-1");

        assertTrue(response.isSuccess());
        verify(webhookSender).editPost(eq(DiscordWebhookSender.Channel.BASTION_CREATION),
                eq("t-1"), anyString());
    }
}