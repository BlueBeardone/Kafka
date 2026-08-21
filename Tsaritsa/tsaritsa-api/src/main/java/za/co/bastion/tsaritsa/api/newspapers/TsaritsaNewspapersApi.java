package za.co.bastion.tsaritsa.api.newspapers;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import za.co.bastion.common.dto.ApiResponse;

public interface TsaritsaNewspapersApi {
    ApiResponse createNewspaperArticle(NewspaperArticle article);
    ApiResponse getNewspaperArticle(String id);
    ApiResponse getAllNewspaperArticles();
    ApiResponse updateNewspaperArticle(String id, NewspaperArticle article);
    ApiResponse deleteNewspaperArticle(String id);

    @Getter
    @Setter
    @Accessors(chain = true)
    class NewspaperArticle {
        private String id;
        private String title;
        private String content;
        private String imageUrl;
        private String author;
    }
}