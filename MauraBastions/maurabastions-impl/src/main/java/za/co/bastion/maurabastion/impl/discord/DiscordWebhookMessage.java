package za.co.bastion.maurabastion.impl.discord;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Discord webhook response body when called with wait=true: the created
 * message JSON. For forum posts Discord returns no thread object; the new
 * thread's id equals the message's channel_id.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record DiscordWebhookMessage(String id,
                                    DiscordThread thread,
                                    @JsonProperty("channel_id") String channelId) {

    public record DiscordThread(String id) {
    }
}
