package com.blog.config.jwt;

import com.blog.entity.Authorities;
import com.blog.entity.Member;
import com.blog.repository.MemberRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;
    public static final Duration ACCESS_TOKEN_DURATION = Duration.ofDays(1);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(()-> new IllegalArgumentException("일치하는 member가 존재하지 않습니다."));
        String accessToken = tokenProvider.generateToken(member, ACCESS_TOKEN_DURATION);
        String targetUrl = getTargetUrl(accessToken,authentication);
        tokenProvider.sendAccessToken(response,accessToken);
        if(member.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))){
            tokenProvider.sendIsAdmin(response);
        }
        log.info("로그인에 성공하였습니다. 이메일 : {}", email);
        log.info("로그인에 성공하였습니다. AccessToken : {}", accessToken);

    }

    /**
     * 액세스 토큰을 리다이렉트 경로 패스에 추가.
     *  ex. http://localhost:8080/?token=123456789
     */
    private String getTargetUrl(String token,Authentication authentication){
        if(authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))){
            return UriComponentsBuilder.fromUriString("/")
                    .queryParam("token",token)
                    .queryParam("admin","true")
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
