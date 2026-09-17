package za.co.bastion.maurabastion.impl.discord;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import za.co.bastion.common.exception.BackendException;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
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
            case FACILITY_REMOVED -> "facilityRemovedUrl";
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

    @Test
    void send_eventChannel_sendsEmbedWithChannelColor() {
        DiscordWebhookConnector connector = mock(DiscordWebhookConnector.class);
        ArgumentCaptor<DiscordWebhookPayload> captor = ArgumentCaptor.forClass(DiscordWebhookPayload.class);
        sender = new DiscordWebhookSender() {
            @Override
            DiscordWebhookConnector connectorFor(DiscordWebhookSender.Channel channel, String url) {
                return connector;
            }
        };

        sender.send(DiscordWebhookSender.Channel.BASTION_CREATION, "🏰 **Maura** has been founded!");

        verify(connector).send(captor.capture());
        DiscordWebhookPayload payload = captor.getValue();
        assertNull(payload.getContent());
        assertEquals(1, payload.getEmbeds().size());
        DiscordWebhookPayload.Embed embed = payload.getEmbeds().get(0);
        assertEquals(0xE8B23A, embed.getColor());
        assertEquals("🏰 **Maura** has been founded!", embed.getDescription());
        assertNull(embed.getImage());
    }

    @Test
    void send_trailingUrl_becomesEmbedImage() {
        DiscordWebhookConnector connector = mock(DiscordWebhookConnector.class);
        ArgumentCaptor<DiscordWebhookPayload> captor = ArgumentCaptor.forClass(DiscordWebhookPayload.class);
        sender = new DiscordWebhookSender() {
            @Override
            DiscordWebhookConnector connectorFor(DiscordWebhookSender.Channel channel, String url) {
                return connector;
            }
        };

        sender.send(DiscordWebhookSender.Channel.FACILITY_BUILT, "Built!\n\nhttps://example.com/img.png");

        verify(connector).send(captor.capture());
        DiscordWebhookPayload.Embed embed = captor.getValue().getEmbeds().get(0);
        assertEquals(0x3B7DE8, embed.getColor());
        assertEquals("Built!", embed.getDescription());
        assertEquals("https://example.com/img.png", embed.getImage().getUrl());
    }

    @Test
    void send_urlInsideMessage_staysInDescription() {
        DiscordWebhookConnector connector = mock(DiscordWebhookConnector.class);
        ArgumentCaptor<DiscordWebhookPayload> captor = ArgumentCaptor.forClass(DiscordWebhookPayload.class);
        sender = new DiscordWebhookSender() {
            @Override
            DiscordWebhookConnector connectorFor(DiscordWebhookSender.Channel channel, String url) {
                return connector;
            }
        };

        sender.send(DiscordWebhookSender.Channel.DEFENDER_ACQUIRED, "see https://example.com/img.png now");

        verify(connector).send(captor.capture());
        DiscordWebhookPayload.Embed embed = captor.getValue().getEmbeds().get(0);
        assertEquals(0x7A8B99, embed.getColor());
        assertEquals("see https://example.com/img.png now", embed.getDescription());
        assertNull(embed.getImage());
    }

    @Test
    void send_facilityRemoved_sendsRedEmbed() {
        DiscordWebhookConnector connector = mock(DiscordWebhookConnector.class);
        ArgumentCaptor<DiscordWebhookPayload> captor = ArgumentCaptor.forClass(DiscordWebhookPayload.class);
        sender = new DiscordWebhookSender() {
            @Override
            DiscordWebhookConnector connectorFor(DiscordWebhookSender.Channel channel, String url) {
                return connector;
            }
        };

        sender.send(DiscordWebhookSender.Channel.FACILITY_REMOVED, "🏚️ torn down");

        verify(connector).send(captor.capture());
        DiscordWebhookPayload.Embed embed = captor.getValue().getEmbeds().get(0);
        assertEquals(0xE85B5B, embed.getColor());
        assertEquals("🏚️ torn down", embed.getDescription());
    }

    @Test
    void send_generalChannel_stillSendsPlainContent() {
        DiscordWebhookConnector connector = mock(DiscordWebhookConnector.class);
        ArgumentCaptor<DiscordWebhookPayload> captor = ArgumentCaptor.forClass(DiscordWebhookPayload.class);
        sender = new DiscordWebhookSender() {
            @Override
            DiscordWebhookConnector connectorFor(DiscordWebhookSender.Channel channel, String url) {
                return connector;
            }
        };

        sender.send(DiscordWebhookSender.Channel.GENERAL, "hello");

        verify(connector).send(captor.capture());
        DiscordWebhookPayload payload = captor.getValue();
        assertEquals("hello", payload.getContent());
        assertNull(payload.getEmbeds());
    }

    @Test
    void createPost_returnsThreadId_andSendsThreadName() {
        DiscordWebhookConnector connector = mock(DiscordWebhookConnector.class);
        when(connector.sendAndWait(eq(true), any()))
                .thenReturn(new DiscordWebhookMessage("m-1", new DiscordWebhookMessage.DiscordThread("t-1")));
        ArgumentCaptor<DiscordWebhookPayload> captor = ArgumentCaptor.forClass(DiscordWebhookPayload.class);
        sender = new DiscordWebhookSender() {
            @Override
            DiscordWebhookConnector connectorFor(DiscordWebhookSender.Channel channel, String url) {
                return connector;
            }
        };

        String threadId = sender.createPost(
                DiscordWebhookSender.Channel.BASTION_CREATION, "Ravencrest", "🏰 founded");

        assertEquals("t-1", threadId);
        verify(connector).sendAndWait(eq(true), captor.capture());
        assertEquals("Ravencrest", captor.getValue().getThreadName());
        assertEquals(1, captor.getValue().getEmbeds().size());
        assertEquals(0xE8B23A, captor.getValue().getEmbeds().get(0).getColor());
    }

    @Test
    void createPost_whenDiscordErrors_throwsBackendException502() {
        sender = new DiscordWebhookSender() {
            @Override
            DiscordWebhookConnector connectorFor(DiscordWebhookSender.Channel channel, String url) {
                DiscordWebhookConnector mock = mock(DiscordWebhookConnector.class);
                when(mock.sendAndWait(anyBoolean(), any()))
                        .thenThrow(new WebApplicationException(Response.status(500).build()));
                return mock;
            }
        };

        BackendException e = assertThrows(BackendException.class, () -> sender.createPost(
                DiscordWebhookSender.Channel.BASTION_CREATION, "Ravencrest", "🏰 founded"));

        assertEquals(502, e.getBackendStatus());
    }

    @Test
    void createPost_whenResponseMissingThread_throwsBackendException502() {
        DiscordWebhookConnector connector = mock(DiscordWebhookConnector.class);
        when(connector.sendAndWait(anyBoolean(), any()))
                .thenReturn(new DiscordWebhookMessage("m-1", null));
        sender = new DiscordWebhookSender() {
            @Override
            DiscordWebhookConnector connectorFor(DiscordWebhookSender.Channel channel, String url) {
                return connector;
            }
        };

        BackendException e = assertThrows(BackendException.class, () -> sender.createPost(
                DiscordWebhookSender.Channel.BASTION_CREATION, "Ravencrest", "🏰 founded"));

        assertEquals(502, e.getBackendStatus());
        assertTrue(e.getBackendMessage().contains("thread"));
    }

    @Test
    void sendToThread_passesThreadId() {
        DiscordWebhookConnector connector = mock(DiscordWebhookConnector.class);
        sender = new DiscordWebhookSender() {
            @Override
            DiscordWebhookConnector connectorFor(DiscordWebhookSender.Channel channel, String url) {
                return connector;
            }
        };

        sender.sendToThread(DiscordWebhookSender.Channel.BASTION_CREATION, "t-9", "hello thread");

        verify(connector).sendToThread(eq("t-9"), any());
    }

    @Test
    void sendToThread_whenDiscordErrors_doesNotThrow() {
        sender = new DiscordWebhookSender() {
            @Override
            DiscordWebhookConnector connectorFor(DiscordWebhookSender.Channel channel, String url) {
                DiscordWebhookConnector mock = mock(DiscordWebhookConnector.class);
                doThrow(new WebApplicationException(Response.status(404).build()))
                        .when(mock).sendToThread(any(), any());
                return mock;
            }
        };

        assertDoesNotThrow(() -> sender.sendToThread(
                DiscordWebhookSender.Channel.BASTION_CREATION, "t-1", "hello"));
    }

    @Test
    void sendToThread_whenUrlNotConfigured_doesNotThrow() throws Exception {
        setField(sender, "bastionCreationUrl", "  ");

        assertDoesNotThrow(() -> sender.sendToThread(
                DiscordWebhookSender.Channel.BASTION_CREATION, "t-1", "hello"));
    }
}
