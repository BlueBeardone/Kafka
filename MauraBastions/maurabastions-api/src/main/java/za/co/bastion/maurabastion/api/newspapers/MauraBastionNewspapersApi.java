package za.co.bastion.maurabastion.api.newspapers;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import za.co.bastion.common.dto.ApiResponse;

public interface MauraBastionNewspapersApi {
    ApiResponse createNewspaperArticle(String authHeader, NewspaperArticle article);
    ApiResponse getAllNewspaperArticles(String authHeader);

    @Getter
    @Setter
    @Accessors(chain = true)
    class NewspaperArticle {
        private String title;
        private String content;
        private String imageUrl;
        private String author;
    }
}