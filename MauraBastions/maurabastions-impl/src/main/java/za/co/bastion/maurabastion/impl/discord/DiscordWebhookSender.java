package za.co.bastion.maurabastion.impl.discord;

import io.quarkus.rest.client.reactive.QuarkusRestClientBuilder;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import za.co.bastion.common.exception.BackendException;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class DiscordWebhookSender {

    public enum Channel {
        GENERAL, INDIVIDUAL_BASTION_TURN, BASTION_CREATION, FACILITY_BUILT,
        FACILITY_RANK_UP, BRANCH_UPGRADE, HIRELING_HIRED, DEFENDER_ACQUIRED
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
    @ConfigProperty(name = "discord.webhook.hireling-hired.url")
    String hirelingHiredUrl;

    @Inject
    @ConfigProperty(name = "discord.webhook.defender-acquired.url")
    String defenderAcquiredUrl;

    private final Map<Channel, DiscordWebhookConnector> clients = new ConcurrentHashMap<>();

    public void send(Channel channel, String content) {
        try {
            connectorFor(channel, urlFor(channel)).send(new DiscordWebhookPayload(content));
        } catch (WebApplicationException e) {
            throw new BackendException(502, "Discord webhook error: HTTP " + e.getResponse().getStatus());
        } catch (BackendException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new BackendException(502, "Discord webhook error: " + e.getMessage());
        }
    }

    private String urlFor(Channel channel) {
        return switch (channel) {
            case GENERAL -> generalUrl;
            case INDIVIDUAL_BASTION_TURN -> individualBastionTurnUrl;
            case BASTION_CREATION -> bastionCreationUrl;
            case FACILITY_BUILT -> facilityBuiltUrl;
            case FACILITY_RANK_UP -> facilityRankUpUrl;
            case BRANCH_UPGRADE -> branchUpgradeUrl;
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
