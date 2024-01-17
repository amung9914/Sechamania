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
        return "test/img";
    }

    @GetMapping("/login")
    public String login(){
        return "user/login";
    }

    @GetMapping("/login/error")
    public String loginError(Model model){
        model.addAttribute("loginErrorMsg","아이디 또는 비밀번호를 확인해주세요");
        return "user/login";
    }

    @GetMapping("/signup")
    public String signup(){
        return "user/signup";
    }
}
