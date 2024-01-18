package com.blog.service;


import com.blog.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor // 스프링 시큐리티 관련 인터페이스 구현
public class UserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;
    @Override // 사용자 정보를 가져온다
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return memberRepository.findByEmail(email)
                .orElseThrow(()-> new IllegalArgumentException(email));
    }
}