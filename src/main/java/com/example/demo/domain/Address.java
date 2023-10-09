package com.example.demo.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class Address {
    private String postCode;
    private String address;
    private String detailAddress;
    private String extraAddress;
    private String fullAddress;

    protected Address() {
    }

    public Address(String postCode, String address, String detailAddress, String extraAddress) {
        this.postCode = postCode;
        this.address = address;
        this.detailAddress = detailAddress;
        this.extraAddress = extraAddress;
        this.fullAddress = getFullAddress();
    }

    private String getFullAddress() {
        return "(" + postCode +") " + address +" "+ detailAddress+" "+extraAddress;
    }

}
