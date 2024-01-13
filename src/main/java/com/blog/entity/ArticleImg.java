package com.blog.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class ArticleImg extends BaseEntity{
    @Id @GeneratedValue
    @Column(name = "article_img_id")
    private Long id;

    @Column(nullable = false)
    private String path;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

    @Builder
    public ArticleImg(String path, Article article) {
        this.path = path;
        this.article = article;
    }

    // 아티클 연결
    public void connectArticle(Article article){
        this.article = article;
    }
}
