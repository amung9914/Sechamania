package com.blog.service;

import com.blog.dto.AddCommentDto;
import com.blog.dto.CommentResponse;
import com.blog.entity.Article;
import com.blog.entity.Comment;
import com.blog.entity.Member;
import com.blog.repository.ArticleRepository;
import com.blog.repository.CommentRepository;
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
public class CommentService {
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final ArticleRepository articleRepository;


    public Page<CommentResponse> findByArticle(long articleId){
        PageRequest pageRequest = PageRequest.of(0,5, Sort.by(Sort.Direction.DESC,"id"));
        Page<Comment> comments = commentRepository.findCommentsByArticle_Id(articleId, pageRequest);
        Page<CommentResponse> map = comments.map(comment ->
                new CommentResponse(comment.getId(), comment.getContent(), comment.getMember().getNickname()));
        return map;
    }

    @Transactional
    public Long save(String email, AddCommentDto dto){
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 email입니다"));

        Article findArticle = articleRepository.findById(dto.getArticleId())
                        .orElseThrow(() -> new IllegalArgumentException("not found Article:" + dto.getArticleId()));

        Comment newComment = Comment.builder()
                .member(member)
                .content(dto.getContent())
                .article(findArticle)
                .build();
        return commentRepository.save(newComment).getId();
    }


    /**
     * SPRING SECURITY로 본인인지 권한확인 기능 추가 해주세요
     */
    @Transactional
    public void update(long id, String content){
        Comment findComment = commentRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("not found comment :"+id));
        findComment.changeContent(content);
    }
    /**
     * SPRING SECURITY로 본인인지 권한확인 기능 추가 해주세요
     */
    @Transactional
    public void delete(long id){
        Comment findComment = commentRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("not found comment :"+id));
        commentRepository.delete(findComment);
    }
}
