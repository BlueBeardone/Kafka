package za.co.bastion.maurabastion.impl.discord;

/**
 * Discord webhook response body when called with wait=true: the created
 * message JSON. For forum posts the thread object carries the new thread id.
 */
public record DiscordWebhookMessage(String id, DiscordThread thread) {

    public record DiscordThread(String id) {
    }
}
