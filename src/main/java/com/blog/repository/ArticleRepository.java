package com.blog.repository;

import com.blog.entity.Article;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    /**
     * hashtag와는 패치조인이 가능하지만
     * img와는 패치조인이 동시에 되지 않는다.(컬렉션 둘 이상 패치조인 불가)
     * 따라서
     * 1.배치사이즈 이용 - 쿼리 4번,
     * 2. 패치조인 사용 및 img 따로 객체 그래프 조회 - 쿼리 2번
     *  2번째 방법을 선택하였음
     */
    @Query("select a from Article a join fetch a.articleHashtags ah " +
            "join fetch ah.hashtag h where a.id = :id")
    Article findArticleByArticleId(@Param("id") long id);
}
