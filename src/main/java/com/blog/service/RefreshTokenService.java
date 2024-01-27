package com.blog.service;

import com.blog.entity.RefreshToken;
import com.blog.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void saveTokenInfo(Long userId,String refreshToken, String accessToken){
        refreshTokenRepository.save(new RefreshToken(userId,refreshToken,accessToken));
    }

    @Transactional
    public void removeRefreshToken(String accessToken){
        refreshTokenRepository.findByAccessToken(accessToken)
                .ifPresent(refreshToken -> refreshTokenRepository.delete(refreshToken));
    }

}
