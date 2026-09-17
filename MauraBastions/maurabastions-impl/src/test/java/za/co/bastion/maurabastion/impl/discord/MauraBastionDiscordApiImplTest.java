package za.co.bastion.maurabastion.impl.discord;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import za.co.bastion.common.dto.ApiResponse;
import za.co.bastion.common.exception.BackendException;
import za.co.bastion.common.exception.InvalidRequestException;
import za.co.bastion.fischl.FischlBastion;
import za.co.bastion.fischl.FischlBastionException;
import za.co.bastion.fischl.FischlIdentityException;
import za.co.bastion.fischl.FischlUser;
import za.co.bastion.fischl.bastion.FischlBastionConnector;
import za.co.bastion.fischl.identity.FischlIdentityConnector;
import za.co.bastion.maurabastion.api.discord.MauraBastionDiscordApi;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MauraBastionDiscordApiImplTest {

    @Mock
    FischlIdentityConnector fischlIdentityConnector;

    @Mock
    DiscordWebhookSender webhookSender;

    @Mock
    FischlBastionConnector fischlBastionConnector;

    MauraBastionDiscordApi discordApi;

    @BeforeEach
    void setUp() throws Exception {
        discordApi = new MauraBastionDiscordApiImpl();
        setField(discordApi, "webhookSender", webhookSender);
        setField(discordApi, "fischlIdentityConnector", fischlIdentityConnector);
        setField(discordApi, "fischlBastionConnector", fischlBastionConnector);
        lenient().when(fischlIdentityConnector.getMe(anyString()))
                .thenReturn(new FischlUser().setDisplayName("Steve"));
    }

    private static void setField(Object target, String name, Object value) throws Exception {
        java.lang.reflect.Field field = target.getClass().getDeclaredField(name);
        field.setAccessible(true);
        field.set(target, value);
    }

    @Test
    void sendMessage_sendsToGeneralChannel() throws Exception {
        ApiResponse response = discordApi.sendMessage("Bearer token", "hello");

        assertTrue(response.isSuccess());
        InOrder inOrder = inOrder(fischlIdentityConnector, webhookSender);
        inOrder.verify(fischlIdentityConnector).getMe("Bearer token");
        inOrder.verify(webhookSender).send(DiscordWebhookSender.Channel.GENERAL, "hello");
    }

    @Test
    void sendMessage_withBlankMessage_throwsInvalidRequest() {
        assertThrows(InvalidRequestException.class, () -> discordApi.sendMessage("Bearer token", "   "));
        verifyNoInteractions(webhookSender);
    }

    @Test
    void sendIndividualBastionTurn_formatsResultWithDisplayName() {
        MauraBastionDiscordApi.BastionTurnResult result = new MauraBastionDiscordApi.BastionTurnResult()
                .setBastionId("b-1")
                .setBastionName("Ravencrest")
                .setQuest("Fortify the walls");

        ApiResponse response = discordApi.sendIndividualBastionTurn("Bearer token", result);

        assertTrue(response.isSuccess());
        verify(webhookSender).send(DiscordWebhookSender.Channel.INDIVIDUAL_BASTION_TURN,
                "**Bastion: Ravencrest** (Steve)\nQuest: Fortify the walls");
    }

    @Test
    void sendIndividualBastionTurn_blankDisplayName_omitsParenthetical() throws Exception {
        when(fischlIdentityConnector.getMe(anyString())).thenReturn(new FischlUser());
        MauraBastionDiscordApi.BastionTurnResult result = new MauraBastionDiscordApi.BastionTurnResult()
                .setBastionId("b-1")
                .setBastionName("Ravencrest")
                .setQuest("Fortify the walls");

        discordApi.sendIndividualBastionTurn("Bearer token", result);

        verify(webhookSender).send(DiscordWebhookSender.Channel.INDIVIDUAL_BASTION_TURN,
                "**Bastion: Ravencrest**\nQuest: Fortify the walls");
    }

    @Test
    void sendBastionCreated_sendsToBastionCreationChannel() {
        ApiResponse response = discordApi.sendBastionCreated("Bearer token", "A bastion was born");

        assertTrue(response.isSuccess());
        verify(webhookSender).send(DiscordWebhookSender.Channel.BASTION_CREATION, "A bastion was born");
    }

    @Test
    void sendFacilityBuilt_sendsToFacilityBuiltChannel() {
        discordApi.sendFacilityBuilt("Bearer token", new MauraBastionDiscordApi.DiscordMessageRequest().setMessage("facility built"));
        verify(webhookSender).send(DiscordWebhookSender.Channel.FACILITY_BUILT, "facility built");
    }

    @Test
    void sendFacilityRankUp_sendsToFacilityRankUpChannel() {
        discordApi.sendFacilityRankUp("Bearer token", new MauraBastionDiscordApi.DiscordMessageRequest().setMessage("rank up"));
        verify(webhookSender).send(DiscordWebhookSender.Channel.FACILITY_RANK_UP, "rank up");
    }

    @Test
    void sendBranchUpgradePurchased_sendsToBranchUpgradeChannel() {
        discordApi.sendBranchUpgradePurchased("Bearer token", new MauraBastionDiscordApi.DiscordMessageRequest().setMessage("upgrade"));
        verify(webhookSender).send(DiscordWebhookSender.Channel.BRANCH_UPGRADE, "upgrade");
    }

    @Test
    void sendFacilityRemoved_sendsToFacilityRemovedChannel() {
        discordApi.sendFacilityRemoved("Bearer token", new MauraBastionDiscordApi.DiscordMessageRequest().setMessage("facility removed"));
        verify(webhookSender).send(DiscordWebhookSender.Channel.FACILITY_REMOVED, "facility removed");
    }

    @Test
    void sendHirelingHired_sendsToHirelingHiredChannel() {
        discordApi.sendHirelingHired("Bearer token", new MauraBastionDiscordApi.DiscordMessageRequest().setMessage("hired"));
        verify(webhookSender).send(DiscordWebhookSender.Channel.HIRELING_HIRED, "hired");
    }

    @Test
    void sendDefenderAcquired_sendsToDefenderAcquiredChannel() {
        discordApi.sendDefenderAcquired("Bearer token", new MauraBastionDiscordApi.DiscordMessageRequest().setMessage("defender"));
        verify(webhookSender).send(DiscordWebhookSender.Channel.DEFENDER_ACQUIRED, "defender");
    }

    @Test
    void sendFacilityBuilt_withBastionId_alsoPostsIntoThread() throws Exception {
        when(fischlBastionConnector.read(anyString(), eq("b-1")))
                .thenReturn(new FischlBastion().setId("b-1").setDiscordThreadId("t-1"));

        ApiResponse response = discordApi.sendFacilityBuilt("Bearer token",
                new MauraBastionDiscordApi.DiscordMessageRequest().setMessage("Built!").setBastionId("b-1"));

        assertTrue(response.isSuccess());
        verify(webhookSender).send(DiscordWebhookSender.Channel.FACILITY_BUILT, "Built!");
        verify(webhookSender).sendToThread(DiscordWebhookSender.Channel.BASTION_CREATION, "t-1", "Built!");
    }

    @Test
    void sendFacilityBuilt_withBastionId_alsoEditsInitialMessage() throws Exception {
        when(fischlBastionConnector.read(anyString(), eq("b-1")))
                .thenReturn(new FischlBastion().setId("b-1").setName("Ravencrest")
                        .setDescription("A keep.").setDiscordThreadId("t-1"));

        discordApi.sendFacilityBuilt("Bearer token",
                new MauraBastionDiscordApi.DiscordMessageRequest().setMessage("Built!").setBastionId("b-1"));

        verify(webhookSender).editPost(eq(DiscordWebhookSender.Channel.BASTION_CREATION), eq("t-1"),
                contains("Ravencrest"));
    }

    @Test
    void sendFacilityBuilt_whenEditFails_eventStillSucceeds() throws Exception {
        when(fischlBastionConnector.read(anyString(), eq("b-1")))
                .thenReturn(new FischlBastion().setId("b-1").setName("Ravencrest").setDiscordThreadId("t-1"));
        doThrow(new BackendException(502, "Discord webhook error: HTTP 404"))
                .when(webhookSender).editPost(any(), eq("t-1"), anyString());

        ApiResponse response = discordApi.sendFacilityBuilt("Bearer token",
                new MauraBastionDiscordApi.DiscordMessageRequest().setMessage("Built!").setBastionId("b-1"));

        assertTrue(response.isSuccess());
    }

    @Test
    void sendIndividualBastionTurn_doesNotEditInitialMessage() throws Exception {
        MauraBastionDiscordApi.BastionTurnResult result = new MauraBastionDiscordApi.BastionTurnResult()
                .setBastionId("b-1")
                .setBastionName("Ravencrest")
                .setQuest("Fortify the walls");
        when(fischlBastionConnector.read(anyString(), eq("b-1")))
                .thenReturn(new FischlBastion().setId("b-1").setName("Ravencrest").setDiscordThreadId("t-1"));

        discordApi.sendIndividualBastionTurn("Bearer token", result);

        verify(webhookSender).send(DiscordWebhookSender.Channel.INDIVIDUAL_BASTION_TURN,
                "**Bastion: Ravencrest** (Steve)\nQuest: Fortify the walls");
        verify(webhookSender, never()).editPost(any(), anyString(), anyString());
    }

    @Test
    void sendFacilityBuilt_withoutBastionId_skipsThreadPost() throws Exception {
        discordApi.sendFacilityBuilt("Bearer token",
                new MauraBastionDiscordApi.DiscordMessageRequest().setMessage("Built!"));

        verify(webhookSender).send(DiscordWebhookSender.Channel.FACILITY_BUILT, "Built!");
        verify(fischlBastionConnector, never()).read(anyString(), anyString());
        verify(webhookSender, never()).sendToThread(any(), anyString(), anyString());
    }

    @Test
    void sendFacilityBuilt_whenBastionHasNoThread_channelSendOnly() throws Exception {
        when(fischlBastionConnector.read(anyString(), eq("b-1")))
                .thenReturn(new FischlBastion().setId("b-1").setName("Ravencrest"));

        discordApi.sendFacilityBuilt("Bearer token",
                new MauraBastionDiscordApi.DiscordMessageRequest().setMessage("Built!").setBastionId("b-1"));

        verify(webhookSender, never()).sendToThread(any(), anyString(), anyString());
    }

    @Test
    void sendFacilityBuilt_whenThreadDeleted_channelSendStillSucceeds() throws Exception {
        when(fischlBastionConnector.read(anyString(), eq("b-1")))
                .thenReturn(new FischlBastion().setId("b-1").setDiscordThreadId("t-1"));
        doThrow(new BackendException(502, "Discord webhook error: HTTP 404"))
                .when(webhookSender).sendToThread(any(), eq("t-1"), anyString());

        ApiResponse response = discordApi.sendFacilityBuilt("Bearer token",
                new MauraBastionDiscordApi.DiscordMessageRequest().setMessage("Built!").setBastionId("b-1"));

        assertTrue(response.isSuccess());
        verify(webhookSender).send(DiscordWebhookSender.Channel.FACILITY_BUILT, "Built!");
    }

    @Test
    void sendFacilityBuilt_whenBastionReadFails_channelSendStillSucceeds() throws Exception {
        when(fischlBastionConnector.read(anyString(), eq("b-1")))
                .thenThrow(new FischlBastionException().setStatus(404).setMessage("not found"));

        ApiResponse response = discordApi.sendFacilityBuilt("Bearer token",
                new MauraBastionDiscordApi.DiscordMessageRequest().setMessage("Built!").setBastionId("b-1"));

        assertTrue(response.isSuccess());
        verify(webhookSender).send(DiscordWebhookSender.Channel.FACILITY_BUILT, "Built!");
        verify(webhookSender, never()).sendToThread(any(), anyString(), anyString());
    }

    @Test
    void sendEndpoints_propagateBackendExceptionFromSender() {
        doThrow(new BackendException(502, "Discord webhook error: HTTP 400"))
                .when(webhookSender).send(any(), anyString());

        assertThrows(BackendException.class,
                () -> discordApi.sendFacilityBuilt("Bearer token",
                        new MauraBastionDiscordApi.DiscordMessageRequest().setMessage("facility built")));
    }

    @Test
    void sendMessage_withoutAuthHeader_throws401WithoutCallingSender() {
        BackendException e = assertThrows(BackendException.class,
                () -> discordApi.sendMessage(null, "hello"));

        assertEquals(401, e.getBackendStatus());
        verifyNoInteractions(fischlIdentityConnector);
        verifyNoInteractions(webhookSender);
    }

    @Test
    void sendMessage_whenIdentityRejectsToken_propagates401() throws Exception {
        when(fischlIdentityConnector.getMe(anyString()))
                .thenThrow(new FischlIdentityException().setStatus(401).setMessage("Invalid token"));

        BackendException e = assertThrows(BackendException.class,
                () -> discordApi.sendMessage("Bearer bad", "hello"));

        assertEquals(401, e.getBackendStatus());
        verifyNoInteractions(webhookSender);
    }

    @Test
    void sendMessage_blankAuthHeader_throws401WithoutCallingSender() {
        BackendException e = assertThrows(BackendException.class,
                () -> discordApi.sendMessage("   ", "hello"));

        assertEquals(401, e.getBackendStatus());
        verifyNoInteractions(fischlIdentityConnector);
        verifyNoInteractions(webhookSender);
    }
}