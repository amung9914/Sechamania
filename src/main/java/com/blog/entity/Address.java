package com.blog.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Address {

    private String fullAddress;
    private String city;
    private String lat;
    private String lon;

    @Builder
    public Address(String fullAddress, String city, String lat, String lon) {
        this.fullAddress = fullAddress;
        this.city = city;
        this.lat = lat;
        this.lon = lon;
    }
}
