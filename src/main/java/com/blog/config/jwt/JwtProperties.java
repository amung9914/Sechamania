package com.blog.config.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter @Setter
@Component @ConfigurationProperties("jwt") // 프로퍼티값을 가져온다
public class JwtProperties {
    private String issuer;
    private String secretKey;
}
