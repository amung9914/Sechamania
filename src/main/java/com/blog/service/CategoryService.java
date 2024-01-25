package com.blog.service;

import com.blog.entity.Category;
import com.blog.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public Long save(String name){
        Optional<Category> findCategory = categoryRepository.findCategoryByName(name);
        if(findCategory.isPresent()){
            throw new IllegalArgumentException("이미 사용중인 카테고리입니다");
        }
        return categoryRepository.save(new Category(name)).getId();
    }

    public List<Category> findAll(){
        return categoryRepository.findAll(Sort.by(Sort.Direction.ASC,"name"));
    }

    @Transactional
    public void modify(long id, String name){
        Category category = categoryRepository.findById(id)
                        .orElseThrow(()-> new IllegalArgumentException("not found: "+ id));
        category.updateName(name);
    }

    @Transactional
    public void delete(long id){
        Category category = categoryRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("not found: "+ id));
        categoryRepository.delete(category);
    }

    public Category findByName(String name){
        return categoryRepository.findCategoryByName(name)
                .orElseThrow(()-> new IllegalArgumentException("not found:"+name));

    }

}
