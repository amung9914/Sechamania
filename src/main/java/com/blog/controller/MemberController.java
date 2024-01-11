package com.blog.controller;

import com.blog.util.ImgUploader;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final ImgUploader imgUploader;

    @PostMapping("/test")
    public void img(@RequestParam MultipartFile img){
        imgUploader.savdImg(img);
    }
}
