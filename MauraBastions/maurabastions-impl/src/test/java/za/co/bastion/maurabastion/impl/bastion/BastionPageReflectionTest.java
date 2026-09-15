package za.co.bastion.maurabastion.impl.bastion;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.runtime.annotations.RegisterForReflection;
import java.util.List;
import org.junit.jupiter.api.Test;
import za.co.bastion.maurabastion.api.bastion.MauraBastionBastionApi;

class BastionPageReflectionTest {

    @Test
    void bastionPage_isRegisteredForReflection_forNativeSerialization() {
        assertTrue(
                MauraBastionBastionApi.BastionPage.class
                        .isAnnotationPresent(RegisterForReflection.class),
                "BastionPage is response-only (never a resource method parameter), so Quarkus "
                        + "does not auto-register it. Without @RegisterForReflection the native "
                        + "image cannot serialize it and /bastions/browse returns an empty 500.");
    }

    @Test
    void bastionPage_serializesWithDataViaJackson() throws Exception {
        MauraBastionBastionApi.Bastion bastion =
                new MauraBastionBastionApi.Bastion().setId("b1").setName("Keep");
        MauraBastionBastionApi.BastionPage page =
                new MauraBastionBastionApi.BastionPage()
                        .setBastions(List.of(bastion))
                        .setPage(1)
                        .setLimit(20)
                        .setTotal(1)
                        .setHasMore(false);

        String json = new ObjectMapper().writeValueAsString(page);

        assertTrue(json.contains("\"bastions\""));
        assertTrue(json.contains("\"page\":1"));
        assertTrue(json.contains("\"hasMore\":false"));
    }
}
