package com.blog.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
// 유입경로를 수집하는 table
public class Traffic {
    @Id @GeneratedValue
    @Column(name = "traffic_id")
    private Long id;

    private String referralPath;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime visitDate;

    @Builder
    public Traffic(String referralPath, LocalDateTime visitDate) {
        this.referralPath = referralPath;
        this.visitDate = visitDate;
    }
}
