package za.co.bastion.maurabastion.api.discord;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import za.co.bastion.common.dto.ApiResponse;

import java.util.List;

public interface MauraBastionDiscordApi {
    ApiResponse sendMessage(String authHeader, String message);
    ApiResponse sendIndividualBastionTurn(String authHeader, BastionTurnResult result);
    ApiResponse sendBastionCreated(String authHeader, String message);
    ApiResponse sendFacilityBuilt(String authHeader, DiscordMessageRequest request);
    ApiResponse sendFacilityRankUp(String authHeader, DiscordMessageRequest request);
    ApiResponse sendBranchUpgradePurchased(String authHeader, DiscordMessageRequest request);
    ApiResponse sendFacilityRemoved(String authHeader, DiscordMessageRequest request);
    ApiResponse sendHirelingHired(String authHeader, DiscordMessageRequest request);
    ApiResponse sendDefenderAcquired(String authHeader, DiscordMessageRequest request);

    @Getter
    @Setter
    @Accessors(chain = true)
    class DiscordMessageRequest {
        private String message;
        private String bastionId;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    class BastionTurnResult {
        private String bastionId;
        private String bastionName;
        private String quest;
        private BastionTurnAdvancedFacility advancedFacility;
        private BastionTurnEventResult event;
        private List<BastionTurnFacilityResult> facilityResults;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    class BastionTurnAdvancedFacility {
        private String name;
        private String rankTitle;
        private Integer constructedTurns;
        private Integer constructionTurns;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    class BastionTurnEventResult {
        private String name;
        private String description;
        private String rolledRow;
        private String rewardSummary;
        private BastionTurnDispatchResult dispatch;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    class BastionTurnDispatchResult {
        private List<BastionTurnDispatchUnitResult> units;
        private Integer bonus;
        private Integer total;
        private Integer dc;
        private Boolean success;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    class BastionTurnDispatchUnitResult {
        private String name;
        private List<Integer> rolls;
        private Integer subtotal;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    class BastionTurnFacilityResult {
        private String name;
        private String rolledRow;
    }
}