package za.co.bastion.maurabastion.impl.discord;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class DiscordWebhookPayloadTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void contentOnly_serializesAsPlainContent() throws Exception {
        JsonNode json = mapper.readTree(mapper.writeValueAsString(new DiscordWebhookPayload("hello")));

        assertEquals("hello", json.get("content").asText());
        assertFalse(json.has("embeds"));
    }

    @Test
    void embedWithImage_serializesEmbedFields() throws Exception {
        DiscordWebhookPayload payload = new DiscordWebhookPayload()
                .embed(new DiscordWebhookPayload.Embed()
                        .setDescription("hi")
                        .setColor(15240010)
                        .setImage(new DiscordWebhookPayload.Image().setUrl("https://x/y.png")));

        JsonNode json = mapper.readTree(mapper.writeValueAsString(payload));
        JsonNode embed = json.get("embeds").get(0);

        assertEquals("hi", embed.get("description").asText());
        assertEquals(15240010, embed.get("color").asInt());
        assertEquals("https://x/y.png", embed.get("image").get("url").asText());
        assertFalse(json.has("content"));
    }

    @Test
    void embedWithoutImage_omitsImageField() throws Exception {
        DiscordWebhookPayload payload = new DiscordWebhookPayload()
                .embed(new DiscordWebhookPayload.Embed().setDescription("hi").setColor(1));

        JsonNode embed = mapper.readTree(mapper.writeValueAsString(payload)).get("embeds").get(0);

        assertFalse(embed.has("image"));
    }

    @Test
    void threadName_serializesAsThreadName_andOmittedWhenNull() throws Exception {
        JsonNode withName = mapper.readTree(mapper.writeValueAsString(
                new DiscordWebhookPayload().setThreadName("Ravencrest")));

        assertEquals("Ravencrest", withName.get("thread_name").asText());

        JsonNode withoutName = mapper.readTree(mapper.writeValueAsString(new DiscordWebhookPayload("hi")));

        assertFalse(withoutName.has("thread_name"));
    }
}
