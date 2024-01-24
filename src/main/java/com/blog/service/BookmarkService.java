package com.blog.service;

import com.blog.dto.ArticleListDto;
import com.blog.entity.Article;
import com.blog.entity.Bookmark;
import com.blog.entity.Member;
import com.blog.repository.ArticleRepository;
import com.blog.repository.BookMarkRepository;
import com.blog.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookmarkService {
    private final BookMarkRepository bookMarkRepository;
    private final MemberRepository memberRepository;
    private final ArticleRepository articleRepository;

    @Transactional
    public void save(String email,long articleId){
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 email입니다"));
        Article article = articleRepository.findById(articleId)
                .orElseThrow(()-> new IllegalArgumentException("not found article:"+ articleId));
        Bookmark bookmark = Bookmark.builder()
                .article(article)
                .member(member)
                .build();
        bookMarkRepository.save(bookmark);
    }

    @Transactional
    public void delete(String email, long articleId){
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 email입니다"));
        bookMarkRepository.deleteByMemberAndArticleId(member,articleId);
    }

    public List<Bookmark> findAll(String email){
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 email입니다"));
        return bookMarkRepository.findAllByMember(member);
    }

    /**
     * 게시글이 북마크되어있는 게시글인지 여부
     */
    public Boolean isBookmarked(String email,long articleId){
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 email입니다"));
        Article article = articleRepository.findById(articleId)
                .orElseThrow(()-> new IllegalArgumentException("not found article:"+ articleId));
        Optional<Bookmark> bookmark = bookMarkRepository.findBookmarkByArticleAndMember(article, member);
        return bookmark.isPresent();
    }

    public Page<ArticleListDto> findAllForBookmark(int page,String email){
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 email입니다"));
        PageRequest pageRequest = PageRequest.of(page,5, Sort.by(Sort.Direction.DESC,"id"));
        Page<Bookmark> bookmarks = bookMarkRepository.findPage(pageRequest, member.getId());
        return bookmarks.map(bookmark ->
                new ArticleListDto(bookmark.getArticle().getId(), bookmark.getArticle().getTitle(), bookmark.getArticle().getMember().getNickname(),
                        bookmark.getArticle().getCategory().getName(),bookmark.getArticle().getCreatedDate(),bookmark.getArticle().getMember().getProfileImg()));
    }
}
