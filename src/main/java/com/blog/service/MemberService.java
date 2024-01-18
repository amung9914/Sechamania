package com.blog.service;

import com.blog.dto.AddUserRequest;
import com.blog.entity.Authorities;
import com.blog.entity.Member;
import com.blog.entity.MemberStatus;
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
    public Long join(AddUserRequest dto,String imgPath){
        Member newMember = Member.builder()
                .email(dto.getEmail())
                .password(encoder.encode(dto.getPassword()))
                .nickname(dto.getNickname())
                .address(dto.getAddress())
                .status(MemberStatus.ACTIVE)
                .imgPath(imgPath==null?"/img/defaultProfile.jpg":imgPath)
                .build();
        memberRepository.save(newMember);

        Authorities auth = new Authorities("ROLE_USER");
        authorityRepository.save(auth);
        auth.makeRole(newMember);

        return newMember.getId();
    }

    @Transactional
    public Long joinForCompany(AddUserRequest dto,String imgPath){
        Member newMember = Member.builder()
                .email(dto.getEmail())
                .password(encoder.encode(dto.getPassword()))
                .nickname(dto.getNickname())
                .address(dto.getAddress())
                .status(MemberStatus.ACTIVE)
                .imgPath(imgPath==null?"/img/defaultProfile.jpg":imgPath)
                .build();
        memberRepository.save(newMember);

        Authorities userAuth = new Authorities("ROLE_USER");
        authorityRepository.save(userAuth);
        userAuth.makeRole(newMember);

        Authorities companyAuth = new Authorities("ROLE_COMPANY");
        authorityRepository.save(companyAuth);
        companyAuth.makeRole(newMember);

        return newMember.getId();
    }


    /**
     * 중복 가입 검증
     */
    public void validateDuplacateEmail(String name){
        Optional<Member> byEmail = memberRepository.findByEmail(name);
        if(byEmail.isPresent()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    /**
     * 닉네임 중복 검증
     */
    public void validateDuplicateNickname(String name){
        Optional<Member> findMember = memberRepository.findByNickname(name);
        if(findMember.isPresent()){
            throw new IllegalStateException("사용중인 닉네임입니다.");
        }
    }

    public Member findByEmail(String email){
        return memberRepository.findByEmail(email)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 email입니다"));
    }

}
