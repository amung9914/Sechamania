package com.blog.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity{
    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;
    private String password;

    @Column(unique = true)
    private String nickname;
    @Embedded
    private Address address;

    @OneToMany(mappedBy = "member")
    @JsonIgnore
    private List<Authorities> authorities = new ArrayList<>();

    @Builder
    public Member(String email, String password, String nickname, Address address) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.address = address;
    }

    /**
     * 권한 설정용 메서드
     */
    public List<Authorities> makeUserRole(){
        return this.authorities;
    }

}
