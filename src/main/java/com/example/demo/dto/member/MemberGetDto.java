package com.example.demo.dto.member;

import com.example.demo.domain.Address;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MemberGetDto {
    private Long id;
    private String name;
    private Address address;
}
