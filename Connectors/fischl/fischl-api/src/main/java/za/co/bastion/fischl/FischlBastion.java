package za.co.bastion.fischl;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class FischlBastion {
    private String id;
    private String name;
    private String description;
    private String imgUrl;
    private List<FischlFacility> facilities;
    private List<FischlDefender> defenders;
    private List<FischlHireling> hirelings;
}
