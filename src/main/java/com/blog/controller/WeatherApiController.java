package com.blog.controller;

import com.blog.entity.Member;
import com.blog.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class WeatherApiController {

    private final MemberService memberService;

    @GetMapping("/memberInfo")
    public Result<MemberAddrDto> find(Principal principal){
        String email = principal.getName();
        System.out.println("email = " + email);

        Member findMember = memberService.findByEmail(email);
        return new Result(new MemberAddrDto(findMember));
    }

    @Data
    @AllArgsConstructor
    static class Result<T>{
        private T data;
    }

    @Data
    private class MemberAddrDto {
        String nickname;
        String city;
        String lat;
        String lon;

        public MemberAddrDto(Member member) {
            this.nickname = member.getNickname();
            this.city = member.getAddress().getCity();
            this.lat = member.getAddress().getLat();
            this.lon = member.getAddress().getLon();
        }
    }
}
