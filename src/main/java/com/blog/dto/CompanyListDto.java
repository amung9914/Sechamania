package com.blog.dto;

import com.blog.entity.Address;
import lombok.Data;

@Data
public class CompanyListDto {

    private String name;
    private String fullAddress;
    private String lat;
    private String lon;

    public CompanyListDto(String name, Address address) {
        this.name = name;
        this.fullAddress = address.getFullAddress();
        this.lat = address.getLat();
        this.lon = address.getLon();
    }
}
