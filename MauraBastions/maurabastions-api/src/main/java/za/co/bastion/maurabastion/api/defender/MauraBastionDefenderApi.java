package za.co.bastion.maurabastion.api.defender;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import za.co.bastion.common.dto.ApiResponse;

public interface MauraBastionDefenderApi {
    ApiResponse createDefender(Defender defender);
    ApiResponse getDefender(String id);
    ApiResponse getAllDefenders();
    ApiResponse updateDefender(String id, Defender defender);
    ApiResponse deleteDefender(String id);

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
