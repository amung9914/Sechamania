package com.blog.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleHashtag extends BaseTimeEntity{
    @Id @GeneratedValue
    @Column(name = "article_hashtag_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hashtag_id")
    private Hashtag hashtag;

    @Builder
    public ArticleHashtag(Article article, Hashtag hashtag) {
        this.article = article;
        this.hashtag = hashtag;
    }
}
