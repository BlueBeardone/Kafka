package com.example.bastionmiddleware.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class NewspaperDataDto {
    @NotBlank(message = "newspaperName is required")
    private String newspaperName;

    @NotBlank(message = "date is required")
    private String date;

    @NotBlank(message = "edition is required")
    private String edition;

    @Valid
    @NotNull(message = "leadArticle is required")
    private NewspaperArticleDto leadArticle;

    @Valid
    @NotNull(message = "otherArticles are required")
    private List<NewspaperArticleDto> otherArticles;

    public NewspaperDataDto() {
    }

    public NewspaperDataDto(String newspaperName, String date, String edition, NewspaperArticleDto leadArticle, List<NewspaperArticleDto> otherArticles) {
        this.newspaperName = newspaperName;
        this.date = date;
        this.edition = edition;
        this.leadArticle = leadArticle;
        this.otherArticles = otherArticles;
    }

    public String getNewspaperName() {
        return newspaperName;
    }

    public void setNewspaperName(String newspaperName) {
        this.newspaperName = newspaperName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public NewspaperArticleDto getLeadArticle() {
        return leadArticle;
    }

    public void setLeadArticle(NewspaperArticleDto leadArticle) {
        this.leadArticle = leadArticle;
    }

    public List<NewspaperArticleDto> getOtherArticles() {
        return otherArticles;
    }

    public void setOtherArticles(List<NewspaperArticleDto> otherArticles) {
        this.otherArticles = otherArticles;
    }
}
