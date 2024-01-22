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

    public Page<ArticleListDto> findAllWithPage(int page){
        PageRequest pageRequest = PageRequest.of(page,10, Sort.by(Sort.Direction.DESC,"id"));
        Page<Article> articles = articleRepository.findPage(pageRequest);
         return articles.map(article ->
                new ArticleListDto(article.getId(), article.getTitle(), article.getMember().getNickname(),
                        article.getCreatedDate(),article.getMember().getProfileImg()));
    }

    public Article findById(long id){
        return articleRepository.findArticleByArticleId(id);
    }

    /**
     * SPRING SECURITY로 본인인지 권한확인 기능 추가 해주세요
     */
    public void update(long id, AddArticleDto dto, String[] hashtags){
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

        article.update(dto.getTitle(),dto.getContent(),category);
        articleRepository.save(article);
    }
}
