package za.co.bastion.maurabastion.impl.discord;

import org.junit.jupiter.api.Test;
import za.co.bastion.maurabastion.api.discord.MauraBastionDiscordApi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class BastionTurnMessageFormatterTest {

    private final BastionTurnMessageFormatter formatter = new BastionTurnMessageFormatter();

    private MauraBastionDiscordApi.BastionTurnResult fullResult() {
        return new MauraBastionDiscordApi.BastionTurnResult()
                .setBastionId("b-1")
                .setBastionName("Ravencrest")
                .setQuest("Fortify the walls")
                .setAdvancedFacility(new MauraBastionDiscordApi.BastionTurnAdvancedFacility()
                        .setName("Smithy")
                        .setRankTitle("Master Smithy")
                        .setConstructedTurns(3)
                        .setConstructionTurns(5))
                .setEvent(new MauraBastionDiscordApi.BastionTurnEventResult()
                        .setName("Wandering Merchant")
                        .setDescription("A merchant passes by.")
                        .setRolledRow("12 - Rare goods"));
    }

    @Test
    void format_withAllSections_includesEverything() {
        String message = formatter.format(fullResult());

        assertEquals("""
                **Bastion: Ravencrest** (b-1)
                Quest: Fortify the walls

                **Facility advanced:** Smithy → Master Smithy (turn 3/5)

                **Event:** Wandering Merchant — A merchant passes by.
                Rolled: 12 - Rare goods""", message);
    }

    @Test
    void format_withoutFacility_omitsFacilitySection() {
        MauraBastionDiscordApi.BastionTurnResult result = fullResult().setAdvancedFacility(null);

        String message = formatter.format(result);

        assertFalse(message.contains("Facility advanced"));
        assertFalse(message.contains("Smithy"));
    }

    @Test
    void format_withoutEvent_omitsEventSection() {
        MauraBastionDiscordApi.BastionTurnResult result = fullResult().setEvent(null);

        String message = formatter.format(result);

        assertFalse(message.contains("Event:"));
        assertFalse(message.contains("Rolled:"));
    }

    @Test
    void format_withoutEventRolledRow_omitsRolledLine() {
        MauraBastionDiscordApi.BastionTurnResult result = fullResult()
                .setEvent(new MauraBastionDiscordApi.BastionTurnEventResult()
                        .setName("Wandering Merchant")
                        .setDescription("A merchant passes by.")
                        .setRolledRow(null));

        String message = formatter.format(result);

        assertFalse(message.contains("Rolled:"));
    }

    @Test
    void format_withOnlyCoreFields_returnsCoreMessage() {
        MauraBastionDiscordApi.BastionTurnResult result = new MauraBastionDiscordApi.BastionTurnResult()
                .setBastionId("b-2")
                .setBastionName("Duskhaven")
                .setQuest("Train hirelings");

        assertEquals("""
                **Bastion: Duskhaven** (b-2)
                Quest: Train hirelings""", formatter.format(result));
    }
}
