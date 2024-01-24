package com.blog.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Referer extends BaseTimeEntity{

    @Id @GeneratedValue
    Long id;
    String path; // 유입경로

    public Referer(String path) {
        this.path = path;
    }
    // path가 null인 경우 직접 접속
}
