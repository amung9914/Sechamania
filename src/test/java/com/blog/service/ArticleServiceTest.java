package com.blog.service;

import com.blog.dto.AddArticleDto;
import com.blog.dto.ArticleListDto;
import com.blog.entity.*;
import com.blog.repository.ArticleRepository;
import com.blog.repository.CategoryRepository;
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
@Transactional@SpringBootTest
class ArticleServiceTest {
    @Autowired ArticleService articleService;
    @Autowired
    ArticleRepository articleRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired CategoryService categoryService;
    @Autowired
    CategoryRepository categoryRepository;
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
        // when
        articleService.save("member1",addArticleDto);
        em.flush();
        em.clear();

        List<Article> list = articleService.findAll();

        // then
        Assertions.assertThat(list.get(0).getTitle()).isEqualTo("title1");
    }

    @Test
    public void saveWithHashtags() throws Exception {
        // given
        Member member = new Member("member1", "nick1", "pass", new Address("add","city", "lat", "lon"), MemberStatus.ACTIVE,null);

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
    public void findAllByPage() throws Exception {
        // given
        Member member = new Member("member1", "nick1", "pass", new Address("add","city", "lat", "lon"), MemberStatus.ACTIVE,null);

        memberRepository.save(member);
        Category category = new Category("name1");
        categoryRepository.save(category);

        Article article1 = Article.builder().title("title1")
                .content("content1")
                .category(category)
                .member(member)
                .build();
        Article article2 = Article.builder().title("title2")
                .content("content1")
                .category(category)
                .member(member)
                .build();
        Article article3 = Article.builder().title("title3")
                .content("content1")
                .category(category)
                .member(member)
                .build();

        articleRepository.save(article1);
        articleRepository.save(article2);
        articleRepository.save(article3);

        Page<ArticleListDto> dto = articleService.findAllWithPage(0);

        // then
        Assertions.assertThat(dto.getTotalElements()).isEqualTo(3);
        Assertions.assertThat(dto.getContent().get(0).getTitle()).isEqualTo("title3");
    }

}