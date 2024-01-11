package com.blog.dto;

import com.blog.entity.Address;
import lombok.Builder;
import lombok.Data;

@Data
public class AddUserRequest {
    private String email;
    private String nickname;
    private String password;
    private Address address;

    @Builder
    public AddUserRequest(String email, String nickname, String password, String fullAddress, String city, String lat, String lon) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.address = new Address(fullAddress,city, lat, lon);
    }
}
