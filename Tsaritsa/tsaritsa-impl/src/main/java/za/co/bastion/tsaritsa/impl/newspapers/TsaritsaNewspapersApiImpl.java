package za.co.bastion.tsaritsa.impl.newspapers;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import za.co.bastion.common.dto.ApiResponse;
import za.co.bastion.common.exception.BackendException;
import za.co.bastion.common.exception.InvalidRequestException;
import za.co.bastion.fischl.FischlArticle;
import za.co.bastion.fischl.FischlNewspaperException;
import za.co.bastion.fischl.newspapers.FischlNewspapersConnector;
import za.co.bastion.tsaritsa.api.newspapers.TsaritsaNewspapersApi;

import java.util.List;

@ApplicationScoped
public class TsaritsaNewspapersApiImpl implements TsaritsaNewspapersApi {

    @Inject
    @RestClient
    FischlNewspapersConnector fischlNewspapersConnector;

    @Override
    public ApiResponse createNewspaperArticle(String authHeader, NewspaperArticle article) {
        validateNewspaperArticle(article);
        try {
            FischlArticle result = fischlNewspapersConnector.create(authHeader, toFischlArticle(article));
            return ApiResponse.success("Newspaper article created", toNewspaperArticle(result));
        } catch (FischlNewspaperException e) {
            throw new BackendException(e.getStatus(), "Newspaper backend error: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse getNewspaperArticle(String id) {
        if (id == null || id.isBlank()) {
            throw new InvalidRequestException("id is required");
        }
        try {
            FischlArticle result = fischlNewspapersConnector.read(id);
            return ApiResponse.success("Newspaper article found", toNewspaperArticle(result));
        } catch (FischlNewspaperException e) {
            throw new BackendException(e.getStatus(), "Newspaper backend error: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse getAllNewspaperArticles(String authHeader) {
        try {
            List<FischlArticle> results = fischlNewspapersConnector.readAll(authHeader);
            List<NewspaperArticle> articles = results.stream().map(this::toNewspaperArticle).toList();
            return ApiResponse.success("Newspaper articles found", articles);
        } catch (FischlNewspaperException e) {
            throw new BackendException(e.getStatus(), "Newspaper backend error: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse updateNewspaperArticle(String id, NewspaperArticle article) {
        if (id == null || id.isBlank()) {
            throw new InvalidRequestException("id is required");
        }
        validateNewspaperArticle(article);
        try {
            FischlArticle result = fischlNewspapersConnector.update(id, toFischlArticle(article));
            return ApiResponse.success("Newspaper article updated", toNewspaperArticle(result));
        } catch (FischlNewspaperException e) {
            throw new BackendException(e.getStatus(), "Newspaper backend error: " + e.getMessage());
        }
    }

    @Override
    public ApiResponse deleteNewspaperArticle(String id) {
        if (id == null || id.isBlank()) {
            throw new InvalidRequestException("id is required");
        }
        try {
            fischlNewspapersConnector.delete(id);
            return ApiResponse.success("Newspaper article deleted", null);
        } catch (FischlNewspaperException e) {
            throw new BackendException(e.getStatus(), "Newspaper backend error: " + e.getMessage());
        }
    }

    private void validateNewspaperArticle(NewspaperArticle article) {
        if (article.getTitle() == null || article.getTitle().isBlank()) {
            throw new InvalidRequestException("title is required");
        }
        if (article.getContent() == null || article.getContent().isBlank()) {
            throw new InvalidRequestException("content is required");
        }
    }

    private FischlArticle toFischlArticle(NewspaperArticle article) {
        return new FischlArticle()
                .setId(article.getId())
                .setTitle(article.getTitle())
                .setContent(article.getContent())
                .setImageUrl(article.getImageUrl())
                .setAuthor(article.getAuthor());
    }

    private NewspaperArticle toNewspaperArticle(FischlArticle fischl) {
        return new NewspaperArticle()
                .setId(fischl.getId())
                .setTitle(fischl.getTitle())
                .setContent(fischl.getContent())
                .setImageUrl(fischl.getImageUrl())
                .setAuthor(fischl.getAuthor());
    }
}