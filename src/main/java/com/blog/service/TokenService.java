package com.blog.service;

import com.blog.config.jwt.TokenProvider;
import com.blog.entity.Member;
import com.blog.entity.RefreshToken;
import com.blog.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class TokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenProvider tokenProvider;
    private final MemberService memberService;

    public String createNewAccessToken(String accessToken){
        RefreshToken foundTokenInfo = refreshTokenRepository.findByAccessToken(accessToken)
                .orElseThrow(()-> new IllegalArgumentException("일치하는 refreshToken을 찾을 수 없습니다"));
        String refreshToken = foundTokenInfo.getRefreshToken();
        // 리프레시 토큰 유효성 검사 진행
        if(!tokenProvider.validToken(refreshToken)){
            throw new IllegalArgumentException("이 토큰은 유효하지 않습니다.");
        }

         // 유효하면 이 토큰으로 사용자id 검색
        Long memberId = foundTokenInfo.getId();
        Member member = memberService.findById(memberId);
        // 새로운 액세스 토큰 생성
        String newAccessToken = tokenProvider.generateToken(member, Duration.ofHours(2));

        // 새로 발급한 accessToken으로 Redis 업데이트
        refreshTokenRepository.save(new RefreshToken(memberId,refreshToken,newAccessToken));
        return newAccessToken;
    }

    public void deleteRefreshToken(String accessToken){
        RefreshToken refreshToken = refreshTokenRepository.findByAccessToken(accessToken)
                .orElseThrow(() -> new IllegalArgumentException("일치하는 refreshToken을 찾을 수 없습니다"));
        refreshTokenRepository.delete(refreshToken);
    }
}
