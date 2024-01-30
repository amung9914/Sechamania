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
public class Hashtag extends BaseEntity{

    @Id @GeneratedValue
    @Column(name = "hashtag_id")
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    public Hashtag(String name) {
        this.name = name;
    }
}
