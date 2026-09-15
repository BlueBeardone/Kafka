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
public class FischlBastionPage {
    private List<FischlBastion> bastions;
    private int page;
    private int limit;
    private long total;
    private boolean hasMore;
}
