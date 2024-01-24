package com.blog.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ArticleListDto {
    private Long id;
    private String title;
    private String nickname;
    private String categoryName;
    private LocalDateTime createdTime;
    private String profileImg;

    public ArticleListDto(Long id, String title, String nickname, String categoryName, LocalDateTime createdTime, String profileImg) {
        this.id = id;
        this.title = title;
        this.nickname = nickname;
        this.categoryName = categoryName;
        this.createdTime = createdTime;
        this.profileImg = profileImg;
    }
}
