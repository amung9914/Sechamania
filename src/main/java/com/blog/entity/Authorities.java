package com.blog.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Authorities extends BaseTimeEntity{
    @Id @GeneratedValue
    @Column(name = "authorities_id")
    private Long id;

    private String role;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public Authorities(String role) {
        this.role = role;
    }

    // 연관관계 메서드
    public void makeRole(Member member){
        this.member = member;
        member.makeUserRole().add(this);
    }

}
