package com.blog.repository;

import com.blog.entity.Article;
import com.blog.entity.Bookmark;
import com.blog.entity.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookMarkRepository extends JpaRepository<Bookmark, Long> {

    @EntityGraph(attributePaths = {"article"})
    List<Bookmark> findAllByMember(@Param("member")Member member);

    Optional<Bookmark> findBookmarkByArticleAndMember(@Param("article") Article article, @Param("member") Member member);
}
