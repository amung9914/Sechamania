package com.blog.service;

import com.blog.dto.AddArticleDto;
import com.blog.entity.Article;
import com.blog.entity.ArticleImg;
import com.blog.entity.Category;
import com.blog.entity.Member;
import com.blog.repository.ArticleRepository;
import com.blog.repository.CategoryRepository;
import com.blog.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long save(String email, AddArticleDto dto){
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 email입니다"));

        Category category = categoryRepository.findById(dto.getCategoryId())
                        .orElseThrow(()-> new IllegalArgumentException("not found:" + dto.getCategoryId()));

        // when
        Article newArticle = Article.builder().title(dto.getTitle())
                .content(dto.getContent())
                .category(category)
                .member(member)
                .build();

        return articleRepository.save(newArticle).getId();
    }

    /**
     * 첨부 이미지를 가진 article 저장.
     * imgPaths는 String 변수 하나로 던져도 정상동작
     */
    @Transactional
    public Long saveWithImg(String email, AddArticleDto dto, String... imgPaths) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 email입니다"));

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("not found:" + dto.getCategoryId()));

        ArticleImg[] imgArr = new ArticleImg[imgPaths.length];

        for (int i = 0; i < imgPaths.length; i++) {
            ArticleImg articleImg = ArticleImg.builder()
                    .path(imgPaths[i])
                    .build();
            imgArr[i] = articleImg;
        }

        // when
        Article newArticle = Article.createArticle(dto.getTitle(), dto.getContent(), member, category, imgArr);
        return articleRepository.save(newArticle).getId();
    }


    public List<Article> findAll(){
        return articleRepository.findAll(Sort.by(Sort.Direction.DESC,"id"));
    }
}
