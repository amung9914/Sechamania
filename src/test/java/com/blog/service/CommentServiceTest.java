package com.blog.service;

import com.blog.dto.AddArticleDto;
import com.blog.dto.AddCommentDto;
import com.blog.dto.CommentResponse;
import com.blog.entity.*;
import com.blog.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class CommentServiceTest {

    @Autowired
    ArticleService articleService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired CategoryService categoryService;
    @Autowired CommentService commentService;
    @PersistenceContext
    EntityManager em;


    @Test
    public void save() throws Exception {
        // given
        Member member = new Member("member1", "nick1", "pass", new Address("add","city", "lat", "lon"), MemberStatus.ACTIVE,null);

        memberRepository.save(member);
        Category category = new Category("name1");
        categoryService.save(category);

        AddArticleDto addArticleDto = new AddArticleDto("title1", "content1", category.getId());

        articleService.save("member1",addArticleDto);
        em.flush();
        em.clear();

        List<Article> articleList = articleService.findAll();

        // when
        AddCommentDto commentDto = new AddCommentDto("newContent", articleList.get(0).getId());
        commentService.save("member1",commentDto);
        em.flush();
        em.clear();
        Page<CommentResponse> commentResponses = commentService.findByArticle(articleList.get(0).getId());
        Assertions.assertThat(commentResponses.getTotalElements()).isEqualTo(1);
        Assertions.assertThat(commentResponses.getContent().get(0).getContent()).isEqualTo("newContent");

    }

    @Test
    public void update() throws Exception {
        // given
        Member member = new Member("member1", "nick1", "pass", new Address("add", "city", "lat", "lon"), MemberStatus.ACTIVE,null);

        memberRepository.save(member);
        Category category = new Category("name1");
        categoryService.save(category);

        AddArticleDto addArticleDto = new AddArticleDto("title1", "content1", category.getId());

        articleService.save("member1", addArticleDto);
        em.flush();
        em.clear();

        List<Article> articleList = articleService.findAll();

        AddCommentDto commentDto = new AddCommentDto("newContent", articleList.get(0).getId());
        commentService.save("member1", commentDto);
        em.flush();
        em.clear();

        // when

        commentService.update(1,"수정");
        em.flush();
        em.clear();
        Page<CommentResponse> commentResponses = commentService.findByArticle(articleList.get(0).getId());
        Assertions.assertThat(commentResponses.getContent().get(0).getContent()).isEqualTo("수정");
    }

    @Test
    public void delete() throws Exception {
        // given
        Member member = new Member("member1", "nick1", "pass", new Address("add", "city", "lat", "lon"), MemberStatus.ACTIVE,null);

        memberRepository.save(member);
        Category category = new Category("name1");
        categoryService.save(category);

        AddArticleDto addArticleDto = new AddArticleDto("title1", "content1", category.getId());

        articleService.save("member1", addArticleDto);
        em.flush();
        em.clear();

        List<Article> articleList = articleService.findAll();

        AddCommentDto commentDto = new AddCommentDto("newContent", articleList.get(0).getId());
        commentService.save("member1", commentDto);
        em.flush();
        em.clear();

        // when

        commentService.delete(1);
        em.flush();
        em.clear();
        Page<CommentResponse> commentResponses = commentService.findByArticle(articleList.get(0).getId());
        Assertions.assertThat(commentResponses.getTotalElements()).isEqualTo(0);
    }
}