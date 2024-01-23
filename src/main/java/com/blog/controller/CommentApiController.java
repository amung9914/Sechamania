package com.blog.controller;

import com.blog.dto.CommentRequestDto;
import com.blog.dto.CommentResponseDto;
import com.blog.entity.Member;
import com.blog.service.CommentService;
import com.blog.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentApiController {

    private final CommentService commentService;
    private final MemberService memberService;

    @GetMapping("/api/comment/{articleId}")
    public ResultWithAuthor findComment(@PathVariable long articleId, Principal principal){
        List<CommentResponseDto> list = commentService.getAllCommentsByArticle(articleId);

        if(principal!=null){
            Member member = memberService.findByEmail(principal.getName());
            return new ResultWithAuthor(list,member.getNickname());
        }else{
            return new ResultWithAuthor(list,null);
        }
    }

    @PostMapping("save/comment")
    public Result saveComment(@RequestBody CommentRequestDto dto, Principal principal){
        Long id = commentService.createComment(dto, principal.getName());
        return new Result(id);
    }

    @Data
    @AllArgsConstructor
    static public class Result<T>{
        private T data;
    }

    @Data
    @AllArgsConstructor
    static public class ResultWithAuthor<T>{
        private T data;
        private T nickname;
    }
}
