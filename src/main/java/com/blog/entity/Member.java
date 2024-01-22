package com.blog.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity implements UserDetails {
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
    @Enumerated(EnumType.STRING)
    private MemberStatus status;
    private String profileImg;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Authorities> authorities = new ArrayList<>();

    @Builder
    public Member(String email, String password, String nickname, Address address, MemberStatus status, String imgPath) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.address = address;
        this.status = status;
        profileImg = imgPath;

    }

    /**
     * 사용자 닉네임 변경
     */
    public Member updateNickname(String nickname){
        this.nickname = nickname;
        return this;
    }

    /**
     * 사용자 주소 변경
     */
    public Member updateAddress(Address address){
        this.address = address;
        return this;
    }

    /**
     * 사용자 암호 변경
     */
    public Member updatePassword(String password){
        this.password = password;
        return this;
    }
    public Member updateProfileImg(String profileImg){
        this.profileImg = profileImg;
        return this;
    }

    /**
     * 사용자 탈퇴
     */
    public Member withDraw(){
        this.status = MemberStatus.WITHDRAW;
        return this;

    }

    /**
     * 권한 설정용 메서드
     */
    public List<Authorities> makeUserRole(){
        return this.authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Hibernate.initialize(authorities);
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        for (Authorities authority : authorities) {
            String role = authority.getRole();
            authorityList.add(new SimpleGrantedAuthority(role));
        }
        return authorityList;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.status != MemberStatus.WITHDRAW;
    }
}
