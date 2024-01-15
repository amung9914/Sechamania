package com.blog.repository;

import com.blog.dto.AddUserRequest;
import com.blog.entity.*;
import com.blog.service.MemberService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class ArticleRepositoryTest {

    @Autowired ArticleRepository articleRepository;
    @Autowired MemberRepository memberRepository;
    @PersistenceContext
    EntityManager em;

    @Test
    public void articleTest() throws Exception {
        // given
        Member member = new Member("member1", "nick1", "pass", new Address("add","city", "lat", "lon"), MemberStatus.ACTIVE);

        memberRepository.save(member);

        ArticleImg articleImg = ArticleImg.builder()
                .path("path")
                .build();

        // when
        Article newArticle = Article.createArticle("title","content",member,null,articleImg);

        articleRepository.save(newArticle);
        em.flush();
        em.clear();

        // when

        List<Article> all = articleRepository.findAll();

        // then
        org.assertj.core.api.Assertions.assertThat(all.get(0).getTitle()).isEqualTo("title");
        org.assertj.core.api.Assertions.assertThat(all.get(0).getImg().get(0).getPath()).isEqualTo(articleImg.getPath());

    }

}