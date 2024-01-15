package com.blog.repository;

import com.blog.entity.Article;
import com.blog.entity.ArticleHashtag;
import com.blog.entity.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArticleHashtagRepository extends JpaRepository<ArticleHashtag,Long> {

    List<ArticleHashtag> findAllByHashtag(@Param("hashtag")Hashtag hashtag);

    List<ArticleHashtag> findAllByArticleId(@Param("articleId") long id);

    @Modifying
    @Query("delete from ArticleHashtag ah where ah.article = :article")
    void deleteAllByArticleId(@Param("article") Article article);

    @Modifying
    @Query("delete from ArticleHashtag ah where ah.article = :article " +
            "and ah.hashtag.name = :hashtagName")
    void deleteByArticleAndHashtagName(@Param("article") Article article, @Param("hashtagName") String name);
}
