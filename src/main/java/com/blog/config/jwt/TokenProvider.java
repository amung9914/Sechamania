package com.blog.config.jwt;

import com.blog.entity.Member;
import com.blog.repository.MemberRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;

@Slf4j
@Service @RequiredArgsConstructor
public class TokenProvider {

    private final JwtProperties jwtProperties;
    private final MemberRepository memberRepository;
    private final static String HEADER_AUTHORIZATION = "token";

    public String generateToken(Member member, Duration expiredAt){
        Date now = new Date();
        return makeToken(new Date(now.getTime() + expiredAt.toMillis()),member);
    }

    // JWT 토큰 생성 메서드
    private String makeToken(Date expiry, Member member) {
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .setSubject(member.getEmail()) // 토큰제목 : 유저의 이메일
                .claim("id",member.getId())
                // 서명 : 비밀값과 함께 해시값을 암호화
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();
    }

    //토큰 유효한지 검증하는 메서드
    public boolean validToken(String token){
        try{
            Jwts.parser()
                    .setSigningKey(jwtProperties.getSecretKey()) //비밀키로 복호화
                    .parseClaimsJws(token);
            return true;
        }catch (Exception e){ //복호화시 에러 발생 -> 유효하지 않음
            return false;
        }
    }

    // 클레임 조회
    private Claims getClaims(String token){
        return Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody();
    }

    //토큰 기반으로 인증 정보 가져오는 메서드
    public Authentication getAuthentication(String token){
        Claims claims = getClaims(token);
        Member member = memberRepository.findByEmail(claims.getSubject())
                .orElseThrow(()-> new IllegalArgumentException("not found member:"+claims.getSubject()));
        Collection<? extends GrantedAuthority> authorities = member.getAuthorities();

        return new UsernamePasswordAuthenticationToken(new org.springframework.security.core.userdetails.User(claims.getSubject(), "",authorities)
                ,token,authorities);
    }

    // 토큰 기반으로 유저 ID를 가져오는 메서드
    public Long getUserId(String token){
        Claims claims = getClaims(token);
        return claims.get("id", Long.class);
    }

    /**
     * AccessToken을 헤더에 실어 보냄
     */
    public void sendAccessToken(HttpServletResponse response,String accessToken){
        response.setStatus(HttpServletResponse.SC_OK);
        setAccessTokenHeader(response,accessToken);
        log.info("AccessToken 헤더 설정 완료");
    }

    private void setAccessTokenHeader(HttpServletResponse response, String accessToken) {
        response.setHeader(HEADER_AUTHORIZATION, accessToken);
    }

    public void sendIsAdmin(HttpServletResponse response){
        response.setHeader("ADMIN","true");
        log.info("ADMIN 헤더 설정 완료");
    }

}
