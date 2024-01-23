package com.blog.entity;

import com.blog.dto.CommentRequestDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Comment extends BaseEntity{
    @Id @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @Builder.Default
    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    private List<Comment> children = new ArrayList<>();

    public Comment() {
    }

    @Builder
    public Comment(String content, Article article, Member member) {
        this.content = content;
        this.article = article;
        this.member = member;
    }

    public void update(CommentRequestDto commentRequestDto){
        this.content = commentRequestDto.getContent();
    }

    // 부모 댓글 수정
    public void updateParent(Comment parent){
        this.parent = parent;
    }

    public void changeContent(String content){
        this.content = content;
    }
}
