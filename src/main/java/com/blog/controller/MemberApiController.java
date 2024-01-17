package com.blog.controller;

import com.blog.dto.AddUserRequest;
import com.blog.entity.Address;
import com.blog.service.MemberService;
import com.blog.util.ImgUploader;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;
    private final ImgUploader imgUploader;

    @PostMapping("/signup")
    public Result<Long> createMember(@ModelAttribute @Valid AddUserRequest request,
                                     @RequestParam MultipartFile file){
        String path = null;
        if(file.isEmpty()){
            path = "img/defaultProfile.jpg";
        }else{
            path = imgUploader.savdImg(file);
        }


        Long id = memberService.join(request, path);
        return new Result(id);
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

    @Data
    @AllArgsConstructor
    static class Result<T>{
        private T data;
    }

}
