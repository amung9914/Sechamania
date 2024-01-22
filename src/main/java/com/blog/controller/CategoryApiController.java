package com.blog.controller;

import com.blog.dto.CompanyListDto;
import com.blog.entity.Category;
import com.blog.service.CategoryService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CategoryApiController {

    private final CategoryService categoryService;

    @GetMapping("api/category")
    public Result<Categorydto> getCategory(){
        List<Category> list = categoryService.findAll();
        return new Result(list.stream().map(category -> new Categorydto(category)).toList());
    }

    @Data
    @AllArgsConstructor
    static class Result<T>{
        private T data;
    }


    @Data
    private class Categorydto {
        private Long categoryId;
        private String name;

        public Categorydto(Category category) {
            this.categoryId = category.getId();
            this.name = category.getName();
        }
    }
}
