package com.blog.config.oauth;

import com.blog.config.jwt.TokenProvider;
import com.blog.entity.Member;
import com.blog.entity.RefreshToken;
import com.blog.repository.MemberRepository;
import com.blog.repository.RefreshTokenRepository;
import com.blog.service.MemberService;
import com.blog.util.CookieUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.Duration;

// 인증 성공시 실행할 핸들러
@Component @Slf4j
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    public static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(3);
    public static final Duration ACCESS_TOKEN_DURATION = Duration.ofHours(2);
    public static final String REDIRECT_PATH = "/signup/oauth2";

    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final OAuth2AuthorizationRequestBasedOnCookieRepository authorizationRequestRepository;
    private final MemberRepository memberRepository;

    @Override // 토큰 관련 작업만 추가로 처리하려고 상속 후 오버라이드 함.
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User= (OAuth2User) authentication.getPrincipal();
        String s = (String) oAuth2User.getAttributes().get("email");
        Member member = memberRepository.findByEmail(s)
                .orElseThrow(()-> new IllegalArgumentException("member를 찾을 수 없습니다."));
        // 액세스 토큰 생성 -> 쿼리 파라미터에 액세스 토큰 추가
        String accessToken = tokenProvider.generateToken(member, ACCESS_TOKEN_DURATION);
        // 리프레시 토큰 생성 -> 저장 -> 쿠키에 저장
        String refreshToken = tokenProvider.generateToken(member,REFRESH_TOKEN_DURATION);
        saveRefreshToken(member.getId(),accessToken,refreshToken);
        String targetUrl = getTargetUrl(accessToken,member);
        log.info("로그인에 성공하였습니다. 이메일 : {}", member.getEmail());
        log.info("로그인에 성공하였습니다. AccessToken : {}", accessToken);
        // 세션과 쿠키에 임시로 저장해둔 인증 관련 설정값, 쿠키 제거
        clearAuthenticationAttributes(request,response);
        // 리다이렉트
        getRedirectStrategy().sendRedirect(request,response,targetUrl);

    }

    /**
     * 생성된 리프레시 토큰을 REDIS에 저장
     */
    public void saveRefreshToken(long memberId,String accessToken,String newRefreshToken){
        RefreshToken refreshToken = refreshTokenRepository.findByAccessToken(accessToken)
                .map(entity -> entity.update(newRefreshToken))
                .orElse(new RefreshToken(memberId,newRefreshToken, accessToken));
        refreshTokenRepository.save(refreshToken);
    }

    /**
     * 세션과 쿠키에 임시로 저장해둔 인증 관련 설정값, 쿠키 제거
     */
    private void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response){
        super.clearAuthenticationAttributes(request);
        //OAUth 인증을 위해 저장된 쿠키정보 삭제
        authorizationRequestRepository.removeAuthorizationRequestCookies(request,response);
    }

    /**
     * 액세스 토큰을 리다이렉트 경로 패스에 추가.
     *  ex. http://localhost:8080/?token=123456789
     */
    private String getTargetUrl(String token,Member member){
        if(member.getAddress()==null){
            return UriComponentsBuilder.fromUriString(REDIRECT_PATH)
                    .queryParam("token",token)
                    .build()
                    .toUriString();
        }else{
            return UriComponentsBuilder.fromUriString("/")
                    .queryParam("token",token)
                    .build()
                    .toUriString();
        }

    }

}
