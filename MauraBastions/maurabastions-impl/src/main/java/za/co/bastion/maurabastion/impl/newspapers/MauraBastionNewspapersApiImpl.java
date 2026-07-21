package za.co.bastion.maurabastion.impl.newspapers;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import za.co.bastion.common.dto.ApiResponse;
import za.co.bastion.common.exception.InvalidRequestException;
import za.co.bastion.fischl.FischlArticle;
import za.co.bastion.fischl.FischlNewspaperException;
import za.co.bastion.fischl.newspapers.FischlNewspapersConnector;
import za.co.bastion.maurabastion.api.newspapers.MauraBastionNewspapersApi;

import java.util.List;

@ApplicationScoped
public class MauraBastionNewspapersApiImpl implements MauraBastionNewspapersApi {

    @Inject
    @RestClient
    FischlNewspapersConnector fischlNewspapersConnector;

    @Override
    public ApiResponse createNewspaperArticle(MauraBastionNewspapersApi.NewspaperArticle article) {
        validateNewspaperArticle(article);
        try {
            FischlArticle result = fischlNewspapersConnector.create(toFischlArticle(article));
            return ApiResponse.success("Newspaper article created", toNewspaperArticle(result));
        } catch (FischlNewspaperException e) {
            throw new RuntimeException("Newspaper backend error: " + e.getMessage(), e);
        }
    }

    @Override
    public ApiResponse getAllNewspaperArticles() {
        try {
            List<FischlArticle> results = fischlNewspapersConnector.readAll();
            List<MauraBastionNewspapersApi.NewspaperArticle> articles = results.stream().map(this::toNewspaperArticle).toList();
            return ApiResponse.success("Newspaper articles found", articles);
        } catch (FischlNewspaperException e) {
            throw new RuntimeException("Newspaper backend error: " + e.getMessage(), e);
        }
    }

    private void validateNewspaperArticle(MauraBastionNewspapersApi.NewspaperArticle article) {
        if (article.getTitle() == null || article.getTitle().isBlank()) {
            throw new InvalidRequestException("title is required");
        }
        if (article.getContent() == null || article.getContent().isBlank()) {
            throw new InvalidRequestException("content is required");
        }
    }

    private FischlArticle toFischlArticle(MauraBastionNewspapersApi.NewspaperArticle article) {
        return new FischlArticle()
                .setTitle(article.getTitle())
                .setContent(article.getContent())
                .setImageUrl(article.getImageUrl())
                .setAuthor(article.getAuthor());
    }

    private MauraBastionNewspapersApi.NewspaperArticle toNewspaperArticle(FischlArticle fischl) {
        return new MauraBastionNewspapersApi.NewspaperArticle()
                .setTitle(fischl.getTitle())
                .setContent(fischl.getContent())
                .setImageUrl(fischl.getImageUrl())
                .setAuthor(fischl.getAuthor());
    }
}