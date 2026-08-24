package za.co.bastion.maurabastion.api.bastion;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import za.co.bastion.common.dto.ApiResponse;
import za.co.bastion.common.dto.FacilityTable;
import za.co.bastion.common.dto.Rank;

public interface MauraBastionBastionApi {
    ApiResponse createBastion(String authHeader, Bastion bastion);
    ApiResponse getBastion(String authHeader, String id);
    ApiResponse getAllBastions(String authHeader);
    ApiResponse updateBastion(String authHeader, String id, Bastion bastion);
    ApiResponse deleteBastion(String authHeader, String id);

    @Getter
    @Setter
    @Accessors(chain = true)
    class Bastion {
        private String id;
        private String userId;
        private String name;
        private String description;
        private String imgUrl;
        private List<Facility> facilities;
        private List<Defender> defenders;
        private List<Hireling> hirelings;
    }

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
