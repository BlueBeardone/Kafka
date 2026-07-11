package za.co.bastion.maurabastion.api.facilities;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import za.co.bastion.common.dto.ApiResponse;
import za.co.bastion.common.dto.Rank;

public interface MauraBastionFacilitiesApi {
    ApiResponse createFacility(Facility facility);
    ApiResponse getFacility(String id);
    ApiResponse getAllFacilities();
    ApiResponse updateFacility(String id, Facility facility);
    ApiResponse deleteFacility(String id);

    @Getter
    @Setter
    @Accessors(chain = true)
    class Facility {
        private String id;
        private String name;
        private Rank rank;
        private Integer hirelingAmount;
        private String description;
        private List<List<String>> table;
    }
}
