package za.co.bastion.maurabastion.api.hireling;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import za.co.bastion.common.dto.ApiResponse;

public interface MauraBastionHirelingApi {
    ApiResponse createHireling(String authHeader, Hireling hireling);
    ApiResponse getHireling(String authHeader, String id);
    ApiResponse getAllHirelings(String authHeader);
    ApiResponse updateHireling(String authHeader, String id, Hireling hireling);
    ApiResponse deleteHireling(String authHeader, String id);

    @Getter
    @Setter
    @Accessors(chain = true)
    class Hireling {
        private String id;
        private String name;
        private String role;
        private String description;
        private String imgUrl;
        private String bastionId;
        private String facilityId;
        private String acquisitionStory;
    }
}