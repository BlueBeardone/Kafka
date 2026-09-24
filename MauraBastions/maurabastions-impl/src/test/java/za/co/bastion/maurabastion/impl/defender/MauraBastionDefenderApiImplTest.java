package za.co.bastion.maurabastion.impl.defender;

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
import za.co.bastion.fischl.FischlDefender;
import za.co.bastion.fischl.FischlDefenderException;
import za.co.bastion.fischl.bastion.FischlBastionConnector;
import za.co.bastion.fischl.defender.FischlDefenderConnector;
import za.co.bastion.maurabastion.api.defender.MauraBastionDefenderApi;
import za.co.bastion.maurabastion.impl.discord.DiscordWebhookSender;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MauraBastionDefenderApiImplTest {

    @Mock
    FischlDefenderConnector fischlDefenderConnector;

    @Mock
    FischlBastionConnector fischlBastionConnector;

    @Mock
    DiscordWebhookSender webhookSender;

    @Captor
    ArgumentCaptor<FischlDefender> fischlCaptor;

    MauraBastionDefenderApi defenderApi;

    @BeforeEach
    void setUp() throws Exception {
        defenderApi = new MauraBastionDefenderApiImpl();
        inject("fischlDefenderConnector", fischlDefenderConnector);
        inject("fischlBastionConnector", fischlBastionConnector);
        inject("webhookSender", webhookSender);
    }

    private void inject(String name, Object value) throws Exception {
        Field field = MauraBastionDefenderApiImpl.class.getDeclaredField(name);
        field.setAccessible(true);
        field.set(defenderApi, value);
    }

    @Test
    void createDefender_withValidData_returnsSuccess() throws Exception {
        MauraBastionDefenderApi.Defender defender = new MauraBastionDefenderApi.Defender()
                .setId("d-1").setName("Knight").setDescription("A brave knight")
                .setType("knight").setBastionId("b-1");

        when(fischlDefenderConnector.create(anyString(), any(FischlDefender.class)))
                .thenReturn(new FischlDefender().setId("d-1").setName("Knight"));

        ApiResponse response = defenderApi.createDefender("Bearer token", defender);
        assertTrue(response.isSuccess());
    }

    @Test
    void createDefender_mapsToFischlDefenderCorrectly() throws Exception {
        MauraBastionDefenderApi.Defender defender = new MauraBastionDefenderApi.Defender()
                .setId("d-1").setName("Beast").setDescription("A fierce beast")
                .setImgUrl("http://example.com/img.png").setType("beast")
                .setBastionId("b-1").setAcquisitionStory("Found in the wild");

        when(fischlDefenderConnector.create(anyString(), fischlCaptor.capture()))
                .thenReturn(new FischlDefender());

        defenderApi.createDefender("Bearer token", defender);

        FischlDefender captured = fischlCaptor.getValue();
        assertEquals("d-1", captured.getId());
        assertEquals("Beast", captured.getName());
        assertEquals("A fierce beast", captured.getDescription());
        assertEquals("http://example.com/img.png", captured.getImgUrl());
        assertEquals("beast", captured.getType());
        assertEquals("b-1", captured.getBastionId());
        assertEquals("Found in the wild", captured.getAcquisitionStory());
    }

    @Test
    void createDefender_mapsDefenderFromFischlCorrectly() throws Exception {
        FischlDefender fischl = new FischlDefender()
                .setId("d-1").setName("Knight").setDescription("A knight")
                .setImgUrl("http://example.com/img.png").setType("knight")
                .setBastionId("b-1").setAcquisitionStory("Hired");

        when(fischlDefenderConnector.create(anyString(), any(FischlDefender.class)))
                .thenReturn(fischl);

        MauraBastionDefenderApi.Defender input = new MauraBastionDefenderApi.Defender()
                .setId("d-1").setName("Knight").setType("knight").setBastionId("b-1");

        ApiResponse response = defenderApi.createDefender("Bearer token", input);
        assertTrue(response.isSuccess());

        MauraBastionDefenderApi.Defender result = (MauraBastionDefenderApi.Defender) response.getData();
        assertEquals("d-1", result.getId());
        assertEquals("Knight", result.getName());
        assertEquals("A knight", result.getDescription());
        assertEquals("http://example.com/img.png", result.getImgUrl());
        assertEquals("knight", result.getType());
        assertEquals("b-1", result.getBastionId());
        assertEquals("Hired", result.getAcquisitionStory());
    }

    @Test
    void createDefender_nullType_throwsException() {
        MauraBastionDefenderApi.Defender defender = new MauraBastionDefenderApi.Defender()
                .setId("d-1").setType(null).setBastionId("b-1");

        assertThrows(InvalidRequestException.class, () -> defenderApi.createDefender("Bearer token", defender));
    }

    @Test
    void createDefender_blankType_throwsException() {
        MauraBastionDefenderApi.Defender defender = new MauraBastionDefenderApi.Defender()
                .setId("d-1").setType("").setBastionId("b-1");

        assertThrows(InvalidRequestException.class, () -> defenderApi.createDefender("Bearer token", defender));
    }

    @Test
    void createDefender_nullBastionId_throwsException() {
        MauraBastionDefenderApi.Defender defender = new MauraBastionDefenderApi.Defender()
                .setId("d-1").setType("knight").setBastionId(null);

        assertThrows(InvalidRequestException.class, () -> defenderApi.createDefender("Bearer token", defender));
    }

    @Test
    void createDefender_blankBastionId_throwsException() {
        MauraBastionDefenderApi.Defender defender = new MauraBastionDefenderApi.Defender()
                .setId("d-1").setType("knight").setBastionId("");

        assertThrows(InvalidRequestException.class, () -> defenderApi.createDefender("Bearer token", defender));
    }

    @Test
    void createDefender_optionalFields_canBeNull() throws Exception {
        MauraBastionDefenderApi.Defender defender = new MauraBastionDefenderApi.Defender()
                .setId("d-2").setName("Guard").setType("bastion_defender").setBastionId("b-1");

        when(fischlDefenderConnector.create(anyString(), any(FischlDefender.class)))
                .thenReturn(new FischlDefender().setId("d-2"));

        ApiResponse response = defenderApi.createDefender("Bearer token", defender);
        assertTrue(response.isSuccess());
    }

    @Test
    void getDefender_returnsSuccess() throws Exception {
        when(fischlDefenderConnector.read(anyString(), eq("d-1")))
                .thenReturn(new FischlDefender().setId("d-1").setName("Knight"));

        ApiResponse response = defenderApi.getDefender("Bearer token", "d-1");
        assertTrue(response.isSuccess());
    }

    @Test
    void getDefender_nullId_throwsException() {
        assertThrows(InvalidRequestException.class, () -> defenderApi.getDefender("Bearer token", null));
    }

    @Test
    void getDefender_blankId_throwsException() {
        assertThrows(InvalidRequestException.class, () -> defenderApi.getDefender("Bearer token", ""));
    }

    @Test
    void getAllDefenders_returnsSuccess() throws Exception {
        when(fischlDefenderConnector.readAll(anyString()))
                .thenReturn(List.of(new FischlDefender().setId("d-1").setName("Knight")));

        ApiResponse response = defenderApi.getAllDefenders("Bearer token");
        assertTrue(response.isSuccess());
    }

    @Test
    void updateDefender_returnsSuccess() throws Exception {
        MauraBastionDefenderApi.Defender defender = new MauraBastionDefenderApi.Defender()
                .setName("Updated").setType("beast").setBastionId("b-1");

        when(fischlDefenderConnector.update(anyString(), eq("d-1"), any(FischlDefender.class)))
                .thenReturn(new FischlDefender().setId("d-1").setName("Updated"));

        ApiResponse response = defenderApi.updateDefender("Bearer token", "d-1", defender);
        assertTrue(response.isSuccess());
    }

    @Test
    void updateDefender_nullId_throwsException() {
        MauraBastionDefenderApi.Defender defender = new MauraBastionDefenderApi.Defender()
                .setType("knight").setBastionId("b-1");

        assertThrows(InvalidRequestException.class, () -> defenderApi.updateDefender("Bearer token", null, defender));
    }

    @Test
    void createDefender_whenBackendFails_throwsBackendException() throws Exception {
        MauraBastionDefenderApi.Defender defender = new MauraBastionDefenderApi.Defender()
                .setType("knight").setBastionId("b-1");

        when(fischlDefenderConnector.create(anyString(), any(FischlDefender.class)))
                .thenThrow(new FischlDefenderException().setStatus(403).setMessage("Forbidden"));

        BackendException ex = assertThrows(BackendException.class, () -> defenderApi.createDefender("Bearer token", defender));
        assertEquals(403, ex.getBackendStatus());
    }

    @Test
    void deleteDefender_returnsSuccess() throws Exception {
        doNothing().when(fischlDefenderConnector).delete(anyString(), eq("d-1"));

        ApiResponse response = defenderApi.deleteDefender("Bearer token", "d-1");
        assertTrue(response.isSuccess());
    }

    @Test
    void deleteDefender_nullId_throwsException() {
        assertThrows(InvalidRequestException.class, () -> defenderApi.deleteDefender("Bearer token", null));
    }

    @Test
    void createDefender_withThreadId_refreshesForumPost() throws Exception {
        MauraBastionDefenderApi.Defender defender = new MauraBastionDefenderApi.Defender()
                .setType("Knight").setBastionId("b-1");

        when(fischlDefenderConnector.create(anyString(), any(FischlDefender.class)))
                .thenReturn(new FischlDefender().setId("d-1"));
        when(fischlBastionConnector.read("Bearer token", "b-1"))
                .thenReturn(new FischlBastion().setId("b-1").setDiscordThreadId("t-1")
                        .setDefenders(List.of(new FischlDefender().setName("Knight"))));

        ApiResponse response = defenderApi.createDefender("Bearer token", defender);

        assertTrue(response.isSuccess());
        verify(webhookSender).editPost(eq(DiscordWebhookSender.Channel.BASTION_CREATION),
                eq("t-1"), contains("Defenders:** 1"));
    }

    @Test
    void createDefender_withoutThreadId_skipsForumEdit() throws Exception {
        MauraBastionDefenderApi.Defender defender = new MauraBastionDefenderApi.Defender()
                .setType("Knight").setBastionId("b-1");

        when(fischlDefenderConnector.create(anyString(), any(FischlDefender.class)))
                .thenReturn(new FischlDefender().setId("d-1"));
        when(fischlBastionConnector.read("Bearer token", "b-1"))
                .thenReturn(new FischlBastion().setId("b-1"));

        ApiResponse response = defenderApi.createDefender("Bearer token", defender);

        assertTrue(response.isSuccess());
        verify(webhookSender, never()).editPost(any(), anyString(), anyString());
    }

    @Test
    void createDefender_whenBastionReadFails_stillReturnsSuccess() throws Exception {
        MauraBastionDefenderApi.Defender defender = new MauraBastionDefenderApi.Defender()
                .setType("Knight").setBastionId("b-1");

        when(fischlDefenderConnector.create(anyString(), any(FischlDefender.class)))
                .thenReturn(new FischlDefender().setId("d-1"));
        when(fischlBastionConnector.read("Bearer token", "b-1"))
                .thenThrow(new FischlBastionException().setStatus(500).setMessage("boom"));

        ApiResponse response = defenderApi.createDefender("Bearer token", defender);

        assertTrue(response.isSuccess());
        verify(webhookSender, never()).editPost(any(), anyString(), anyString());
    }

    @Test
    void createDefender_whenForumEditFails_stillReturnsSuccess() throws Exception {
        MauraBastionDefenderApi.Defender defender = new MauraBastionDefenderApi.Defender()
                .setType("Knight").setBastionId("b-1");

        when(fischlDefenderConnector.create(anyString(), any(FischlDefender.class)))
                .thenReturn(new FischlDefender().setId("d-1"));
        when(fischlBastionConnector.read("Bearer token", "b-1"))
                .thenReturn(new FischlBastion().setId("b-1").setDiscordThreadId("t-1"));
        doThrow(new BackendException(502, "Discord webhook error: HTTP 404"))
                .when(webhookSender).editPost(any(), anyString(), anyString());

        ApiResponse response = defenderApi.createDefender("Bearer token", defender);

        assertTrue(response.isSuccess());
    }

    @Test
    void updateDefender_withThreadId_refreshesForumPost() throws Exception {
        MauraBastionDefenderApi.Defender defender = new MauraBastionDefenderApi.Defender()
                .setType("Knight").setBastionId("b-1");

        when(fischlDefenderConnector.update(anyString(), eq("d-1"), any(FischlDefender.class)))
                .thenReturn(new FischlDefender().setId("d-1"));
        when(fischlBastionConnector.read("Bearer token", "b-1"))
                .thenReturn(new FischlBastion().setId("b-1").setDiscordThreadId("t-1")
                        .setDefenders(List.of(new FischlDefender().setName("Knight"))));

        ApiResponse response = defenderApi.updateDefender("Bearer token", "d-1", defender);

        assertTrue(response.isSuccess());
        verify(webhookSender).editPost(eq(DiscordWebhookSender.Channel.BASTION_CREATION),
                eq("t-1"), contains("Defenders:** 1"));
    }

    @Test
    void deleteDefender_withThreadId_refreshesForumPost() throws Exception {
        when(fischlDefenderConnector.read(anyString(), eq("d-1")))
                .thenReturn(new FischlDefender().setId("d-1").setBastionId("b-1"));
        doNothing().when(fischlDefenderConnector).delete(anyString(), eq("d-1"));
        when(fischlBastionConnector.read("Bearer token", "b-1"))
                .thenReturn(new FischlBastion().setId("b-1").setDiscordThreadId("t-1"));

        ApiResponse response = defenderApi.deleteDefender("Bearer token", "d-1");

        assertTrue(response.isSuccess());
        verify(webhookSender).editPost(eq(DiscordWebhookSender.Channel.BASTION_CREATION),
                eq("t-1"), anyString());
    }

    @Test
    void createDefender_forwardsAuthHeaderToConnector() throws Exception {
        MauraBastionDefenderApi.Defender defender = new MauraBastionDefenderApi.Defender()
                .setType("knight").setBastionId("b-1");

        when(fischlDefenderConnector.create(eq("Bearer token"), any(FischlDefender.class)))
                .thenReturn(new FischlDefender().setId("d-1"));

        defenderApi.createDefender("Bearer token", defender);

        verify(fischlDefenderConnector).create(eq("Bearer token"), any(FischlDefender.class));
    }

    @Test
    void getDefender_forwardsAuthHeaderToConnector() throws Exception {
        when(fischlDefenderConnector.read(eq("Bearer token"), eq("d-1")))
                .thenReturn(new FischlDefender().setId("d-1"));

        defenderApi.getDefender("Bearer token", "d-1");

        verify(fischlDefenderConnector).read("Bearer token", "d-1");
    }
}
