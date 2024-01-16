package com.blog.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Company extends BaseEntity{
    @Id @GeneratedValue
    @Column(name = "company_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // 대표자

    @Embedded
    @Column(nullable = false)
    private Address address;

    @Lob
    private String content; // 소개글
    @Column(nullable = false)
    private String contact; // 연락처
    @Column(nullable = false)
    private LocalTime startTime; // 영업 시작 시간
    @Column(nullable = false)
    private LocalTime endTime; // 영업 종료 시간

    private String imgPath; // 대표사진 이미지 파일경로

    @Builder
    public Company(String name, Member member, Address address, String content, String contact, LocalTime startTime, LocalTime endTime, String imgPath) {
        this.name = name;
        this.member = member;
        this.address = address;
        this.content = content;
        this.contact = contact;
        this.startTime = startTime;
        this.endTime = endTime;
        this.imgPath = imgPath;
    }
}
