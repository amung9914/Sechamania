package com.blog.entity;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

    private String fullAddress;
    private String lat;
    private String lon;

    @Builder
    public Address(String fullAddress, String lat, String lon) {
        this.fullAddress = fullAddress;
        this.lat = lat;
        this.lon = lon;
    }
}
