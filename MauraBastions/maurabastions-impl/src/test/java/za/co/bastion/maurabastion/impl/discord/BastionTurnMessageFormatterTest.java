package za.co.bastion.maurabastion.impl.discord;

import org.junit.jupiter.api.Test;
import za.co.bastion.fischl.FischlUser;
import za.co.bastion.maurabastion.api.discord.MauraBastionDiscordApi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    private FischlUser user(String displayName) {
        return new FischlUser().setDisplayName(displayName);
    }

    @Test
    void format_withDisplayName_showsNameNotBastionId() {
        String message = formatter.format(fullResult(), user("Steve"));

        assertEquals("""
                **Bastion: Ravencrest** (Steve)
                Quest: Fortify the walls

                **Facility advanced:** Smithy → Master Smithy (turn 3/5)

                **Event:** Wandering Merchant — A merchant passes by.
                Rolled: 12 - Rare goods""", message);
    }

    @Test
    void format_withBlankDisplayName_omitsParenthetical() {
        MauraBastionDiscordApi.BastionTurnResult result = fullResult().setEvent(null);

        String message = formatter.format(result, user("   "));

        assertFalse(message.contains("b-1"));
        assertTrue(message.startsWith("**Bastion: Ravencrest**\n"));
    }

    @Test
    void format_withNullUser_omitsParenthetical() {
        String message = formatter.format(fullResult(), null);

        assertFalse(message.contains("b-1"));
        assertTrue(message.startsWith("**Bastion: Ravencrest**\n"));
    }

    @Test
    void format_withoutFacility_omitsFacilitySection() {
        MauraBastionDiscordApi.BastionTurnResult result = fullResult().setAdvancedFacility(null);

        String message = formatter.format(result, user("Steve"));

        assertFalse(message.contains("Facility advanced"));
        assertFalse(message.contains("Smithy"));
    }

    @Test
    void format_withoutEvent_omitsEventSection() {
        MauraBastionDiscordApi.BastionTurnResult result = fullResult().setEvent(null);

        String message = formatter.format(result, user("Steve"));

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

        String message = formatter.format(result, user("Steve"));

        assertFalse(message.contains("Rolled:"));
    }

    @Test
    void format_withOnlyCoreFields_returnsCoreMessage() {
        MauraBastionDiscordApi.BastionTurnResult result = new MauraBastionDiscordApi.BastionTurnResult()
                .setBastionId("b-2")
                .setBastionName("Duskhaven")
                .setQuest("Train hirelings");

        assertEquals("""
                **Bastion: Duskhaven** (Steve)
                Quest: Train hirelings""", formatter.format(result, user("Steve")));
    }
}
