package za.co.bastion.maurabastion.impl.bastion;

import org.junit.jupiter.api.Test;
import za.co.bastion.common.dto.Rank;
import za.co.bastion.maurabastion.api.bastion.MauraBastionBastionApi;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BastionCreationMessageBuilderTest {

    @Test
    void build_includesAllSections() {
        MauraBastionBastionApi.Bastion bastion = new MauraBastionBastionApi.Bastion()
                .setName("Ravencrest").setDescription("A keep on the hill.")
                .setFacilities(List.of(new MauraBastionBastionApi.Facility()
                        .setName("Kitchen").setRank(Rank.d)))
                .setDefenders(List.of(new MauraBastionBastionApi.Defender()
                        .setName("Ser Aldric").setType("Guard")))
                .setHirelings(List.of(new MauraBastionBastionApi.Hireling()
                        .setName("Marta").setRole("Cook")));

        String message = BastionCreationMessageBuilder.build(bastion);

        assertEquals("""
                🏰 **Ravencrest** has been founded!

                A keep on the hill.

                **Facilities:**
                • Kitchen — d

                **Defenders:**
                • Ser Aldric — Guard

                **Hirelings:**
                • Marta — Cook""", message);
    }

    @Test
    void build_omitsEmptyListsAndNullDescription() {
        MauraBastionBastionApi.Bastion bastion = new MauraBastionBastionApi.Bastion()
                .setName("Ravencrest").setDescription("  ");

        assertEquals("🏰 **Ravencrest** has been founded!", BastionCreationMessageBuilder.build(bastion));
    }

    @Test
    void build_appendsImgUrlAsLastLine_soSenderEmbedsItAsImage() {
        MauraBastionBastionApi.Bastion bastion = new MauraBastionBastionApi.Bastion()
                .setName("Ravencrest").setDescription("A keep.")
                .setImgUrl("https://example.test/ravencrest.png");

        String message = BastionCreationMessageBuilder.build(bastion);

        assertEquals("🏰 **Ravencrest** has been founded!\n\nA keep.\n\nhttps://example.test/ravencrest.png", message);
    }
}
