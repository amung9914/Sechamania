package com.blog;

import com.blog.dto.AddArticleDto;
import com.blog.entity.*;
import com.blog.repository.AuthorityRepository;
import com.blog.repository.MemberRepository;
import com.blog.service.ArticleService;
import com.blog.service.CategoryService;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class initDb {

    private final InitService initService;

    @PostConstruct
    public void init(){

        initService.dbInit1();
        initService.dbInit2();
        initService.makeCategories();
        //for (int i = 0; i <100; i++) {
            initService.makeArticle();
        //}
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService{
        private final MemberRepository memberRepository;
        private final AuthorityRepository authorityRepository;
        private final CategoryService categoryService;
        private final ArticleService articleService;

        public void dbInit1(){
            Member newMember = Member.builder()
                    .email("admin@admin.com")
                    .password("$2a$10$WvmocswXUNtFTenr8jZh4uNPwmRrTGufupnElllKk61.OaG1GqXQe")
                    .nickname("admin")
                    .address(new Address("서울 영등포구 국제금융로 101층","영등포구","37.5251775245928","126.924876706923"))
                    .status(MemberStatus.ACTIVE)
                    .imgPath("img/defaultProfile.jpg")
                    .build();
            memberRepository.save(newMember);

            Authorities userAuth = new Authorities("ROLE_USER");
            authorityRepository.save(userAuth);
            userAuth.makeRole(newMember);

            Authorities companyAuth = new Authorities("ROLE_COMPANY");
            authorityRepository.save(companyAuth);
            companyAuth.makeRole(newMember);

            Authorities adminAuth = new Authorities("ROLE_ADMIN");
            authorityRepository.save(adminAuth);
            adminAuth.makeRole(newMember);
        }
        public void dbInit2(){
            Member newMember = Member.builder()
                    .email("user@user.com")
                    .password("$2a$10$WvmocswXUNtFTenr8jZh4uNPwmRrTGufupnElllKk61.OaG1GqXQe")
                    .nickname("user")
                    .address(new Address("서울 영등포구 국제금융로 101층","영등포구","37.5251775245928","126.924876706923"))
                    .status(MemberStatus.ACTIVE)
                    .imgPath("img/defaultProfile.jpg")
                    .build();
            memberRepository.save(newMember);

            Authorities userAuth = new Authorities("ROLE_USER");
            authorityRepository.save(userAuth);
            userAuth.makeRole(newMember);

            Authorities companyAuth = new Authorities("ROLE_COMPANY");
            authorityRepository.save(companyAuth);
            companyAuth.makeRole(newMember);
        }

        public void makeCategories(){
            Category category1 = new Category("공지사항");
            Category category2 = new Category("자유게시판");
            Category category3 = new Category("제품리뷰");
            Category category4 = new Category("셀프세차장후기");
            Long id = categoryService.save(category1);
            categoryService.save(category2);
            categoryService.save(category3);
            categoryService.save(category4);
        }

        public void makeArticle(){
            Category category = categoryService.findByName("자유게시판");
            AddArticleDto addArticleDto = new AddArticleDto("안녕하세요", "여러분 환영합니다", category.getId());
            String[] arr = {"환영인사","안녕"};

            // when
            articleService.saveWithHashtag("admin@admin.com",addArticleDto,arr);

            Category category1 = categoryService.findByName("셀프세차장후기");
            AddArticleDto addArticleDto1 = new AddArticleDto("킹콩샤워 마곡직영점 사용 후기", "생각보다 좋을지도", category1.getId());
            String[] arr1 = {"마곡","셀프세차"};
            articleService.saveWithHashtag("user@user.com",addArticleDto1,arr1);

            AddArticleDto addArticleDto2 = new AddArticleDto("오늘 진짜 춥네요", "낮인데 영하 7도네요", category.getId());
            String[] arr2 = {"날씨","영하","눈소식","눈소식1","눈소식2","눈소식3","눈소식4","눈소식5",};
            articleService.saveWithHashtag("user@user.com",addArticleDto2,arr2);
        }
    }
}


