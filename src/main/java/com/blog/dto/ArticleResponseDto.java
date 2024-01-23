package com.blog.dto;

import com.blog.entity.Article;
import com.blog.entity.Member;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ArticleResponseDto {

    private long articleId;
    private String nickname;
    private String profileImg;
    private String title;
    private String content;
    private String categoryName;
    private LocalDateTime createdTime;
    private String[] hashtags;


    public ArticleResponseDto(Article article) {
        this.articleId = article.getId();
        this.createdTime = article.getCreatedDate();
        this.nickname = article.getMember().getNickname();
        this.profileImg = article.getMember().getProfileImg();
        this.title = article.getTitle();
        this.content = article.getContent();
        this.categoryName = article.getCategory().getName();
        this.hashtags = article.getArticleHashtags().stream().map(articleHashtag -> articleHashtag.getHashtag().getName()).toArray(String[]::new);
    }

}
