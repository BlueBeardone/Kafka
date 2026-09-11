package za.co.bastion.maurabastion.impl.discord;

import jakarta.ws.rs.POST;

/**
 * MicroProfile REST client for a Discord webhook. The base URI is the full
 * webhook URL, supplied when the client is built programmatically.
 */
public interface DiscordWebhookConnector {

    @POST
    void send(DiscordWebhookPayload payload);
}