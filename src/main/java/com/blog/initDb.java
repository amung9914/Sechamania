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
        initService.makeArticle();
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
            // when
            articleService.save("admin@admin.com",addArticleDto);
        }
    }
}


