package com.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateAccessTokenDto {
    private String accessToken;

    public CreateAccessTokenDto() {
    }
}
