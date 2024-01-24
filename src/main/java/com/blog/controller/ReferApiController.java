package com.blog.controller;

import com.blog.entity.Referer;
import com.blog.service.RefererService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReferApiController {

    private final RefererService refererService;

    @GetMapping("admin/referer")
    public Result<RefererDto> findAll(){
        List<Referer> list = refererService.findAll();
        return new Result(list.stream().map(referer -> new RefererDto(referer)).toList());
    }

    @Data
    @AllArgsConstructor
    static class Result<T>{
        private T data;
    }

    @Data
    static private class RefererDto {

        String path;
        LocalDateTime createdTime;

        public RefererDto(Referer referer) {
            this.path = referer.getPath();
            this.createdTime = referer.getCreatedDate();
        }
    }
}
