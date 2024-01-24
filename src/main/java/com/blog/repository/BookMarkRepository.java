package com.blog.repository;

import com.blog.entity.Article;
import com.blog.entity.Bookmark;
import com.blog.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookMarkRepository extends JpaRepository<Bookmark, Long> {

    @EntityGraph(attributePaths = {"article"})
    List<Bookmark> findAllByMember(@Param("member")Member member);

    Optional<Bookmark> findBookmarkByArticleAndMember(@Param("article") Article article, @Param("member") Member member);

    void deleteByMemberAndArticleId(Member member,long articleId);

    @Query(value = "select b from Bookmark b " +
            "join fetch b.article a " +
            "join fetch a.category ac " +
            "join fetch a.member am " +
            "join fetch b.member m where m.id = :memberId",
            countQuery = "select count(b) from Bookmark b where b.member.id = :memberId")
    Page<Bookmark> findPage(Pageable pageable, long memberId);

}
