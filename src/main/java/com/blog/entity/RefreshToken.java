package com.blog.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;



@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@RedisHash(value = "jwtToken", timeToLive = 60*60*24*3)
public class RefreshToken {
    @Id
    private Long id;

    @Column(nullable = false)
    private String refreshToken;

    @Indexed // Redis에서 해당 값으로 데이터를 찾는다
    private String accessToken;

    public RefreshToken(Long id, String refreshToken, String accessToken) {
        this.id = id;
        this.refreshToken = refreshToken;
        this.accessToken = accessToken;
    }

    public RefreshToken update(String newRefreshToken){
        this.refreshToken = newRefreshToken;
        return this;
    }

}
