package com.blog.service;

import com.blog.dto.AddArticleDto;
import com.blog.dto.ArticleListDto;
import com.blog.dto.CommentResponse;
import com.blog.entity.*;
import com.blog.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

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
        saveHashtags(hashtags);
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
        saveHashtags(hashtags);
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


    private void saveHashtags(String[] hashtags) {
        for (String hashtag : hashtags) {
            Optional<Hashtag> tag = hashtagRepository.findByname(hashtag);
            if(tag.isEmpty()){
                hashtagRepository.save(new Hashtag(hashtag));
            }
        }
    }

    private void saveHashtag(String hashtag) {
        Optional<Hashtag> tag = hashtagRepository.findByname(hashtag);
            if(tag.isEmpty()){
                hashtagRepository.save(new Hashtag(hashtag));
            }
    }
    public List<Article> findAll(){
        return articleRepository.findAll();
    }

    public Page<ArticleListDto> findAllWithPage(){
        PageRequest pageRequest = PageRequest.of(0,10, Sort.by(Sort.Direction.DESC,"id"));
        Page<Article> articles = articleRepository.findPage(pageRequest);
         return articles.map(article ->
                new ArticleListDto(article.getId(), article.getTitle(), article.getMember().getNickname(),article.getCreatedDate()));
    }

    public Article findById(long id){
        return articleRepository.findArticleByArticleId(id);
    }

    /**
     * SPRING SECURITY로 본인인지 권한확인 기능 추가 해주세요
     */
    public void update(long id, AddArticleDto dto, String[] hashtags,String... imgPaths){
        Article article = articleRepository.findArticleByArticleId(id);
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(()-> new IllegalArgumentException("not found:" + dto.getCategoryId()));

        // 해시태그 수정 시작
        Set<String> newHashtags = new HashSet<>((hashtags != null) ? new HashSet<>(Arrays.asList(hashtags)) : Collections.emptySet());
        Set<String> existingHashtags = article.getArticleHashtags().stream()
                .map(articleHashtag -> articleHashtag.getHashtag().getName())
                .collect(Collectors.toSet());

        for (String existingHashtag : existingHashtags) {
            if(!newHashtags.contains(existingHashtag)){
                articleHashtagRepository.deleteByArticleAndHashtagName(article,existingHashtag);
            }
        }

        newHashtags.removeAll(existingHashtags);
        newHashtags.forEach(hashtag -> {
            saveHashtag(hashtag);
            Hashtag newHashtag = hashtagRepository.findByname(hashtag)
                    .orElseThrow(()-> new IllegalArgumentException("newHashtag 저장실패"));
            ArticleHashtag articleHashtag = ArticleHashtag.builder().article(article)
                    .hashtag(newHashtag)
                    .build();
            articleHashtagRepository.save(articleHashtag);
        });
        // 해시태그 수정 end

        // 이미지 수정 시작
        if(imgPaths!=null){
            ArticleImg[] imgs = new ArticleImg[imgPaths.length];

            for (int i = 0; i < imgs.length; i++) {
                ArticleImg articleImg = ArticleImg.builder().path(imgPaths[i]).build();
                imgs[i] = articleImg;
            }
            article.changeImg(imgs);
        }else{
            article.getImg().clear();
        }

        article.update(dto.getTitle(),dto.getContent(),category);
        articleRepository.save(article);
    }
}
