package za.co.bastion.maurabastion.api.facilities;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import za.co.bastion.common.dto.ApiResponse;
import za.co.bastion.common.dto.FacilityTable;
import za.co.bastion.common.dto.Rank;

public interface MauraBastionFacilitiesApi {
    ApiResponse createFacility(String authHeader, Facility facility);
    ApiResponse getFacility(String id);
    ApiResponse getAllFacilities();
    ApiResponse updateFacility(String authHeader, String id, Facility facility);
    ApiResponse deleteFacility(String authHeader, String id);

    @Getter
    @Setter
    @Accessors(chain = true)
    class Facility {
        private String id;
        private String name;
        private Rank rank;
        private Integer minimumRequiredHirelings;
        private Integer constructionTurns;
        private Integer cost;
        private String description;
        private String imgUrl;
        private FacilityTable table;
        private Integer constructedTurns;
        private String bastionId;
    }
}