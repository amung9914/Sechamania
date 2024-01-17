package com.blog.dto;

import com.blog.entity.Address;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
public class AddUserRequest {
    @NotNull
    private String email;
    @NotNull
    private String nickname;
    private String password;
    @NotNull
    private Address address;

    @Builder
    public AddUserRequest(String email, String nickname, String password, String roadAddress, String extraAddr, String city, String lat, String lon) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.address = new Address(extraAddr==null?roadAddress:roadAddress+extraAddr,city, lat, lon);
    }
}
