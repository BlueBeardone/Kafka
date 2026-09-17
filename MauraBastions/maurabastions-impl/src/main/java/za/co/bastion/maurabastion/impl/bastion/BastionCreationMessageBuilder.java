package za.co.bastion.maurabastion.impl.bastion;

import za.co.bastion.maurabastion.api.bastion.MauraBastionBastionApi;

import java.util.List;
import java.util.function.Function;

public final class BastionCreationMessageBuilder {

    private BastionCreationMessageBuilder() {
    }

    public static String build(MauraBastionBastionApi.Bastion bastion) {
        StringBuilder sb = new StringBuilder();
        sb.append("🏰 **").append(bastion.getName()).append("** has been founded!");
        String description = blankToNull(bastion.getDescription());
        if (description != null) {
            sb.append("\n\n").append(description);
        }
        appendSection(sb, "Facilities", bastion.getFacilities(),
                f -> f.getName() + detail(f.getRank() == null ? null : f.getRank().name()));
        appendCount(sb, "Defenders", bastion.getDefenders());
        appendCount(sb, "Hirelings", bastion.getHirelings());
        String imgUrl = blankToNull(bastion.getImgUrl());
        if (imgUrl != null) {
            appendBlock(sb, imgUrl);
        }
        return sb.toString();
    }

    private static <T> void appendSection(StringBuilder sb, String title, List<T> items,
                                           Function<T, String> describe) {
        if (items == null || items.isEmpty()) {
            return;
        }
        appendBlock(sb, "**" + title + ":**");
        for (var item : items) {
            sb.append("\n• ").append(describe.apply(item));
        }
    }

    private static void appendCount(StringBuilder sb, String title, java.util.Collection<?> items) {
        sb.append("\n\n**").append(title).append(":** ").append(items == null ? 0 : items.size());
    }

    private static void appendBlock(StringBuilder sb, String block) {
        while (sb.length() > 0 && sb.charAt(sb.length() - 1) == '\n') {
            sb.setLength(sb.length() - 1);
        }
        if (sb.length() > 0) {
            sb.append("\n\n");
        }
        sb.append(block);
    }

    private static String detail(String value) {
        String trimmed = value == null ? "" : value.trim();
        return trimmed.isEmpty() ? "" : " — " + trimmed;
    }

    private static String blankToNull(String value) {
        if (value == null) return null;
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
