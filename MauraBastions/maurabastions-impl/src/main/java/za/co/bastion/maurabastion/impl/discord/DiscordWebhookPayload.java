package za.co.bastion.maurabastion.impl.discord;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * Discord webhook request body: {"content": "<message>"} for plain sends,
 * {"embeds": [...]} for announcement sends. Success is HTTP 204.
 */
@Getter
@Setter
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class DiscordWebhookPayload {
    private String content;
    private List<Embed> embeds;
    @JsonProperty("thread_name")
    private String threadName;

    public DiscordWebhookPayload() {
    }

    public DiscordWebhookPayload(String content) {
        this.content = content;
    }

    public DiscordWebhookPayload embed(Embed embed) {
        if (embeds == null) {
            embeds = new ArrayList<>();
        }
        embeds.add(embed);
        return this;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Embed {
        private String description;
        private Integer color;
        private Image image;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    public static class Image {
        private String url;
    }
}
