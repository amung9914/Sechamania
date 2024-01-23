package com.blog.dto;

import com.blog.entity.Comment;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CommentResponseDto {
    private Long commentId;
    private String content;
    private Long articleId;
    private Long parentId;
    private List<CommentResponseDto> children = new ArrayList<>();

    public CommentResponseDto(Comment comment) {
        this.commentId = comment.getId();
        this.content = comment.getContent();
        this.articleId = comment.getArticle().getId();
        if(comment.getParent()!=null){
            this.parentId = comment.getParent().getId();
        }
    }
}
