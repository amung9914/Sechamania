package com.blog.controller;

import com.blog.dto.HashtagDto;
import com.blog.entity.Hashtag;
import com.blog.service.HashtagService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class HashtagApiController {

    private final HashtagService hashtagService;

    @GetMapping("api/hashtag")
    public Result<HashtagListDto> getHashtag(){
        List<Hashtag> list = hashtagService.findAll();
        return new Result(list.stream().map(hashtag -> new HashtagListDto(hashtag)).toList());
    }

    @Data
    @AllArgsConstructor
    static class Result<T>{
        private T data;
    }

    @Data
    private class HashtagListDto {
        private Long hashtagId;
        private String name;

        public HashtagListDto(Hashtag hashtag) {
            this.hashtagId = hashtag.getId();
            this.name = hashtag.getName();
        }
    }
}
