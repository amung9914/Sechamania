package com.blog.service;

import com.blog.dto.AddCommentDto;
import com.blog.dto.CommentRequestDto;
import com.blog.dto.CommentResponse;
import com.blog.dto.CommentResponseDto;
import com.blog.entity.Article;
import com.blog.entity.Comment;
import com.blog.entity.Member;
import com.blog.repository.ArticleRepository;
import com.blog.repository.CommentCustomRepository;
import com.blog.repository.CommentRepository;
import com.blog.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final ArticleRepository articleRepository;
    private final CommentCustomRepository commentCustomRepository;

    @Transactional
    public Long createComment(CommentRequestDto dto, String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 email입니다"));
        Article findArticle = articleRepository.findById(dto.getArticleId())
                .orElseThrow(() -> new IllegalArgumentException("not found Article:" + dto.getArticleId()));

        Comment parent = null;
        //자식댓글인 경우
        if (dto.getParentId() != null) {
            parent = commentRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException("not found parent:" + dto.getParentId()));
            // 부모댓글의 게시글 번호화 자식댓글의 게시글 번호 같은지 체크
            if (parent.getArticle().getId() != dto.getArticleId()) {
                throw new IllegalArgumentException("부모댓글과 자식댓글의 게시글 번호가 일치하지 않습니다");
            }
        }
        Comment comment = Comment.builder()
                .member(member)
                .article(findArticle)
                .content(dto.getContent())
                .build();
        if (parent != null) {
            comment.updateParent(parent);
        }
        return commentRepository.save(comment).getId();
    }

    public List<CommentResponseDto> getAllCommentsByArticle(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("not found Article:" + articleId));
        List<Comment> commentList = commentCustomRepository.findAllByArticle(article);
        if (commentList.isEmpty()) {
            return null;
        }
        List<CommentResponseDto> dtoList = new ArrayList<>();
        Map<Long, CommentResponseDto> map = new HashMap<>();
        commentList.stream().forEach(c -> {
            CommentResponseDto cdto = new CommentResponseDto(c);
            if (c.getParent() != null) {
                cdto.setParentId(c.getParent().getId());
            }
            map.put(cdto.getCommentId(), cdto);
            // 자식댓글이면 map에서 부모를 찾아서 자식 댓글 리스트에 추가한다.
            if (c.getParent() != null)
                map.get(c.getParent().getId()).getChildren().add(cdto);
                //최상위댓글이면 바로 리스트에 추가한다
            else dtoList.add(cdto);
        });
        return dtoList;
    }

    @Transactional
    public void updateComment(String email, long id, String content) {
        Comment comment = commentRepository.findCommentById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found comment :" + id));
        validateForComment(comment, email);
        comment.changeContent(content);
    }

    @Transactional
    public void delete(long id,String email) {
        Comment findComment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found comment :" + id));
        validateForComment(findComment, email);
        commentRepository.delete(findComment);
    }

    private void validateForComment(Comment comment, String email) {
        if (!comment.getMember().getEmail().equals(email)) {
            throw new IllegalArgumentException("작성자가 아닙니다");
        }
    }

     /*public Page<CommentResponse> findByArticle(long articleId){
        PageRequest pageRequest = PageRequest.of(0,5, Sort.by(Sort.Direction.DESC,"id"));
        Page<Comment> comments = commentRepository.findCommentsByArticle_Id(articleId, pageRequest);
        Page<CommentResponse> map = comments.map(comment ->
                new CommentResponse(comment.getId(), comment.getContent(), comment.getMember().getNickname()));
        return map;
        }
     */
}



