package za.co.bastion.maurabastion.impl.discord;

import za.co.bastion.fischl.FischlUser;
import za.co.bastion.maurabastion.api.discord.MauraBastionDiscordApi;

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
        }

        return sb.toString();
    }
}
