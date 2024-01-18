package com.blog.dto;

import com.blog.entity.Address;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
public class OauthSignupRequest {
    @NotNull
    private String nickname;
    @NotNull
    private Address address;

    @Builder
    public OauthSignupRequest(String nickname, String roadAddress, String extraAddr, String city, String lat, String lon) {
        this.nickname = nickname;
        this.address = new Address(extraAddr==null?roadAddress:roadAddress+extraAddr,city, lat, lon);
    }
}
