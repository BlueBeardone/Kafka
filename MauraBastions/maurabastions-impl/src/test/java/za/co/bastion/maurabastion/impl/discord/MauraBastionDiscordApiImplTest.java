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
import za.co.bastion.fischl.FischlIdentityException;
import za.co.bastion.fischl.FischlUser;
import za.co.bastion.fischl.identity.FischlIdentityConnector;
import za.co.bastion.maurabastion.api.discord.MauraBastionDiscordApi;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MauraBastionDiscordApiImplTest {

    @Mock
    FischlIdentityConnector fischlIdentityConnector;

    @Mock
    DiscordWebhookSender webhookSender;

    MauraBastionDiscordApi discordApi;

    @BeforeEach
    void setUp() throws Exception {
        discordApi = new MauraBastionDiscordApiImpl();
        setField(discordApi, "webhookSender", webhookSender);
        setField(discordApi, "fischlIdentityConnector", fischlIdentityConnector);
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
        discordApi.sendFacilityBuilt("Bearer token", "facility built");
        verify(webhookSender).send(DiscordWebhookSender.Channel.FACILITY_BUILT, "facility built");
    }

    @Test
    void sendFacilityRankUp_sendsToFacilityRankUpChannel() {
        discordApi.sendFacilityRankUp("Bearer token", "rank up");
        verify(webhookSender).send(DiscordWebhookSender.Channel.FACILITY_RANK_UP, "rank up");
    }

    @Test
    void sendBranchUpgradePurchased_sendsToBranchUpgradeChannel() {
        discordApi.sendBranchUpgradePurchased("Bearer token", "upgrade");
        verify(webhookSender).send(DiscordWebhookSender.Channel.BRANCH_UPGRADE, "upgrade");
    }

    @Test
    void sendFacilityRemoved_sendsToFacilityRemovedChannel() {
        discordApi.sendFacilityRemoved("Bearer token", "facility removed");
        verify(webhookSender).send(DiscordWebhookSender.Channel.FACILITY_REMOVED, "facility removed");
    }

    @Test
    void sendHirelingHired_sendsToHirelingHiredChannel() {
        discordApi.sendHirelingHired("Bearer token", "hired");
        verify(webhookSender).send(DiscordWebhookSender.Channel.HIRELING_HIRED, "hired");
    }

    @Test
    void sendDefenderAcquired_sendsToDefenderAcquiredChannel() {
        discordApi.sendDefenderAcquired("Bearer token", "defender");
        verify(webhookSender).send(DiscordWebhookSender.Channel.DEFENDER_ACQUIRED, "defender");
    }

    @Test
    void sendEndpoints_propagateBackendExceptionFromSender() {
        doThrow(new BackendException(502, "Discord webhook error: HTTP 400"))
                .when(webhookSender).send(any(), anyString());

        assertThrows(BackendException.class,
                () -> discordApi.sendFacilityBuilt("Bearer token", "facility built"));
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