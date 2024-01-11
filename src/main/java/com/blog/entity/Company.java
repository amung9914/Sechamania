package com.blog.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Company extends BaseEntity{
    @Id @GeneratedValue
    @Column(name = "company_id")
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // 대표자

    @Embedded
    private Address address;

    @Lob
    private String content;

    private String contact; // 연락처

    private LocalDateTime startTime; // 영업 시작 시간
    private LocalDateTime endTime; // 영업 종료 시간

    private String path; // 대표사진 이미지 파일경로


}
