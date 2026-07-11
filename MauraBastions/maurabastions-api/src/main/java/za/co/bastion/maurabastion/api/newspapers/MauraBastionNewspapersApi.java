package za.co.bastion.maurabastion.api.newspapers;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import za.co.bastion.common.dto.ApiResponse;

public interface MauraBastionNewspapersApi {
    ApiResponse createNewspaper(Newspaper newspaper);
    ApiResponse getNewspaper(String id);
    ApiResponse getAllNewspapers();
    ApiResponse updateNewspaper(String id, Newspaper newspaper);
    ApiResponse deleteNewspaper(String id);

    @Getter
    @Setter
    @Accessors(chain = true)
    class Newspaper {
        private String id;
        private String newspaperName;
        private String date;
        private String edition;
        private Article leadArticle;
        private List<Article> otherArticles;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    class Article {
        private String title;
        private String content;
        private String imageUrl;
        private String author;
    }
}
