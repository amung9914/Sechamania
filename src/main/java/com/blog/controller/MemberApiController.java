package com.blog.controller;

import com.blog.dto.AddUserRequest;
import com.blog.dto.OauthSignupRequest;
import com.blog.dto.UpdateMemberDto;
import com.blog.entity.Address;
import com.blog.entity.Member;
import com.blog.service.MemberService;
import com.blog.util.ImgUploader;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public Result<Long> createMember(@ModelAttribute @Valid AddUserRequest request,
                                     @RequestParam MultipartFile file){
        Long id = memberService.join(request, file);
        return new Result(id);
    }

    @PostMapping("/oauthSignup")
    public Result<String> createMemberforOauth(@ModelAttribute @Valid OauthSignupRequest request,
                                             @RequestParam MultipartFile file, Principal principal){
        memberService.joinForOauth(request,principal.getName(),file);
        return new Result(request.getNickname());
    }

    /**
     * 아이디 중복 검증
     */
    @GetMapping("/signup/email/{email}")
    public Result<Boolean> isCheckForEmail(@PathVariable String email){
        memberService.validateDuplacateEmail(email);
        return new Result(true);
    }

    /**
     * 닉네임 중복 검증
     */
    @GetMapping("/signup/nickname/{nickname}")
    public Result<Boolean> isCheckForNickname(@PathVariable String nickname){
        memberService.validateDuplicateNickname(nickname);
        return new Result(true);
    }

    /**
     * 기본정보 수정에 필요한 정보 제공
     */
    @GetMapping("/updateMember")
    public Result<UpdateMemberDto> findMemberData(Principal principal){
        Member member = memberService.findByEmail(principal.getName());
        return new Result(new updateInfoDto(member));
    }

    /**
     * 기본정보 수정
     */
    @PostMapping("/updateMember")
    public Result<Boolean> updateMemberData(Principal principal, @ModelAttribute UpdateMemberDto dto,
                                            @RequestParam MultipartFile file){

        memberService.updateMember(principal.getName(),dto,file);
        return new Result(true);
    }

    /**
     * 패스워드 수정
     */
    @PostMapping("/password")
    public Result<Boolean> update(Principal principal, @RequestBody passwordDto dto){
        memberService.updatePassword(principal.getName(), dto.getPassword(), dto.getNewPassword());
        return new Result(true);
    }

    @Data
    @AllArgsConstructor
    static class Result<T>{
        private T data;
    }

    @Data
    @AllArgsConstructor
    static private class passwordDto {
        String password;
        String newPassword;

    }

    @Data
    static private class updateInfoDto {
        String nickname;
        String fullAddress;
        String imgPath;

        public updateInfoDto(Member member) {
            this.nickname = member.getNickname();
            this.fullAddress = member.getAddress().getFullAddress();
            this.imgPath = member.getProfileImg();
        }
    }


}
