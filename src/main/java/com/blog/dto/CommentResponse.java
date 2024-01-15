package com.blog.dto;

import com.blog.entity.Article;
import com.blog.entity.Member;
import lombok.Data;

@Data
public class CommentResponse {
    private Long id;
    private String content;
    private String nickname;

    public CommentResponse(Long id, String content, String nickname) {
        this.id = id;
        this.content = content;
        this.nickname = nickname;
    }
}
