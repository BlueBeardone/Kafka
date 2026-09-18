package za.co.bastion.maurabastion.impl.discord;

import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;

/**
 * MicroProfile REST client for a Discord webhook. The base URI is the full
 * webhook URL, supplied when the client is built programmatically.
 */
public interface DiscordWebhookConnector {

    @POST
    void send(DiscordWebhookPayload payload);

    @POST
    DiscordWebhookMessage sendAndWait(@QueryParam("wait") boolean wait, DiscordWebhookPayload payload);

    @POST
    void sendToThread(@QueryParam("thread_id") String threadId, DiscordWebhookPayload payload);

    @PATCH
    @Path("messages/{messageId}")
    void edit(@PathParam("messageId") String messageId, @QueryParam("thread_id") String threadId,
              DiscordWebhookPayload payload);
}
