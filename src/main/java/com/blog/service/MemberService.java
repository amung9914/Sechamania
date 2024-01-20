package com.blog.service;

import com.blog.dto.AddUserRequest;
import com.blog.dto.OauthSignupRequest;
import com.blog.dto.UpdateMemberDto;
import com.blog.entity.Authorities;
import com.blog.entity.Member;
import com.blog.entity.MemberStatus;
import com.blog.repository.AuthorityRepository;
import com.blog.repository.MemberRepository;
import com.blog.util.ImgUploader;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final AuthorityRepository authorityRepository;
    private final BCryptPasswordEncoder encoder;

    private final ImgUploader imgUploader;

    @Transactional
    public Long join(AddUserRequest dto, MultipartFile file){

        String path = null;
        if(file.isEmpty()){
            path = "img/defaultProfile.jpg";
        }else{
            path = imgUploader.savdImg(file);
        }

        Member newMember = Member.builder()
                .email(dto.getEmail())
                .password(encoder.encode(dto.getPassword()))
                .nickname(dto.getNickname())
                .address(dto.getAddress())
                .status(MemberStatus.ACTIVE)
                .imgPath(path==null?"/img/defaultProfile.jpg":path)
                .build();
        memberRepository.save(newMember);

        Authorities auth = new Authorities("ROLE_USER");
        authorityRepository.save(auth);
        auth.makeRole(newMember);

        return newMember.getId();
    }

    @Transactional
    public Long joinForCompany(AddUserRequest dto,MultipartFile file){
        String path = null;
        if(file.isEmpty()){
            path = "img/defaultProfile.jpg";
        }else{
            path = imgUploader.savdImg(file);
        }

        Member newMember = Member.builder()
                .email(dto.getEmail())
                .password(encoder.encode(dto.getPassword()))
                .nickname(dto.getNickname())
                .address(dto.getAddress())
                .status(MemberStatus.ACTIVE)
                .imgPath(path==null?"/img/defaultProfile.jpg":path)
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

    @Transactional
    public void joinForOauth(OauthSignupRequest request, String email,MultipartFile file) {

        String path = null;
        if(file.isEmpty()){
            path = "img/defaultProfile.jpg";
        }else{
            path = imgUploader.savdImg(file);
        }
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 email입니다"));
        member.updateNickname(request.getNickname());
        member.updateAddress(request.getAddress());
        member.updateProfileImg(path);
    }

    @Transactional
    public void updateMember(String email, UpdateMemberDto dto,MultipartFile file){


        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 email입니다"));
        if(!member.getNickname().equals(dto.getNickname())){
            validateDuplicateNickname(dto.getNickname());
            member.updateNickname(dto.getNickname());
        }
        if(dto.getAddress()!=null){
            member.updateAddress(dto.getAddress());
        }
        if(file!=null){

            String path = null;
            path = imgUploader.savdImg(file);

            member.updateProfileImg(path);
        }
    }

    @Transactional
    public void updatePassword(String email, String password,String newpassword){
        Member member = memberRepository.findByEmail(email)
                        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 email입니다."));
        // 비교할 요청값과 실제 암호화된 암호 비교
        if(encoder.matches(password, member.getPassword())){
            member.updatePassword(encoder.encode(newpassword));
        }else{
            throw new IllegalStateException("비밀번호가 일치하지 않습니다.");
        }
    }
}
