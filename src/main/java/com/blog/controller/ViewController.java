package com.blog.controller;

import com.blog.entity.Referer;
import com.blog.repository.RefererRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ViewController {
    private final RefererRepository refererRepository;
    @GetMapping("/")
    public String root(){
        return "home";
    }

    @GetMapping("/test")
    public String imgtest(){
        return "/test/img";
    }

    @GetMapping("/login")
    public String login(){
        return "/user/login";
    }

    @GetMapping("/login/error")
    public String loginError(Model model){
        model.addAttribute("loginErrorMsg","아이디 또는 비밀번호를 확인해주세요");
        return "/user/login";
    }

    @GetMapping("/signup")
    public String signup(){
        return "/user/signup";
    }

    @GetMapping("/signup/oauth2")
    public String signupByOauth2(){
        return "/user/oauthSignup";
    }

    @GetMapping("/view/mypage")
    public String mypage(){
        return "/user/mypage";
    }

    @GetMapping("/view/article")
    public String articleMain(){
        return "/article/main";
    }

    @GetMapping("/view/updateArticle/{id}")
    public String articleUpdate(){
        return "/article/update";
    }

    @GetMapping("/view/writeArticle")
    public String articleWrite(){
        return"/article/write";
    }

    @GetMapping("/view/article/{id}")
    public String articleView(){
        return "/article/view";
    }

    @GetMapping("/view/bookmark")
    public String bookmarkList(){
        return "/user/bookmark";
    }

    @GetMapping("/view/myArticle")
    public String myArticleList(){
        return "/user/myArticle";
    }

    @GetMapping("/view/notice")
    public String noticeList(){
        return "/article/notice";
    }

    @GetMapping("/view/admin")
    public String adminMain(){
        return "/admin/main";
    }

    @GetMapping("/view/admin/category")
    public String adminCategory(){
        return "/admin/category";
    }
}
