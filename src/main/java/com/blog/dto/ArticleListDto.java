package com.blog.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ArticleListDto {
    private Long id;
    private String title;
    private String nickname;
    private LocalDateTime createdTime;

    public ArticleListDto(Long id, String title, String nickname, LocalDateTime createdTime) {
        this.id = id;
        this.title = title;
        this.nickname = nickname;
        this.createdTime = createdTime;
    }
}
