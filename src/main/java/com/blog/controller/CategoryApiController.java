package com.blog.controller;

import com.blog.dto.CompanyListDto;
import com.blog.entity.Category;
import com.blog.service.CategoryService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("admin/category")
    public Result update(@RequestBody Categorydto dto){
        categoryService.modify(dto.getCategoryId(),dto.getName());
        return new Result(true);
    }

    @DeleteMapping("admin/category")
    public Result delete(@RequestBody Categorydto dto){
        categoryService.delete(dto.getCategoryId());
        return new Result(true);
    }

    @PostMapping("admin/saveCategory")
    public Result save(@RequestBody Categorydto dto){
        Long id = categoryService.save(dto.getName());
        return new Result(id);
    }

    @Data
    @AllArgsConstructor
    static class Result<T>{
        private T data;
    }


    @Data
    static private class Categorydto {
        private long categoryId;
        private String name;

        public Categorydto(long categoryId, String name) {
            this.categoryId = categoryId;
            this.name = name;
        }

        public Categorydto(Category category) {
            this.categoryId = category.getId();
            this.name = category.getName();
        }
    }
}
