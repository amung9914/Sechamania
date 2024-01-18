package com.blog.config;

import com.blog.config.jwt.TokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// 액세스 토큰값이 담긴 Authorization 헤더값을 가져오고, 토근 유효하면 인증 정보 설정함
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final static String HEADER_AUTHORIZATION = "Authorization";
    private final static String TOKEN_PREFIX = "Bearer ";


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 요청header의 Authorization 키 값 조회
        String authorizationHeader = request.getHeader(HEADER_AUTHORIZATION);
        String token = getAccessToken(authorizationHeader); // 실 토큰값으로 만듦
        // 가져온 토큰이 유효한 경우 인증 정보를 설정함
        if(tokenProvider.validToken(token)){
            Authentication authentication = tokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request,response);
    }

    // 키값에서 접두사를 제거합니다.(Bearer )
    private String getAccessToken(String authorizationHeader){
        if(authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PREFIX)){
            //"Bearer "이후값부터 리턴함. null인경우 || 지정된값으로 시작하지 않는경우 null리턴
            return authorizationHeader.substring(TOKEN_PREFIX.length());
        }
        return null;
    }


    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
