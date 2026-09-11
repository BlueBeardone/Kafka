package za.co.bastion.maurabastion.impl.discord;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Discord webhook request body: {"content": "<message>"}. Success is HTTP 204.
 */
@Getter
@Setter
@Accessors(chain = true)
public class DiscordWebhookPayload {
    private String content;

    public DiscordWebhookPayload() {
    }

    public DiscordWebhookPayload(String content) {
        this.content = content;
    }
}