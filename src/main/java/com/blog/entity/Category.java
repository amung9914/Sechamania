package com.blog.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseEntity{
    @Id @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    @Column(nullable = false)
    private String categoryName;

    public Category(String categoryName) {
        this.categoryName = categoryName;
    }
}
