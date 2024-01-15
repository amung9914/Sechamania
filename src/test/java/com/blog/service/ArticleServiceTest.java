package com.blog.service;

import com.blog.dto.AddArticleDto;
import com.blog.entity.*;
import com.blog.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@Transactional@SpringBootTest
class ArticleServiceTest {
    @Autowired ArticleService articleService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired CategoryService categoryService;
    @PersistenceContext
    EntityManager em;

    @Test
    public void save() throws Exception {
        // given
        Member member = new Member("member1", "nick1", "pass", new Address("add","city", "lat", "lon"), MemberStatus.ACTIVE);

        memberRepository.save(member);
        Category category = new Category("name1");
        categoryService.save(category);

        AddArticleDto addArticleDto = new AddArticleDto("title1", "content1", category.getId());
        // when
        articleService.save("member1",addArticleDto);
        em.flush();
        em.clear();

        List<Article> list = articleService.findAll();

        // then
        Assertions.assertThat(list.get(0).getTitle()).isEqualTo("title1");
    }

    @Test
    public void saveWithImg() throws Exception {
        // given
        Member member = new Member("member1", "nick1", "pass", new Address("add","city", "lat", "lon"), MemberStatus.ACTIVE);

        memberRepository.save(member);
        Category category = new Category("name1");
        categoryService.save(category);

        AddArticleDto addArticleDto = new AddArticleDto("title1", "content1", category.getId());

        // String arr = "img1";
        String[] arr = {"img1","img2","img3"};

        // when
        articleService.saveWithImg("member1",addArticleDto,arr);
        em.flush();
        em.clear();

        List<Article> list = articleService.findAll();

        // then
        Assertions.assertThat(list.get(0).getTitle()).isEqualTo("title1");
    }

    @Test
    public void saveWithHashtags() throws Exception {
        // given
        Member member = new Member("member1", "nick1", "pass", new Address("add","city", "lat", "lon"), MemberStatus.ACTIVE);

        memberRepository.save(member);
        Category category = new Category("name1");
        categoryService.save(category);

        AddArticleDto addArticleDto = new AddArticleDto("title1", "content1", category.getId());

        // String arr = "img1";
        String[] arr = {"hash1","hash2","hash3"};

        // when
        articleService.saveWithHashtag("member1",addArticleDto,arr);
        em.flush();
        em.clear();

        List<Article> list = articleService.findAll();

        // then
        Assertions.assertThat(list.get(0).getTitle()).isEqualTo("title1");
    }

    @Test
    @Rollback(value = false)
    public void saveWithHashtagsAndImgs() throws Exception {
        // given
        Member member = new Member("member1", "nick1", "pass", new Address("add","city", "lat", "lon"), MemberStatus.ACTIVE);

        memberRepository.save(member);
        Category category = new Category("name1");
        categoryService.save(category);

        AddArticleDto addArticleDto = new AddArticleDto("title1", "content1", category.getId());

        // String arr = "img1";
        String[] arr = {"hash1","hash2","hash3"};
        String[] arr2 = {"img1","img2","img3"};

        // when
        articleService.saveArticleWithHashtagAndImg("member1",addArticleDto,arr,arr2);
        em.flush();
        em.clear();

        List<Article> list = articleService.findAll();
        System.out.println("*************************************");
        Article findArticle = articleService.findById(list.get(0).getId());
        // then
        System.out.println("findArticle = " + findArticle.getTitle());
        for (ArticleHashtag articleHashtag : findArticle.getArticleHashtags()) {
            System.out.println("Hashtag = " + articleHashtag.getHashtag().getName());
        }
        for (ArticleImg articleImg : findArticle.getImg()) {
            System.out.println("articleImg = " + articleImg.getPath());
        }
    }

}