package za.co.bastion.tsaritsa.api.defender;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import za.co.bastion.common.dto.ApiResponse;

public interface TsaritsaDefenderApi {
    ApiResponse createDefender(String authHeader, Defender defender);
    ApiResponse getDefender(String authHeader, String id);
    ApiResponse getAllDefenders(String authHeader);
    ApiResponse updateDefender(String authHeader, String id, Defender defender);
    ApiResponse deleteDefender(String authHeader, String id);

    @Getter
    @Setter
    @Accessors(chain = true)
    class Defender {
        private String id;
        private String name;
        private String description;
        private String imgUrl;
        private String type;
        private String bastionId;
        private String acquisitionStory;
    }
}