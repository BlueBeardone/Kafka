package za.co.bastion.tsaritsa.api.hireling;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import za.co.bastion.common.dto.ApiResponse;

public interface TsaritsaHirelingApi {
    ApiResponse createHireling(Hireling hireling);
    ApiResponse getHireling(String id);
    ApiResponse getAllHirelings();
    ApiResponse updateHireling(String id, Hireling hireling);
    ApiResponse deleteHireling(String id);

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