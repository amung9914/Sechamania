package com.blog.config;

import com.blog.config.jwt.CustomJsonUsernamePasswordAuthenticationFilter;
import com.blog.config.jwt.LoginFailureHandler;
import com.blog.config.jwt.LoginSuccessHandler;
import com.blog.config.jwt.TokenProvider;
import com.blog.config.oauth.OAuth2AuthorizationRequestBasedOnCookieRepository;
import com.blog.config.oauth.OAuth2SuccessHandler;
import com.blog.config.oauth.OAuth2UserCustomService;
import com.blog.repository.MemberRepository;
import com.blog.repository.RefreshTokenRepository;
import com.blog.service.MemberService;
import com.blog.service.UserDetailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.cj.protocol.a.authentication.Sha256PasswordPlugin;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final OAuth2UserCustomService oAuth2UserCustomService;
    private final UserDetailService userDetailService;
    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final ObjectMapper objectMapper;
    private final String[] RESOURCE = {"/css/**","/js/**","/img/**","/tempImg/**"};

    @Bean
    public WebSecurityCustomizer configure(){
        return (web) -> web.ignoring()
                .requestMatchers("https://s3.ap-northeast-2.amazonaws.com/library9914/*");

    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.csrf(csrf -> csrf.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(formLogin -> formLogin.disable());
        http.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        //헤더를 확인할 커스텀 필터를 추가해준다

                http.authorizeHttpRequests(request -> request
                        .requestMatchers("/error","/error/**","/","/login","/login/**",
                                "/mail","/test/**","/signup","/signup/**","/view/**","/tempimg/**"
                        ,"api/**").permitAll()
                        .requestMatchers(RESOURCE).permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login"));

        http.oauth2Login(request -> request
                .loginPage("/login")
                .authorizationEndpoint()
                .authorizationRequestRepository(oAuth2AuthorizationRequestBasedOnCookieRepository())
                .and()
                .successHandler(oAuth2SccessHandler())
                .userInfoEndpoint()
                .userService(oAuth2UserCustomService));

        // /api로 시작하는 url인 경우 401 상태코드 반환
        http.exceptionHandling(e -> e
                .defaultAuthenticationEntryPointFor(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                        new AntPathRequestMatcher("/api/**")));
        // 원래 스프링 시큐리티 필터 순서가 LogoutFilter 이후에 로그인 필터 동작
        // 따라서, LogoutFilter 이후에 우리가 만든 필터 동작하도록 설정
        // 순서 : LogoutFilter -> JwtAuthenticationProcessingFilter -> CustomJsonUsernamePasswordAuthenticationFilter
        http.addFilterAfter(customJsonUsernamePasswordAuthenticationFilter(), LogoutFilter.class);
        http.addFilterBefore(tokenAuthenticationFilter(), CustomJsonUsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter(){
        return new TokenAuthenticationFilter(tokenProvider);
    }

    @Bean
    // PBKDF2 암호화 적용
    public Pbkdf2PasswordEncoder pbkdf2PasswordEncoder(){
        return new Pbkdf2PasswordEncoder("secret9914",50,10000,Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256);
        // 비밀키(secret), salt의 길이(saltLength), 반복 횟수(iterations), 그리고 SecretKeyFactoryAlgorithm
    }

    @Bean
    public AuthenticationManager authenticationManager(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(pbkdf2PasswordEncoder());
        provider.setUserDetailsService(userDetailService);
        return new ProviderManager(provider);
    }

    @Bean
    public LoginSuccessHandler loginSuccessHandler(){
        return new LoginSuccessHandler(tokenProvider,refreshTokenRepository,memberRepository);
    }

    @Bean
    public LoginFailureHandler loginFailureHandler(){
        return new LoginFailureHandler();
    }

    @Bean
    public CustomJsonUsernamePasswordAuthenticationFilter customJsonUsernamePasswordAuthenticationFilter(){
        CustomJsonUsernamePasswordAuthenticationFilter customJsonUsernamePasswordAuthenticationFilter
                = new CustomJsonUsernamePasswordAuthenticationFilter(objectMapper);
        customJsonUsernamePasswordAuthenticationFilter.setAuthenticationManager(authenticationManager());
        customJsonUsernamePasswordAuthenticationFilter.setAuthenticationSuccessHandler(loginSuccessHandler());
        customJsonUsernamePasswordAuthenticationFilter.setAuthenticationFailureHandler(loginFailureHandler());
        return customJsonUsernamePasswordAuthenticationFilter;
    }

    @Bean
    public OAuth2SuccessHandler oAuth2SccessHandler(){
        return new OAuth2SuccessHandler(tokenProvider,
                refreshTokenRepository,
                oAuth2AuthorizationRequestBasedOnCookieRepository(),
                memberRepository);
    }

    @Bean
    public OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository(){
        return new OAuth2AuthorizationRequestBasedOnCookieRepository();
    }

}
