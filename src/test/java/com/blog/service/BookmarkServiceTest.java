package com.blog.service;

import com.blog.dto.AddArticleDto;
import com.blog.dto.AddCommentDto;
import com.blog.dto.CommentResponse;
import com.blog.entity.*;
import com.blog.repository.ArticleRepository;
import com.blog.repository.CategoryRepository;
import com.blog.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@EnabledIfEnvironmentVariable(
        named = "SPRING_PROFILES_ACTIVE",
        matches = "local"
)
@Transactional
class BookmarkServiceTest {
    @Autowired
    ArticleRepository articleRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    BookmarkService bookmarkService;
    @PersistenceContext
    EntityManager em;

    //@Test
    public void save() throws Exception {
        // given
        Member member = new Member("member1", "nick1", "pass", new Address("add","city", "lat", "lon"), MemberStatus.ACTIVE,null);

        memberRepository.save(member);
        Category category = new Category("name1");
        categoryRepository.save(category);

        Article newArticle = Article.builder().title("title1")
                .content("content1")
                .category(category)
                .member(member)
                .build();
        articleRepository.save(newArticle);

        //when
        bookmarkService.save("member1",1);

        em.flush();
        em.clear();
        List<Bookmark> list = bookmarkService.findAll("member1");

        //assertThat(list.size()).isEqualTo(1);
        //assertThat(list.get(0).getArticle().getTitle()).isEqualTo("title1");

    }

    //@Test
    public void booleanTest() throws Exception {
        // given
        Member member = new Member("member1", "nick1", "pass", new Address("add","city", "lat", "lon"), MemberStatus.ACTIVE,null);

        memberRepository.save(member);
        Category category = new Category("name1");
        categoryRepository.save(category);

        Article newArticle = Article.builder().title("title1")
                .content("content1")
                .category(category)
                .member(member)
                .build();
        articleRepository.save(newArticle);

        //when
        bookmarkService.save("member1",1);
        em.flush(); em.clear();

        List<Article> all = articleRepository.findAll();

        Boolean bookmarked = bookmarkService.isBookmarked("member1", all.get(0).getId());

        //assertThat(bookmarked).isTrue();
    }

    //@Test
    public void delete() throws Exception {
        // given
        Member member = new Member("member1", "nick1", "pass", new Address("add","city", "lat", "lon"), MemberStatus.ACTIVE,null);

        memberRepository.save(member);
        Category category = new Category("name1");
        categoryRepository.save(category);

        Article newArticle = Article.builder().title("title1")
                .content("content1")
                .category(category)
                .member(member)
                .build();
        articleRepository.save(newArticle);

        //when
        bookmarkService.save("member1",1);

        em.flush();
        em.clear();
        bookmarkService.delete("member1",1);
        em.flush();
        em.clear();
        List<Bookmark> list = bookmarkService.findAll("member1");

        //assertThat(list.size()).isEqualTo(0);


    }

}