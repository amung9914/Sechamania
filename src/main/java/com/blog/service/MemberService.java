package com.blog.service;

import com.blog.dto.AddUserRequest;
import com.blog.entity.Authorities;
import com.blog.entity.Member;
import com.blog.repository.AuthorityRepository;
import com.blog.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final AuthorityRepository authorityRepository;
    private final BCryptPasswordEncoder encoder;

    @Transactional
    public Long join(AddUserRequest dto){
        validateDuplacateEmail(dto);
        validateDuplicateNickname(dto);
        Member newMember = Member.builder()
                .email(dto.getEmail())
                .password(encoder.encode(dto.getPassword()))
                .nickname(dto.getNickname())
                .address(dto.getAddress())
                .build();
        memberRepository.save(newMember);

        Authorities auth = new Authorities("USER");
        authorityRepository.save(auth);
        auth.makeRole(newMember);

        return newMember.getId();
    }

    /**
     * 중복 가입 검증
     */
    private void validateDuplacateEmail(AddUserRequest dto){
        Optional<Member> byEmail = memberRepository.findByEmail(dto.getEmail());
        if(byEmail.isPresent()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    private void validateDuplicateNickname(AddUserRequest dto){
        Optional<Member> findMember = memberRepository.findByNickname(dto.getNickname());
        if(findMember.isPresent()){
            throw new IllegalStateException("사용중인 닉네임입니다.");
        }
    }
}
