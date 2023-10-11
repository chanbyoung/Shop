package com.example.demo.dto.member;

import com.example.demo.domain.Address;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberUpdateDto {
    private String name;
    private String birth;
    private Address address;

}
