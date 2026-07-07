package com.example.bastionmiddleware.dto;

import jakarta.validation.constraints.NotBlank;

public class NewspaperArticleDto {
    @NotBlank(message = "title is required")
    private String title;

    @NotBlank(message = "content is required")
    private String content;

    private String imageUrl;
    private String author;

    public NewspaperArticleDto() {
    }

    public NewspaperArticleDto(String title, String content, String imageUrl, String author) {
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
