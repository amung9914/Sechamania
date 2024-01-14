package com.blog.dto;

import lombok.Data;

@Data
public class AddArticleDto {

    private String title;
    private String content;
    private Long categoryId;

    public AddArticleDto(String title, String content, Long categoryId) {
        this.title = title;
        this.content = content;
        this.categoryId = categoryId;
    }
}
