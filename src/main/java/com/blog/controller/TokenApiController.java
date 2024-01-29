package com.blog.controller;

import com.blog.dto.CreateAccessTokenDto;
import com.blog.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TokenApiController {
    private final TokenService tokenService;

    @PostMapping("/api/token")
    public ResponseEntity<CreateAccessTokenDto> createNewAccessToken
            (@RequestBody CreateAccessTokenDto request){
        String newAccessToken = tokenService.createNewAccessToken(request.getAccessToken());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CreateAccessTokenDto(newAccessToken));
    }

    @PostMapping("/api/delete/token")
    public HttpStatus deleteRefreshToken
            (@RequestBody CreateAccessTokenDto request){
        tokenService.deleteRefreshToken(request.getAccessToken());
        return HttpStatus.OK;
    }

}
