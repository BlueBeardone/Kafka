package za.co.bastion.fischl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import za.co.bastion.common.dto.FacilityTable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class FischlFacility {
    private String id;
    private String name;
    private String rank;
    private Integer minimumRequiredHirelings;
    private Integer constructionTurns;
    private Integer cost;
    private String description;
    private String imgUrl;
    private FacilityTable table;
    private Integer constructedTurns;
    private String bastionId;
}