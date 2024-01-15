package com.blog.dto;

import lombok.Data;

@Data
public class AddArticleDto {

    private String title;
    private String content;
    private long categoryId;

    public AddArticleDto(String title, String content, long categoryId) {
        this.title = title;
        this.content = content;
        this.categoryId = categoryId;
    }
}
