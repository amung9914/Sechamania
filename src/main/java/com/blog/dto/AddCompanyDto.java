package com.blog.dto;

import com.blog.entity.Address;
import com.blog.entity.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalTime;

@Data
public class AddCompanyDto {

    private String name; // 회사명
    private Address address;
    private String content; // 소개글
    private String contact; // 연락처
    private LocalTime startTime; // 영업 시작 시간
    private LocalTime endTime; // 영업 종료 시간

    @Builder
    public AddCompanyDto(String name, String fullAddress, String city, String lat, String lon,
                         String content, String contact, LocalTime startTime, LocalTime endTime) {
        this.name = name;
        this.address = new Address(fullAddress,city, lat, lon);
        this.content = content;
        this.contact = contact;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
