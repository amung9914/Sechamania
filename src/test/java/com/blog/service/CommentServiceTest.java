package com.blog.service;

import com.blog.dto.CommentRequestDto;
import com.blog.dto.CommentResponseDto;
import com.blog.entity.*;
import com.blog.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@EnabledIfEnvironmentVariable(
        named = "SPRING_PROFILES_ACTIVE",
        matches = "local"
)
@Transactional
class CommentServiceTest {

    @Autowired
    ArticleService articleService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired CategoryService categoryService;
    @Autowired CommentService commentService;
    @Autowired
    @PersistenceContext
    EntityManager em;

    @Test
    @Rollback(value = false)
    public void save() throws Exception {
        List<Article> all = articleService.findAll();

        // given
        CommentRequestDto dto1 = new CommentRequestDto("댓글1", all.get(0).getId(), null);
        // when
        Long id = commentService.createComment(dto1, "admin@admin.com");
        CommentRequestDto dto2 = new CommentRequestDto("답글1", all.get(0).getId(), id);
        Long id2 = commentService.createComment(dto2, "admin@admin.com");
        CommentRequestDto dto3 = new CommentRequestDto("답글1의 답글1", all.get(0).getId(), id2);
        commentService.createComment(dto3, "admin@admin.com");
        em.flush();
        em.clear();
        List<CommentResponseDto> list = commentService.getAllCommentsByArticle(all.get(0).getId());
        System.out.println("list.size() = " + list.size());
        for (CommentResponseDto commentResponseDto : list) {
            System.out.println("commentResponseDto.getCommentId() = " + commentResponseDto.getCommentId());
            System.out.println("commentResponseDto.getContent() = " + commentResponseDto.getContent());
            System.out.println("commentResponseDto.getParentId() = " + commentResponseDto.getParentId());
            List<CommentResponseDto> children = commentResponseDto.getChildren();
            for (CommentResponseDto child : children) {
                System.out.println("child.getContent() = " + child.getContent());
            }
        }

    }

}