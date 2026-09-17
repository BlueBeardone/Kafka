package za.co.bastion.maurabastion.impl.bastion;

import za.co.bastion.maurabastion.api.bastion.MauraBastionBastionApi;

public final class BastionConstructionMessageBuilder {

    private BastionConstructionMessageBuilder() {
    }

    public static String build(MauraBastionBastionApi.Bastion bastion, MauraBastionBastionApi.Facility facility) {
        StringBuilder sb = new StringBuilder();
        String rank = facility.getRank() == null ? "" : " (Rank " + facility.getRank().name() + ")";
        sb.append("🏗️ **").append(bastion.getName()).append("** has started construction on **")
                .append(facility.getName()).append("**").append(rank).append("!");
        String description = blankToNull(facility.getDescription());
        if (description != null) {
            sb.append("\n\n").append(description);
        }
        StringBuilder details = new StringBuilder();
        if (facility.getCost() != null) {
            details.append("Cost: ").append(facility.getCost()).append("gp");
        }
        if (facility.getConstructionTurns() != null) {
            if (details.length() > 0) details.append(" • ");
            details.append("Build time: ").append(turns(facility.getConstructionTurns()));
        }
        if (facility.getMinimumRequiredHirelings() != null) {
            if (details.length() > 0) details.append(" • ");
            details.append("Required hirelings: ").append(facility.getMinimumRequiredHirelings());
        }
        if (details.length() > 0) {
            sb.append("\n\n").append(details);
        }
        String imgUrl = blankToNull(facility.getImgUrl());
        if (imgUrl != null) {
            sb.append("\n\n").append(imgUrl);
        }
        return sb.toString();
    }

    private static String turns(int turns) {
        return turns == 1 ? "1 turn" : turns + " turns";
    }

    private static String blankToNull(String value) {
        if (value == null) return null;
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
