package com.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {
    @GetMapping("/")
    public String root(){ return "home";}

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

    @GetMapping("/view/admin")
    public String admin(){
        return "/admin/admin";
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
}
