package com.blog.config.jwt;

import com.blog.entity.Authorities;
import com.blog.entity.Member;
import com.blog.entity.RefreshToken;
import com.blog.repository.MemberRepository;
import com.blog.repository.RefreshTokenRepository;
import com.blog.util.CookieUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;
    public static final Duration ACCESS_TOKEN_DURATION = Duration.ofHours(2);
    public static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(3);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(()-> new IllegalArgumentException("일치하는 member가 존재하지 않습니다."));
        String accessToken = tokenProvider.generateToken(member, ACCESS_TOKEN_DURATION);
        tokenProvider.sendAccessToken(response,accessToken);
        if(member.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))){
            tokenProvider.sendIsAdmin(response);
        }
        String refreshToken = tokenProvider.generateToken(member,REFRESH_TOKEN_DURATION);
        saveRefreshToken(member.getId(), accessToken, refreshToken);

        log.info("로그인에 성공하였습니다. 이메일 : {}", email);
        log.info("로그인에 성공하였습니다. AccessToken : {}", accessToken);

    }

    /**
     * 생성된 리프레시 토큰을 REDIS에 저장
     */
    public void saveRefreshToken(long memberId,String accessToken, String newRefreshToken) {
        RefreshToken refreshToken = refreshTokenRepository.findByAccessToken(accessToken)
                .map(entity -> entity.update(newRefreshToken))
                .orElse(new RefreshToken(memberId,newRefreshToken, accessToken));
        refreshTokenRepository.save(refreshToken);
    }
}
