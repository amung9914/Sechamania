package com.blog.controller;

import com.blog.dto.ArticleListDto;
import com.blog.service.ArticleService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ArticleApiController {

    private final ArticleService articleService;

    @GetMapping("api/articleList")
    public Page<ArticleListDto> getArticleList(){
        return articleService.findAllWithPage(0);
    }

    @GetMapping("api/articleList/{page}")
    public Page<ArticleListDto> getPage(@PathVariable int page){
        return articleService.findAllWithPage(page);
    }

    @Data
    @AllArgsConstructor
    static class Result<T>{
        private T data;
    }


}
