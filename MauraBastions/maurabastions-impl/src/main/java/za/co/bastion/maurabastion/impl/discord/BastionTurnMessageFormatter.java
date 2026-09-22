package za.co.bastion.maurabastion.impl.discord;

import za.co.bastion.fischl.FischlUser;
import za.co.bastion.maurabastion.api.discord.MauraBastionDiscordApi;

import java.util.List;

public class BastionTurnMessageFormatter {

    public String format(MauraBastionDiscordApi.BastionTurnResult result, FischlUser user) {
        StringBuilder sb = new StringBuilder();
        sb.append("**Bastion: ").append(result.getBastionName()).append("**");
        String displayName = user == null ? null : user.getDisplayName();
        if (displayName != null && !displayName.isBlank()) {
            sb.append(" (").append(displayName).append(")");
        }
        sb.append("\n");
        sb.append("Quest: ").append(result.getQuest());

        MauraBastionDiscordApi.BastionTurnAdvancedFacility facility = result.getAdvancedFacility();
        if (facility != null) {
            sb.append("\n\n**Facility advanced:** ").append(facility.getName())
                    .append(" → ").append(facility.getRankTitle())
                    .append(" (turn ").append(facility.getConstructedTurns())
                    .append("/").append(facility.getConstructionTurns()).append(")");
        }

        MauraBastionDiscordApi.BastionTurnEventResult event = result.getEvent();
        if (event != null) {
            sb.append("\n\n**Event:** ").append(event.getName())
                    .append(" — ").append(event.getDescription());
            if (event.getRolledRow() != null && !event.getRolledRow().isBlank()) {
                sb.append("\nRolled: ").append(event.getRolledRow());
            }
            if (event.getRewardSummary() != null && !event.getRewardSummary().isBlank()) {
                sb.append("\n**Rewards:** ").append(event.getRewardSummary());
            }
            appendDispatch(sb, event.getDispatch());
        }

        List<MauraBastionDiscordApi.BastionTurnFacilityResult> facilityResults = result.getFacilityResults();
        if (facilityResults != null) {
            List<String> lines = facilityResults.stream()
                    .filter(r -> r != null && r.getName() != null
                            && r.getRolledRow() != null && !r.getRolledRow().isBlank())
                    .map(r -> "- " + r.getName() + ": " + r.getRolledRow())
                    .toList();
            if (!lines.isEmpty()) {
                sb.append("\n\n**Facility results:**");
                for (String line : lines) {
                    sb.append("\n").append(line);
                }
            }
        }

        return sb.toString();
    }

    private void appendDispatch(StringBuilder sb, MauraBastionDiscordApi.BastionTurnDispatchResult dispatch) {
        if (dispatch == null || dispatch.getUnits() == null || dispatch.getUnits().isEmpty()) {
            return;
        }
        sb.append("\n\n**Dispatch:**");
        for (MauraBastionDiscordApi.BastionTurnDispatchUnitResult unit : dispatch.getUnits()) {
            if (unit == null || unit.getName() == null) {
                continue;
            }
            String rolls = unit.getRolls() == null ? ""
                    : unit.getRolls().stream().map(String::valueOf).collect(java.util.stream.Collectors.joining(", "));
            sb.append("\n• ").append(unit.getName()).append(": ").append(unit.getSubtotal());
            if (!rolls.isEmpty()) {
                sb.append(" (").append(rolls).append(")");
            }
        }
        String verdict = Boolean.TRUE.equals(dispatch.getSuccess()) ? "MISSION HELD" : "MISSION LOST";
        sb.append("\n• Total ").append(dispatch.getTotal()).append(" vs DC ").append(dispatch.getDc())
                .append(" — ").append(verdict);
    }
}
