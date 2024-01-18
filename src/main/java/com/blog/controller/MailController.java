package com.blog.controller;

import com.blog.service.MailService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;
    @PostMapping("/mail")
    public Result<String> mailSend(@RequestBody RequestMailDto request){

        String number = mailService.sendMail(request.mail);
        return new Result(number);
    }

    @Data
    @AllArgsConstructor
    static class Result<T>{
        private T data;
    }

    @Data
    static class RequestMailDto{
        String mail;
    }

}
