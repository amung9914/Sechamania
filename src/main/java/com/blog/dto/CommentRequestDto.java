package com.blog.dto;

import com.blog.entity.Comment;
import lombok.Data;

@Data
public class CommentRequestDto {
    private String content;
    private Long articleId;
    private Long parentId;

    public CommentRequestDto(String content, Long articleId, Long parentId) {
        this.content = content;
        this.articleId = articleId;
        this.parentId = parentId;
    }

}
