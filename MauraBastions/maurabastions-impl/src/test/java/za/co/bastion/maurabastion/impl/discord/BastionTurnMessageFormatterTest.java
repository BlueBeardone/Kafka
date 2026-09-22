package za.co.bastion.maurabastion.impl.discord;

import org.junit.jupiter.api.Test;
import za.co.bastion.fischl.FischlUser;
import za.co.bastion.maurabastion.api.discord.MauraBastionDiscordApi;

import java.util.List;

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

    @Test
    void format_withRewardSummary_appendsRewardsLine() {
        MauraBastionDiscordApi.BastionTurnResult result = fullResult()
                .setEvent(new MauraBastionDiscordApi.BastionTurnEventResult()
                        .setName("Wandering Merchant")
                        .setDescription("A merchant passes by.")
                        .setRewardSummary("2 × Adamantine (Rank D), 320 GP"));

        String message = formatter.format(result, user("Steve"));

        assertTrue(message.contains("**Rewards:** 2 × Adamantine (Rank D), 320 GP"));
    }

    @Test
    void format_withBlankRewardSummary_omitsRewardsLine() {
        MauraBastionDiscordApi.BastionTurnResult result = fullResult()
                .setEvent(new MauraBastionDiscordApi.BastionTurnEventResult()
                        .setName("Wandering Merchant")
                        .setDescription("A merchant passes by.")
                        .setRewardSummary("   "));

        String message = formatter.format(result, user("Steve"));

        assertFalse(message.contains("Rewards:"));
    }

    @Test
    void format_withFacilityResults_listsEachRoll() {
        MauraBastionDiscordApi.BastionTurnResult result = fullResult()
                .setFacilityResults(List.of(
                        new MauraBastionDiscordApi.BastionTurnFacilityResult()
                                .setName("Kitchen")
                                .setRolledRow("3 | Hearty meal | Everyone is fed"),
                        new MauraBastionDiscordApi.BastionTurnFacilityResult()
                                .setName("Training Yard")
                                .setRolledRow("7 | Drills | Defenders train")));

        String message = formatter.format(result, user("Steve"));

        assertTrue(message.contains("**Facility results:**"));
        assertTrue(message.contains("- Kitchen: 3 | Hearty meal | Everyone is fed"));
        assertTrue(message.contains("- Training Yard: 7 | Drills | Defenders train"));
    }

    @Test
    void format_withoutFacilityResults_omitsFacilityResultsSection() {
        String message = formatter.format(fullResult(), user("Steve"));

        assertFalse(message.contains("Facility results"));
    }

    @Test
    void format_withNullRolledRow_skipsEntry() {
        MauraBastionDiscordApi.BastionTurnResult result = fullResult()
                .setFacilityResults(List.of(
                        new MauraBastionDiscordApi.BastionTurnFacilityResult()
                                .setName("Training Yard")
                                .setRolledRow(null),
                        new MauraBastionDiscordApi.BastionTurnFacilityResult()
                                .setName("Kitchen")
                                .setRolledRow("3 | Hearty meal")));

        String message = formatter.format(result, user("Steve"));

        assertFalse(message.contains("Training Yard"));
        assertTrue(message.contains("- Kitchen: 3 | Hearty meal"));
    }

    @Test
    void format_withEmptyFacilityResults_omitsFacilityResultsSection() {
        MauraBastionDiscordApi.BastionTurnResult result = fullResult()
                .setFacilityResults(List.of());

        String message = formatter.format(result, user("Steve"));

        assertFalse(message.contains("Facility results"));
    }

    @Test
    void format_withDispatch_showsUnitsTotalAndVerdict() {
        MauraBastionDiscordApi.BastionTurnResult result = fullResult()
                .setEvent(new MauraBastionDiscordApi.BastionTurnEventResult()
                        .setName("Wolf Cull")
                        .setDescription("Wolves.")
                        .setDispatch(new MauraBastionDiscordApi.BastionTurnDispatchResult()
                                .setUnits(List.of(
                                        new MauraBastionDiscordApi.BastionTurnDispatchUnitResult()
                                                .setName("Aldric")
                                                .setRolls(List.of(4, 3))
                                                .setSubtotal(7),
                                        new MauraBastionDiscordApi.BastionTurnDispatchUnitResult()
                                                .setName("Marta").setRolls(List.of(2)).setSubtotal(2)))
                                .setBonus(0)
                                .setTotal(9)
                                .setDc(10)
                                .setSuccess(false)));

        String message = formatter.format(result, user("Steve"));

        assertTrue(message.contains("**Dispatch:**"));
        assertTrue(message.contains("• Aldric: 7 (4, 3)"));
        assertTrue(message.contains("• Marta: 2 (2)"));
        assertTrue(message.contains("• Total 9 vs DC 10 — MISSION LOST"));
    }

    @Test
    void format_withSuccessfulDispatch_showsMissionHeld() {
        MauraBastionDiscordApi.BastionTurnResult result = fullResult()
                .setEvent(new MauraBastionDiscordApi.BastionTurnEventResult()
                        .setName("Wolf Cull")
                        .setDescription("Wolves.")
                        .setDispatch(new MauraBastionDiscordApi.BastionTurnDispatchResult()
                                .setUnits(List.of(new MauraBastionDiscordApi.BastionTurnDispatchUnitResult()
                                        .setName("Aldric").setRolls(List.of(6)).setSubtotal(6)))
                                .setBonus(1)
                                .setTotal(7)
                                .setDc(5)
                                .setSuccess(true)));

        String message = formatter.format(result, user("Steve"));

        assertTrue(message.contains("• Total 7 vs DC 5 — MISSION HELD"));
    }

    @Test
    void format_withoutDispatch_omitsDispatchSection() {
        String message = formatter.format(fullResult(), user("Steve"));

        assertFalse(message.contains("Dispatch:"));
    }

    @Test
    void format_withEmptyDispatchUnits_omitsDispatchSection() {
        MauraBastionDiscordApi.BastionTurnResult result = fullResult()
                .setEvent(new MauraBastionDiscordApi.BastionTurnEventResult()
                        .setName("Wolf Cull")
                        .setDescription("Wolves.")
                        .setDispatch(new MauraBastionDiscordApi.BastionTurnDispatchResult()
                                .setUnits(List.of())));

        String message = formatter.format(result, user("Steve"));

        assertFalse(message.contains("Dispatch:"));
    }
}
