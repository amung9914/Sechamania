package com.blog.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class RefreshToken {
    @Id @GeneratedValue
    @Column(updatable = false)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long memberId;

    @Column(nullable = false)
    private String refreshToken;

    public RefreshToken(Long memberId, String refreshToken) {
        this.memberId = memberId;
        this.refreshToken = refreshToken;
    }

    public RefreshToken update(String newRefreshToken){
        this.refreshToken = newRefreshToken;
        return this;
    }

}
