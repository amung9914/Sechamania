package com.blog.controller;

import com.blog.service.BookmarkService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class BookmarkApiController {
    private final BookmarkService bookmarkService;

    @PostMapping("bookmark/{articleId}")
    public Result save(@PathVariable long articleId, Principal principal){
        bookmarkService.save(principal.getName(), articleId);
        return new Result(true);
    }

    @DeleteMapping("bookmark/{articleId}")
    public Result delete(@PathVariable long articleId, Principal principal){
        bookmarkService.delete(principal.getName(), articleId);
        return new Result(true);
    }

    @Data
    @AllArgsConstructor
    static public class Result<T>{
        private T data;
    }

}
