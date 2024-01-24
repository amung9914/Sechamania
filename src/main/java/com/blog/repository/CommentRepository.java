package com.blog.repository;

import com.blog.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment,Long> {

    Page<Comment> findCommentsByArticle_Id(long articleId, Pageable ppageable);


    @EntityGraph(attributePaths = {"member"})
    Optional<Comment> findCommentById(long id);
}
