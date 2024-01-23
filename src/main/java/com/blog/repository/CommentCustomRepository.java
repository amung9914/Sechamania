package com.blog.repository;

import com.blog.entity.Article;
import com.blog.entity.Comment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.blog.entity.QComment.comment;

@Repository
@RequiredArgsConstructor
public class CommentCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<Comment> findAllByArticle(Article article){
        return jpaQueryFactory.selectFrom(comment)
                .leftJoin(comment.parent)
                .join(comment.member)
                .where(comment.article.id.eq(article.getId()))
                .orderBy(comment.parent.id.asc().nullsFirst(), comment.createdDate.asc())
                .fetch();
    }

}
