package com.blog.dto;

import com.blog.entity.Address;
import com.blog.entity.Member;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateMemberDto {
    private String nickname;
    private Address address;

    @Builder
    public UpdateMemberDto(String nickname, String roadAddress, String extraAddr, String city, String lat, String lon) {
        this.nickname = nickname;
        this.address = new Address(extraAddr==null?roadAddress:roadAddress+extraAddr,city, lat, lon);
    }

}
