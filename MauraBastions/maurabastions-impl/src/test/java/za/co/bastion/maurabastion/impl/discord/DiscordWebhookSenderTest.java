package za.co.bastion.maurabastion.impl.discord;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import za.co.bastion.common.exception.BackendException;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;

class DiscordWebhookSenderTest {

    private DiscordWebhookSender sender;

    @BeforeEach
    void setUp() throws Exception {
        sender = new DiscordWebhookSender();
        for (DiscordWebhookSender.Channel channel : DiscordWebhookSender.Channel.values()) {
            setField(sender, fieldName(channel), "http://localhost:19999/webhook-" + channel.name().toLowerCase());
        }
    }

    private static void setField(Object target, String name, String value) throws Exception {
        Field field = DiscordWebhookSender.class.getDeclaredField(name);
        field.setAccessible(true);
        field.set(target, value);
    }

    private static String fieldName(DiscordWebhookSender.Channel channel) {
        return switch (channel) {
            case GENERAL -> "generalUrl";
            case INDIVIDUAL_BASTION_TURN -> "individualBastionTurnUrl";
            case BASTION_CREATION -> "bastionCreationUrl";
            case FACILITY_BUILT -> "facilityBuiltUrl";
            case FACILITY_RANK_UP -> "facilityRankUpUrl";
            case BRANCH_UPGRADE -> "branchUpgradeUrl";
            case HIRELING_HIRED -> "hirelingHiredUrl";
            case DEFENDER_ACQUIRED -> "defenderAcquiredUrl";
        };
    }

    @Test
    void send_whenUrlNotConfigured_throwsBackendException500() throws Exception {
        setField(sender, "bastionCreationUrl", "  ");

        BackendException e = assertThrows(BackendException.class,
                () -> sender.send(DiscordWebhookSender.Channel.BASTION_CREATION, "hello"));

        assertEquals(500, e.getBackendStatus());
        assertTrue(e.getBackendMessage().contains("BASTION_CREATION"));
    }

    @Test
    void send_whenDiscordReturnsErrorStatus_throwsBackendException502() {
        sender = new DiscordWebhookSender() {
            @Override
            DiscordWebhookConnector connectorFor(DiscordWebhookSender.Channel channel, String url) {
                DiscordWebhookConnector mock = mock(DiscordWebhookConnector.class);
                doThrow(new WebApplicationException(Response.status(400).build()))
                        .when(mock).send(any());
                return mock;
            }
        };

        BackendException e = assertThrows(BackendException.class,
                () -> sender.send(DiscordWebhookSender.Channel.GENERAL, "hello"));

        assertEquals(502, e.getBackendStatus());
        assertTrue(e.getBackendMessage().contains("400"));
    }

    @Test
    void send_whenNetworkFails_throwsBackendException502() {
        sender = new DiscordWebhookSender() {
            @Override
            DiscordWebhookConnector connectorFor(DiscordWebhookSender.Channel channel, String url) {
                DiscordWebhookConnector mock = mock(DiscordWebhookConnector.class);
                doThrow(new RuntimeException("Connection refused")).when(mock).send(any());
                return mock;
            }
        };

        BackendException e = assertThrows(BackendException.class,
                () -> sender.send(DiscordWebhookSender.Channel.FACILITY_BUILT, "hello"));

        assertEquals(502, e.getBackendStatus());
        assertTrue(e.getBackendMessage().contains("Connection refused"));
    }
}
