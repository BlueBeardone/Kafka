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
public class FischlNewspaper {
    private String id;
    private String newspaperName;
    private String date;
    private String edition;
    private FischlArticle leadArticle;
    private List<FischlArticle> otherArticles;
}
