package com.blog.controller;

import com.blog.util.ImgUploader;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final ImgUploader imgUploader;
    @GetMapping("/test/1") // 권환 확인용
    public Collection<? extends GrantedAuthority> check(UserDetails userDetails){
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities();
    }
   /*
   public void img(@RequestParam MultipartFile img){

        imgUploader.savdImg(img);
    }
     */
}
