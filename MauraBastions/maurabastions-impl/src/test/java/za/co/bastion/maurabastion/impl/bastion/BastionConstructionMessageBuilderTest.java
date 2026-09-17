package za.co.bastion.maurabastion.impl.bastion;

import org.junit.jupiter.api.Test;
import za.co.bastion.common.dto.Rank;
import za.co.bastion.maurabastion.api.bastion.MauraBastionBastionApi;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BastionConstructionMessageBuilderTest {

    @Test
    void build_includesFacilityDetails() {
        MauraBastionBastionApi.Bastion bastion = new MauraBastionBastionApi.Bastion().setName("Ravencrest");
        MauraBastionBastionApi.Facility facility = new MauraBastionBastionApi.Facility()
                .setName("Kitchen").setRank(Rank.d)
                .setDescription("Cooks food.")
                .setCost(600).setConstructionTurns(2).setMinimumRequiredHirelings(1);

        assertEquals("""
                🏗️ **Ravencrest** has started construction on **Kitchen** (Rank d)!

                Cooks food.

                Cost: 600gp • Build time: 2 turns • Required hirelings: 1""",
                BastionConstructionMessageBuilder.build(bastion, facility));
    }

    @Test
    void build_omitsBlankDescription_andSingularTurn() {
        MauraBastionBastionApi.Bastion bastion = new MauraBastionBastionApi.Bastion().setName("Ravencrest");
        MauraBastionBastionApi.Facility facility = new MauraBastionBastionApi.Facility()
                .setName("Well Room").setRank(Rank.d).setDescription("  ").setConstructionTurns(1);

        assertEquals("🏗️ **Ravencrest** has started construction on **Well Room** (Rank d)!\n\nBuild time: 1 turn",
                BastionConstructionMessageBuilder.build(bastion, facility));
    }

    @Test
    void build_appendsImgUrlAsLastLine_soItBecomesTheEmbedImage() {
        MauraBastionBastionApi.Bastion bastion = new MauraBastionBastionApi.Bastion().setName("Ravencrest");
        MauraBastionBastionApi.Facility facility = new MauraBastionBastionApi.Facility()
                .setName("Kitchen").setRank(Rank.d).setImgUrl("https://example.test/kitchen.png");

        assertEquals("🏗️ **Ravencrest** has started construction on **Kitchen** (Rank d)!\n\nhttps://example.test/kitchen.png",
                BastionConstructionMessageBuilder.build(bastion, facility));
    }
}
