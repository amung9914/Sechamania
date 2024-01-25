package com.blog.repository;

import com.blog.entity.Article;
import com.blog.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    @Query("select a from Article a " +
            "join fetch a.category c " +
            "join fetch a.member m " +
            "where a.id =:id")
    Article findArticleWithoutLeftJoinById(@Param("id") long id);

    @Query("select a from Article a " +
            "join fetch a.category c " +
            "join fetch a.member m " +
            "join fetch a.articleHashtags ah " +
            "join fetch ah.hashtag h " +
            "where a.id =:id")
    Article findArticleById(@Param("id") long id);

    @Query(value = "select a from Article a left join a.member m " +
            "join fetch a.category c where c.name !='공지사항'",
            countQuery = "select count(a) from Article a")
    Page<Article> findPage(Pageable pageable);

    @Query(value = "select a from Article a left join a.member m " +
            "join a.articleHashtags ah " +
            "join ah.hashtag h where h.id =:hashtagId",
            countQuery = "select count(a) from Article a join a.articleHashtags ah join ah.hashtag h where h.id =:hashtagId")
    Page<Article> findPageWithHashtag(Pageable pageable, @Param("hashtagId")long hashtagId);

    @Query(value = "select a from Article a left join a.member m " +
            "join a.category c where c.id =:categoryId",
            countQuery = "select count(a) from Article a join a.category c where c.id =:categoryId")
    Page<Article> findPageWithCategory(Pageable pageable, @Param("categoryId")long categoryId);



    @Query(value = "select a from Article a left join a.member m " +
            "join fetch a.category c where c.name = :name",
            countQuery = "select count(a) from Article a join a.category c where c.name = :name")
    Page<Article> findPageForNotice(Pageable pageable,@Param("name")String name);

    @Query(value = "select a from Article a " +
            "join fetch a.member m " +
            "join fetch a.category c where m.id = :memberId",
            countQuery = "select count(a) from Article a join a.member m where a.member.id = :memberId")
    Page<Article> findMyArticles(Pageable pageable, @Param("memberId")long memberId);

}
