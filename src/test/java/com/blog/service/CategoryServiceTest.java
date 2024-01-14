package com.blog.service;

import com.blog.entity.Category;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest@Transactional
class CategoryServiceTest {

    @Autowired CategoryService categoryService;
    @PersistenceContext
    EntityManager em;

    @Test
    public void save() throws Exception {
        // given

        Category category = new Category("name1");
        categoryService.save(category);
        // when
        em.flush();
        em.clear();

        List<Category> all = categoryService.findAll();

        // then
        Assertions.assertThat(all.get(0).getCategoryName()).isEqualTo(category.getCategoryName());
    }

    @Test
    public void list() throws Exception {
        // given

        Category category1 = new Category("name1");
        Category category2 = new Category("name2");
        Category category3 = new Category("name3");
        categoryService.save(category1);
        categoryService.save(category2);
        categoryService.save(category3);
        // when
        em.flush();
        em.clear();

        List<Category> all = categoryService.findAll();

        // then
        Assertions.assertThat(all.size()).isEqualTo(3);
    }

    @Test
    public void modify() throws Exception {
        // given

        Category category = new Category("name1");
        categoryService.save(category);
        // when
        em.flush();
        em.clear();

        List<Category> all = categoryService.findAll();
        Category findCategory = all.get(0);
        categoryService.modify(findCategory.getId(), "newName");
        em.flush();
        em.clear();

        List<Category> list = categoryService.findAll();

        // then
        Assertions.assertThat(list.get(0).getCategoryName()).isEqualTo("newName");
    }

    @Test
    public void delete() throws Exception {
        // given

        Category category1 = new Category("name1");
        Category category2 = new Category("name2");
        Category category3 = new Category("name3");
        categoryService.save(category1);
        categoryService.save(category2);
        categoryService.save(category3);
        // when
        em.flush();
        em.clear();

        Long id = null;

        List<Category> all = categoryService.findAll();
        for (Category category : all) {
            if(category.getCategoryName().equals("name2")){
                id = category.getId();
            }
        }

        categoryService.delete(id);
        em.flush();
        em.clear();
        List<Category> list = categoryService.findAll();

        // then
        Assertions.assertThat(list.size()).isEqualTo(2);
    }

}