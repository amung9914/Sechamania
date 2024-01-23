package com.blog.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article extends BaseEntity{
    @Id @GeneratedValue
    @Column(name = "article_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    @Lob
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
    @OneToMany(mappedBy = "article",cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ArticleHashtag> articleHashtags = new ArrayList<>();
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Comment> comments;

    @Builder
    public Article(String title, String content, Member member, Category category,List<ArticleHashtag> articleHashtags) {
        this.title = title;
        this.content = content;
        this.member = member;
        this.category = category;
        this.articleHashtags = articleHashtags;
    }

    // 생성 메서드
    public static Article createArticleWtihHashtags(String title, String content, Member member, Category category, ArticleHashtag... hashtags) {
        Article article = new Article();
        article.title = title;
        article.content = content;
        article.member = member;
        article.category = category;
        if(hashtags!=null){
            for (ArticleHashtag hashtag : hashtags) {
                article.addHashtag(hashtag);
            }
        }
        return article;
    }

    public static Article createArticle(String title, String content, Member member, Category category) {
        Article article = new Article();
        article.title = title;
        article.content = content;
        article.member = member;
        article.category = category;
        return article;
    }

    public static Article createArticleWithImgAndHashtags(String title, String content, Member member, Category category,
                                                          ArticleHashtag[] hashtags) {
        Article article = new Article();
        article.title = title;
        article.content = content;
        article.member = member;
        article.category = category;
        if(hashtags!=null){
            for (ArticleHashtag hashtag : hashtags) {
                article.addHashtag(hashtag);
            }
        }

        return article;
    }


    public void addHashtag(ArticleHashtag articleHashtag){
        articleHashtags.add(articleHashtag);
        articleHashtag.addArticle(this);
    }

    public void update(String title, String content,Category category){
        this.title = title;
        this.content = content;
        this.category = category;
    }
}
