package com.blog.repository;

import com.blog.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    @Query("select a from Article a join fetch a.articleHashtags ah " +
            "join fetch ah.hashtag h " +
            "join fetch a.category c where a.id =:id")
    Article findArticleByArticleId(@Param("id") long id);

    @Query(value = "select a from Article a left join a.member m",
            countQuery = "select count(a) from Article a")
    Page<Article> findPage(Pageable pageable);
}
