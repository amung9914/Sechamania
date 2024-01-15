package com.blog.service;

import com.blog.dto.AddArticleDto;
import com.blog.entity.*;
import com.blog.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;
    private final HashtagRepository hashtagRepository;
    private final ArticleHashtagRepository articleHashtagRepository;

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

    @Transactional
    public void saveWithHashtag(String email, AddArticleDto dto, String[] hashtags){
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 email입니다"));

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(()-> new IllegalArgumentException("not found:" + dto.getCategoryId()));

        ArticleHashtag[] hashtagArr = new ArticleHashtag[hashtags.length];
        saveHashtag(hashtags);
        for (int i = 0; i < hashtags.length; i++) {
            Hashtag findtag = hashtagRepository.findByname(hashtags[i])
                    .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 hashtag"));
            hashtagArr[i] = ArticleHashtag.builder().hashtag(findtag).build();
        }
        Article newArticle = Article.createArticleWtihHashtags(dto.getTitle(), dto.getContent(), member, category, hashtagArr);
        articleRepository.save(newArticle);
        articleHashtagRepository.saveAll(Arrays.asList(hashtagArr));
    }

    @Transactional
    public void saveArticleWithHashtagAndImg(String email, AddArticleDto dto, String[] hashtags,String... imgPaths){
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 email입니다"));

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(()-> new IllegalArgumentException("not found:" + dto.getCategoryId()));

        ArticleHashtag[] hashtagArr = new ArticleHashtag[hashtags.length];
        saveHashtag(hashtags);
        for (int i = 0; i < hashtags.length; i++) {
            Hashtag findtag = hashtagRepository.findByname(hashtags[i])
                    .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 hashtag"));
            hashtagArr[i] = ArticleHashtag.builder().hashtag(findtag).build();
        }

        ArticleImg[] imgArr = new ArticleImg[imgPaths.length];

        for (int i = 0; i < imgPaths.length; i++) {
            ArticleImg articleImg = ArticleImg.builder()
                    .path(imgPaths[i])
                    .build();
            imgArr[i] = articleImg;
        }

        Article newArticle = Article.createArticleWithImgAndHashtags(dto.getTitle(), dto.getContent(), member, category, hashtagArr,imgArr);
        articleRepository.save(newArticle);
        articleHashtagRepository.saveAll(Arrays.asList(hashtagArr));
    }


    private void saveHashtag(String[] hashtags) {
        for (String hashtag : hashtags) {
            Optional<Hashtag> tag = hashtagRepository.findByname(hashtag);
            if(tag.isEmpty()){
                hashtagRepository.save(new Hashtag(hashtag));
            }
        }
    }
    public List<Article> findAll(){
        return articleRepository.findAll(Sort.by(Sort.Direction.DESC,"id"));
    }

    public Article findById(long id){
        return articleRepository.findArticleByArticleId(id);
    }
}
