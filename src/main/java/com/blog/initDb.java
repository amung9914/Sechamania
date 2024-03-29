package com.blog;

import com.blog.dto.AddArticleDto;
import com.blog.dto.CommentRequestDto;
import com.blog.entity.*;
import com.blog.repository.AuthorityRepository;
import com.blog.repository.MemberRepository;
import com.blog.service.ArticleService;
import com.blog.service.BookmarkService;
import com.blog.service.CategoryService;
import com.blog.service.CommentService;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class initDb {

    private final InitService initService;

    @PostConstruct
    public void init(){

        initService.dbInit1();
        initService.dbInit2();
        initService.dbInit3();
        initService.makeCategories();
        for (int i = 0; i < 10; i++) {
            initService.makeArticle();
        }
        initService.makeComment();
        initService.makeBookmark();

    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService{
        private final MemberRepository memberRepository;
        private final AuthorityRepository authorityRepository;
        private final CategoryService categoryService;
        private final ArticleService articleService;
        private final CommentService commentService;
        private final BookmarkService bookmarkService;

        public void dbInit1(){
            Member newMember = Member.builder()
                    .email("admin@admin.com")
                    .password("$2a$10$WvmocswXUNtFTenr8jZh4uNPwmRrTGufupnElllKk61.OaG1GqXQe")
                    .nickname("관리자")
                    .address(new Address("서울 영등포구 국제금융로 101층","영등포구","37.5251775245928","126.924876706923"))
                    .status(MemberStatus.ACTIVE)
                    .imgPath("/img/defaultProfile.jpg")
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
                    .nickname("몽몽")
                    .address(new Address("서울 영등포구 국제금융로 101층","영등포구","37.5251775245928","126.924876706923"))
                    .status(MemberStatus.ACTIVE)
                    .imgPath("/img/defaultProfile.jpg")
                    .build();
            memberRepository.save(newMember);

            Authorities userAuth = new Authorities("ROLE_USER");
            authorityRepository.save(userAuth);
            userAuth.makeRole(newMember);

            Authorities companyAuth = new Authorities("ROLE_COMPANY");
            authorityRepository.save(companyAuth);
            companyAuth.makeRole(newMember);
        }

        public void dbInit3(){
            Member newMember = Member.builder()
                    .email("user1@user.com")
                    .password("$2a$10$WvmocswXUNtFTenr8jZh4uNPwmRrTGufupnElllKk61.OaG1GqXQe")
                    .nickname("보리이즈마인")
                    .address(new Address("서울 영등포구 국제금융로 101층","영등포구","37.5251775245928","126.924876706923"))
                    .status(MemberStatus.ACTIVE)
                    .imgPath("/img/defaultProfile.jpg")
                    .build();
            memberRepository.save(newMember);

            Authorities userAuth = new Authorities("ROLE_USER");
            authorityRepository.save(userAuth);
            userAuth.makeRole(newMember);
        }

        public void makeCategories(){
            Long id = categoryService.save("공지사항");
            categoryService.save("자유게시판");
            categoryService.save("제품리뷰");
            categoryService.save("셀프세차장후기");
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
            String[] arr2 = {"날씨","영하","눈소식",};
            articleService.saveWithHashtag("user@user.com",addArticleDto2,arr2);

            Category category3 = categoryService.findByName("공지사항");
            AddArticleDto addArticleDto3 = new AddArticleDto("개인정보 처리방침 변경 안내(2024.01.24)",
                    "개인정보 처리방침 내용을 일부 개정하게 되어 2024년 1월 24일자로 새롭게 시행되기에 이를 알려드리고자 합니다. 아래 내용을 참조하시어 서비스 이용에 불편이 없도록 유의하시기 바랍니다.", category3.getId());

            articleService.save("admin@admin.com",addArticleDto3);
            AddArticleDto addArticleDto4 = new AddArticleDto("세차매니아 로그인에 문제가 있으신가요?",
                    "세차매니아에서는 이메일 형태의 아이디와 구글을 통한 로그인이 제공되고 있습니다.", category3.getId());
            articleService.save("admin@admin.com",addArticleDto4);

            AddArticleDto addArticleDto5 = new AddArticleDto("쓱싹스팀세차 마곡점 이용 후기", "셀스세차장은 아닌데 좋아서 가봤어요", category1.getId());
            String[] arr5 = {"스팀세차"};
            articleService.saveWithHashtag("user1@user.com",addArticleDto5,arr5);

            AddArticleDto addArticleDto6 = new AddArticleDto("어제 손세차하다가 얼음될뻔 했네요", "다들 조심 하시길", category.getId());
            String[] arr6 = {"날씨","얼음","강추위","손세차"};
            articleService.saveWithHashtag("user1@user.com",addArticleDto6,arr6);

        }
        public void makeComment(){
            List<Article> all = articleService.findAll();
            // given
            CommentRequestDto dto1 = new CommentRequestDto("댓글1", all.get(0).getId(), null);
            // when
            Long id = commentService.createComment(dto1, "user1@user.com");
            CommentRequestDto dto2 = new CommentRequestDto("답글1", all.get(0).getId(), id);
            Long id2 = commentService.createComment(dto2, "user@user.com");
            CommentRequestDto dto3 = new CommentRequestDto("답글1의 답글1", all.get(0).getId(), id2);
            commentService.createComment(dto3, "user1@user.com");
        }

        public void makeBookmark(){
            bookmarkService.save("admin@admin.com",1);
            bookmarkService.save("admin@admin.com",2);
            bookmarkService.save("admin@admin.com",3);
        }
    }
}


