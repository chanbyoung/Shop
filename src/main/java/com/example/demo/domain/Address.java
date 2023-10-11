package com.example.demo.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Embeddable
@ToString
@Getter @Setter
public class Address {
    private String postCode;
    private String address;
    private String detailAddress;
    private String extraAddress;
    private String fullAddress;

    protected Address() {
    }

    public String getFullAddress() {
        return "(" + postCode +") " + address +" "+ detailAddress+" "+extraAddress;
    }

}
