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
        PageRequest pageRequest = PageRequest.of(page,5, Sort.by(Sort.Direction.DESC,"id"));
        Page<Article> articles = articleRepository.findPage(pageRequest);
         return articles.map(article ->
                new ArticleListDto(article.getId(), article.getTitle(), article.getMember().getNickname(),
                        article.getCategory().getName(),article.getCreatedDate(),article.getMember().getProfileImg()));
    }

    /**
     * 해시태그로 검색한 articleList
     */
    public Page<ArticleListDto> findAllWithPageAndHashtag(int page,long hashtagId){
        PageRequest pageRequest = PageRequest.of(page,5, Sort.by(Sort.Direction.DESC,"id"));
        Page<Article> articles = articleRepository.findPageWithHashtag(pageRequest,hashtagId);
        return articles.map(article ->
                new ArticleListDto(article.getId(), article.getTitle(), article.getMember().getNickname(),
                        article.getCategory().getName(),article.getCreatedDate(),article.getMember().getProfileImg()));
    }



    /**
     * 카테고리로 검색한 articleList
     */
    public Page<ArticleListDto> findAllWithPageAndCategory(int page,long categoryId){
        PageRequest pageRequest = PageRequest.of(page,5, Sort.by(Sort.Direction.DESC,"id"));
        Page<Article> articles = articleRepository.findPageWithCategory(pageRequest,categoryId);
        return articles.map(article ->
                new ArticleListDto(article.getId(), article.getTitle(), article.getMember().getNickname(),
                        article.getCategory().getName(),article.getCreatedDate(),article.getMember().getProfileImg()));
    }

    public Page<ArticleListDto> findPageForNotice(int page){
        PageRequest pageRequest = PageRequest.of(page,5, Sort.by(Sort.Direction.DESC,"id"));
        Page<Article> articles = articleRepository.findPageForNotice(pageRequest,"공지사항");
        return articles.map(article ->
                new ArticleListDto(article.getId(), article.getTitle(), article.getMember().getNickname(),
                        article.getCategory().getName(),article.getCreatedDate(),article.getMember().getProfileImg()));
    }

    /**
     * 검색어로 검색
     */
    public Page<ArticleListDto> findPageForSearch(int page, String keyword){
        String target = "%"+keyword+"%";
        PageRequest pageRequest = PageRequest.of(page,5, Sort.by(Sort.Direction.DESC,"id"));
        Page<Article> articles = articleRepository.findPageForSearch(pageRequest,target);
        return articles.map(article ->
                new ArticleListDto(article.getId(), article.getTitle(), article.getMember().getNickname(),
                        article.getCategory().getName(),article.getCreatedDate(),article.getMember().getProfileImg()));
    }


    public Page<ArticleListDto> findMyArticle(int page,String email){
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 email입니다"));
        PageRequest pageRequest = PageRequest.of(page,5, Sort.by(Sort.Direction.DESC,"id"));
        Page<Article> articles = articleRepository.findMyArticles(pageRequest,member.getId());
        return articles.map(article ->
                new ArticleListDto(article.getId(), article.getTitle(), article.getMember().getNickname(),
                        article.getCategory().getName(),article.getCreatedDate(),article.getMember().getProfileImg()));
    }

    public Article findById(long id){
        List<ArticleHashtag> articleHashtagList = articleHashtagRepository.findAllByArticleId(id);
        // N+1방지 작업
        if(articleHashtagList.isEmpty()){
            return articleRepository.findArticleWithoutLeftJoinById(id);
        }else{
            return articleRepository.findArticleById(id);
        }
    }
    
    @Transactional
    public void update(String email,long id, AddArticleDto dto, String[] hashtags){
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 email입니다"));
        Article article = null;
        List<ArticleHashtag> articleHashtagList = articleHashtagRepository.findAllByArticleId(id);
        // N+1방지 작업
        if(articleHashtagList.isEmpty()){
            article = articleRepository.findArticleWithoutLeftJoinById(id);
        }else{
            article =  articleRepository.findArticleById(id);
        }

        validateAuthor(member,article);

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
        Article finalArticle = article;
        newHashtags.forEach(hashtag -> {
            saveHashtag(hashtag);
            Hashtag newHashtag = hashtagRepository.findByname(hashtag)
                    .orElseThrow(()-> new IllegalArgumentException("newHashtag 저장실패"));
            ArticleHashtag articleHashtag = ArticleHashtag.builder().article(finalArticle)
                    .hashtag(newHashtag)
                    .build();
            articleHashtagRepository.save(articleHashtag);
        });
        // 해시태그 수정 end

        article.update(dto.getTitle(),dto.getContent(),category);
        articleRepository.save(article);
    }

    @Transactional
    public void delete(String email,long id){
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 email입니다"));
        Article article = null;
        List<ArticleHashtag> articleHashtagList = articleHashtagRepository.findAllByArticleId(id);
        // N+1방지 작업
        if(articleHashtagList.isEmpty()){
            article = articleRepository.findArticleWithoutLeftJoinById(id);
        }else{
            article =  articleRepository.findArticleById(id);
        }
        validateAuthor(member,article);

        articleRepository.deleteById(id);
    }


    private void validateAuthor(Member member,Article article){
        if(!article.getMember().getEmail().equals(member.getEmail())){
            throw new IllegalArgumentException("작성자가 아닙니다");
        }
    }
}
