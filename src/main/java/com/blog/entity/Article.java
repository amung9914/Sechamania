package com.blog.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Builder
    public Article(String title, String content, Member member, Category category) {
        this.title = title;
        this.content = content;
        this.member = member;
        this.category = category;
    }
}
