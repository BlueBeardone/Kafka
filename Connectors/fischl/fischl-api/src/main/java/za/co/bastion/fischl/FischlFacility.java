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
public class FischlFacility {
    private String id;
    private String name;
    private String rank;
    private Integer hirelingAmount;
    private String description;
    private List<List<String>> table;
}
