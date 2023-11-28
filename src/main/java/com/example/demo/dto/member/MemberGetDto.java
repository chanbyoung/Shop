package com.example.demo.dto.member;

import com.example.demo.domain.Address;
import com.example.demo.domain.Gender;
import com.example.demo.domain.Order;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@Builder
public class MemberGetDto {
    private Long id;
    private String loginId;
    private String name;
    private String email;
    private String birth;
    private Gender gender;
    private Address address;
    private List<Order> orderList;
}
