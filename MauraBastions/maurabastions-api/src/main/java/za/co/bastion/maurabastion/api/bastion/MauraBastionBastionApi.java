package za.co.bastion.maurabastion.api.bastion;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import za.co.bastion.common.dto.ApiResponse;
import za.co.bastion.common.dto.FacilityTable;
import za.co.bastion.common.dto.Rank;

public interface MauraBastionBastionApi {
    ApiResponse createBastion(Bastion bastion);
    ApiResponse getBastion(String id);
    ApiResponse getAllBastions();
    ApiResponse updateBastion(String id, Bastion bastion);
    ApiResponse deleteBastion(String id);

    @Getter
    @Setter
    @Accessors(chain = true)
    class Bastion {
        private String id;
        private String name;
        private String description;
        private String imgUrl;
        private List<Facility> facilities;
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
