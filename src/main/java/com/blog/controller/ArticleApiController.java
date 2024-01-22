package com.blog.controller;

import com.blog.dto.ArticleListDto;
import com.blog.dto.HashtagDto;
import com.blog.service.ArticleService;
import com.blog.util.ImgUploader;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class ArticleApiController {

    private final ImgUploader imgUploader;
    private final ArticleService articleService;

    @GetMapping("api/articleList")
    public Page<ArticleListDto> getArticleList(){
        return articleService.findAllWithPage(0);
    }

    @GetMapping("api/articleList/{page}")
    public Page<ArticleListDto> getPage(@PathVariable int page){
        return articleService.findAllWithPage(page);
    }

    @PostMapping("save/articleImage")
    public Result<String> uploadImage(@RequestParam MultipartFile file){
        System.out.println(file);
        return new Result("/img/defaultProfile.jpg");
    }

    @PostMapping("save/article")
    public Result<Boolean> saveArticle(@RequestBody HashtagDto dto, Principal principal){
        articleService.saveWithHashtag(principal.getName(), dto.getAddArticleDto(), dto.getHashtags());
        return new Result(true);
    }

    @Data
    @AllArgsConstructor
    static class Result<T>{
        private T data;
    }


}
