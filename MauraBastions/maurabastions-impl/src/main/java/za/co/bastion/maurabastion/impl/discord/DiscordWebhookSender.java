package za.co.bastion.maurabastion.impl.discord;

import io.quarkus.rest.client.reactive.QuarkusRestClientBuilder;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import za.co.bastion.common.exception.BackendException;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class DiscordWebhookSender {

    public enum Channel {
        GENERAL, INDIVIDUAL_BASTION_TURN, BASTION_CREATION, FACILITY_BUILT,
        FACILITY_RANK_UP, BRANCH_UPGRADE, FACILITY_REMOVED, HIRELING_HIRED, DEFENDER_ACQUIRED
    }

    @Inject
    @ConfigProperty(name = "discord.webhook.general.url")
    String generalUrl;

    @Inject
    @ConfigProperty(name = "discord.webhook.individual-bastion-turn.url")
    String individualBastionTurnUrl;

    @Inject
    @ConfigProperty(name = "discord.webhook.bastion-creation.url")
    String bastionCreationUrl;

    @Inject
    @ConfigProperty(name = "discord.webhook.facility-built.url")
    String facilityBuiltUrl;

    @Inject
    @ConfigProperty(name = "discord.webhook.facility-rank-up.url")
    String facilityRankUpUrl;

    @Inject
    @ConfigProperty(name = "discord.webhook.branch-upgrade.url")
    String branchUpgradeUrl;

    @Inject
    @ConfigProperty(name = "discord.webhook.facility-removed.url")
    String facilityRemovedUrl;

    @Inject
    @ConfigProperty(name = "discord.webhook.hireling-hired.url")
    String hirelingHiredUrl;

    @Inject
    @ConfigProperty(name = "discord.webhook.defender-acquired.url")
    String defenderAcquiredUrl;

    private final Map<Channel, DiscordWebhookConnector> clients = new ConcurrentHashMap<>();

    private static final Map<Channel, Integer> EMBED_COLORS = Map.of(
            Channel.BASTION_CREATION, 0xE8B23A,
            Channel.FACILITY_BUILT, 0x3B7DE8,
            Channel.FACILITY_RANK_UP, 0x3BB273,
            Channel.BRANCH_UPGRADE, 0x9B59B6,
            Channel.FACILITY_REMOVED, 0xE85B5B,
            Channel.HIRELING_HIRED, 0xE8A23A,
            Channel.DEFENDER_ACQUIRED, 0x7A8B99,
            Channel.INDIVIDUAL_BASTION_TURN, 0x3BB2B2);

    public void send(Channel channel, String content) {
        try {
            DiscordWebhookPayload payload = channel == Channel.GENERAL
                    ? new DiscordWebhookPayload(content)
                    : embedPayload(channel, content);
            connectorFor(channel, urlFor(channel)).send(payload);
        } catch (WebApplicationException e) {
            throw new BackendException(502, "Discord webhook error: HTTP " + e.getResponse().getStatus());
        } catch (BackendException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new BackendException(502, "Discord webhook error: " + e.getMessage());
        }
    }

    public String createPost(Channel channel, String threadName, String content) {
        try {
            DiscordWebhookPayload payload = embedPayload(channel, content).setThreadName(threadName);
            DiscordWebhookMessage message = connectorFor(channel, urlFor(channel)).sendAndWait(true, payload);
            String threadId = threadIdOf(message);
            if (threadId == null) {
                throw new BackendException(502, "Discord webhook error: response missing thread id");
            }
            return threadId;
        } catch (BackendException e) {
            throw e;
        } catch (WebApplicationException e) {
            throw new BackendException(502, "Discord webhook error: HTTP " + e.getResponse().getStatus());
        } catch (RuntimeException e) {
            throw new BackendException(502, "Discord webhook error: " + e.getMessage());
        }
    }

    private String threadIdOf(DiscordWebhookMessage message) {
        if (message == null) {
            return null;
        }
        if (message.thread() != null && message.thread().id() != null && !message.thread().id().isBlank()) {
            return message.thread().id();
        }
        String channelId = message.channelId();
        return channelId == null || channelId.isBlank() ? null : channelId;
    }

    public void sendToThread(Channel channel, String threadId, String content) {
        try {
            DiscordWebhookPayload payload = embedPayload(channel, content);
            connectorFor(channel, urlFor(channel)).sendToThread(threadId, payload);
        } catch (RuntimeException ignored) {
            // best effort: posting into a bastion's thread must never fail the caller
        }
    }

    public void editPost(Channel channel, String messageId, String content) {
        try {
            DiscordWebhookPayload payload = embedPayload(channel, content);
            connectorFor(channel, urlFor(channel)).edit(messageId, messageId, payload);
        } catch (BackendException e) {
            throw e;
        } catch (WebApplicationException e) {
            throw new BackendException(502, "Discord webhook error: HTTP " + e.getResponse().getStatus());
        } catch (RuntimeException e) {
            throw new BackendException(502, "Discord webhook error: " + e.getMessage());
        }
    }

    DiscordWebhookPayload embedPayload(Channel channel, String message) {
        String description = message;
        String imageUrl = null;
        String[] lines = message.split("\n", -1);
        if (lines.length > 0 && lines[lines.length - 1].matches("https?://\\S+")) {
            imageUrl = lines[lines.length - 1];
            description = String.join("\n", Arrays.copyOf(lines, lines.length - 1)).stripTrailing();
        }
        DiscordWebhookPayload.Embed embed = new DiscordWebhookPayload.Embed()
                .setDescription(description)
                .setColor(EMBED_COLORS.get(channel));
        if (imageUrl != null) {
            embed.setImage(new DiscordWebhookPayload.Image().setUrl(imageUrl));
        }
        return new DiscordWebhookPayload().embed(embed);
    }

    private String urlFor(Channel channel) {
        return switch (channel) {
            case GENERAL -> generalUrl;
            case INDIVIDUAL_BASTION_TURN -> individualBastionTurnUrl;
            case BASTION_CREATION -> bastionCreationUrl;
            case FACILITY_BUILT -> facilityBuiltUrl;
            case FACILITY_RANK_UP -> facilityRankUpUrl;
            case BRANCH_UPGRADE -> branchUpgradeUrl;
            case FACILITY_REMOVED -> facilityRemovedUrl;
            case HIRELING_HIRED -> hirelingHiredUrl;
            case DEFENDER_ACQUIRED -> defenderAcquiredUrl;
        };
    }

    DiscordWebhookConnector connectorFor(Channel channel, String url) {
        if (url == null || url.isBlank()) {
            throw new BackendException(500, "Discord webhook not configured for " + channel);
        }
        return clients.computeIfAbsent(channel, c ->
                QuarkusRestClientBuilder.newBuilder()
                        .baseUri(URI.create(url))
                        .build(DiscordWebhookConnector.class));
    }
}
