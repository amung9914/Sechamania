package com.blog.repository;

import com.blog.dto.AddUserRequest;
import com.blog.entity.*;
import com.blog.service.MemberService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@EnabledIfEnvironmentVariable(
        named = "SPRING_PROFILES_ACTIVE",
        matches = "local"
)
@Transactional
class ArticleRepositoryTest {

    @Autowired ArticleRepository articleRepository;
    @Autowired MemberRepository memberRepository;
    @PersistenceContext
    EntityManager em;

    @Test
    public void articleTest() throws Exception {
        // given
        Member member = new Member("member1", "nick1", "pass", new Address("add","city", "lat", "lon"), MemberStatus.ACTIVE,null);

        memberRepository.save(member);

        // when
        Article newArticle = Article.createArticle("title","content",member,null);

        articleRepository.save(newArticle);
        em.flush();
        em.clear();

        // when

        List<Article> all = articleRepository.findAll();

        // then

    }

}