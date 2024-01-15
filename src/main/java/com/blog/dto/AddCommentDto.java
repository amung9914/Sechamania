package com.blog.dto;

import lombok.Data;

@Data
public class AddCommentDto {

    private String Content;
    private Long ArticleId;

    public AddCommentDto(String content, Long articleId) {
        Content = content;
        ArticleId = articleId;
    }
}
