package com.blog.controller;

import com.blog.dto.CommentRequestDto;
import com.blog.dto.CommentResponseDto;
import com.blog.service.CommentService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentApiController {

    private final CommentService commentService;

    @GetMapping("/api/comment/{articleId}")
    public Result findComment(@PathVariable long articleId){
        List<CommentResponseDto> list = commentService.getAllCommentsByArticle(articleId);
        return new Result(list);
    }

    @PostMapping("save/comment")
    public Result saveComment(CommentRequestDto dto, Principal principal){
        Long id = commentService.createComment(dto, principal.getName());
        return new Result(id);
    }

    @Data
    @AllArgsConstructor
    static public class Result<T>{
        private T data;
    }
}
