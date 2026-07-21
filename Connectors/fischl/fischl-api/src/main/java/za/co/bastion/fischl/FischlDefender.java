package za.co.bastion.fischl;

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
public class FischlDefender {
    private String id;
    private String name;
    private String description;
    private String imgUrl;
    private String type;
    private String bastionId;
    private String acquisitionStory;
}
