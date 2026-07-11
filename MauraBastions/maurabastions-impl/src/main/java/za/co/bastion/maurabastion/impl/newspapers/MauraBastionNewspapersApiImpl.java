package za.co.bastion.maurabastion.impl.newspapers;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import za.co.bastion.common.dto.ApiResponse;
import za.co.bastion.common.exception.InvalidRequestException;
import za.co.bastion.fischl.*;
import za.co.bastion.fischl.newspapers.FischlNewspapersConnector;
import za.co.bastion.maurabastion.api.newspapers.MauraBastionNewspapersApi;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class MauraBastionNewspapersApiImpl implements MauraBastionNewspapersApi {

    @Inject
    @RestClient
    FischlNewspapersConnector fischlNewspapersConnector;

    @Override
    public ApiResponse createNewspaper(Newspaper newspaper) {
        validateNewspaper(newspaper);
        try {
            FischlNewspaper result = fischlNewspapersConnector.create(toFischlNewspaper(newspaper));
            return ApiResponse.success("Newspaper created", toNewspaper(result));
        } catch (FischlNewspaperException e) {
            throw new RuntimeException("Newspaper backend error: " + e.getMessage(), e);
        }
    }

    @Override
    public ApiResponse getNewspaper(String id) {
        if (id == null || id.isBlank()) {
            throw new InvalidRequestException("id is required");
        }
        try {
            FischlNewspaper result = fischlNewspapersConnector.read(id);
            return ApiResponse.success("Newspaper found", toNewspaper(result));
        } catch (FischlNewspaperException e) {
            throw new RuntimeException("Newspaper backend error: " + e.getMessage(), e);
        }
    }

    @Override
    public ApiResponse getAllNewspapers() {
        try {
            List<FischlNewspaper> results = fischlNewspapersConnector.readAll();
            List<Newspaper> newspapers = results.stream().map(this::toNewspaper).toList();
            return ApiResponse.success("Newspapers found", newspapers);
        } catch (FischlNewspaperException e) {
            throw new RuntimeException("Newspaper backend error: " + e.getMessage(), e);
        }
    }

    @Override
    public ApiResponse updateNewspaper(String id, Newspaper newspaper) {
        if (id == null || id.isBlank()) {
            throw new InvalidRequestException("id is required");
        }
        validateNewspaper(newspaper);
        try {
            FischlNewspaper result = fischlNewspapersConnector.update(id, toFischlNewspaper(newspaper));
            return ApiResponse.success("Newspaper updated", toNewspaper(result));
        } catch (FischlNewspaperException e) {
            throw new RuntimeException("Newspaper backend error: " + e.getMessage(), e);
        }
    }

    @Override
    public ApiResponse deleteNewspaper(String id) {
        if (id == null || id.isBlank()) {
            throw new InvalidRequestException("id is required");
        }
        try {
            fischlNewspapersConnector.delete(id);
            return ApiResponse.success("Newspaper deleted", null);
        } catch (FischlNewspaperException e) {
            throw new RuntimeException("Newspaper backend error: " + e.getMessage(), e);
        }
    }

    private void validateNewspaper(Newspaper newspaper) {
        if (newspaper.getNewspaperName() == null || newspaper.getNewspaperName().isBlank()) {
            throw new InvalidRequestException("newspaperName is required");
        }
        if (newspaper.getDate() == null || newspaper.getDate().isBlank()) {
            throw new InvalidRequestException("date is required");
        }
        if (newspaper.getEdition() == null || newspaper.getEdition().isBlank()) {
            throw new InvalidRequestException("edition is required");
        }
    }

    private FischlNewspaper toFischlNewspaper(Newspaper newspaper) {
        FischlArticle lead = null;
        if (newspaper.getLeadArticle() != null) {
            lead = new FischlArticle()
                    .setTitle(newspaper.getLeadArticle().getTitle())
                    .setContent(newspaper.getLeadArticle().getContent())
                    .setImageUrl(newspaper.getLeadArticle().getImageUrl())
                    .setAuthor(newspaper.getLeadArticle().getAuthor());
        }
        List<FischlArticle> others = new ArrayList<>();
        if (newspaper.getOtherArticles() != null) {
            others = newspaper.getOtherArticles().stream().map(a ->
                    new FischlArticle()
                            .setTitle(a.getTitle())
                            .setContent(a.getContent())
                            .setImageUrl(a.getImageUrl())
                            .setAuthor(a.getAuthor())
            ).toList();
        }
        return new FischlNewspaper()
                .setId(newspaper.getId())
                .setNewspaperName(newspaper.getNewspaperName())
                .setDate(newspaper.getDate())
                .setEdition(newspaper.getEdition())
                .setLeadArticle(lead)
                .setOtherArticles(others);
    }

    private Newspaper toNewspaper(FischlNewspaper fischl) {
        Article lead = null;
        if (fischl.getLeadArticle() != null) {
            lead = new Article()
                    .setTitle(fischl.getLeadArticle().getTitle())
                    .setContent(fischl.getLeadArticle().getContent())
                    .setImageUrl(fischl.getLeadArticle().getImageUrl())
                    .setAuthor(fischl.getLeadArticle().getAuthor());
        }
        List<Article> others = new ArrayList<>();
        if (fischl.getOtherArticles() != null) {
            others = fischl.getOtherArticles().stream().map(a ->
                    new Article()
                            .setTitle(a.getTitle())
                            .setContent(a.getContent())
                            .setImageUrl(a.getImageUrl())
                            .setAuthor(a.getAuthor())
            ).toList();
        }
        return new Newspaper()
                .setId(fischl.getId())
                .setNewspaperName(fischl.getNewspaperName())
                .setDate(fischl.getDate())
                .setEdition(fischl.getEdition())
                .setLeadArticle(lead)
                .setOtherArticles(others);
    }
}
