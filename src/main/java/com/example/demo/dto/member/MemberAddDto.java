package com.example.demo.dto.member;

import com.example.demo.domain.Address;
import com.example.demo.domain.Gender;
import com.example.demo.domain.Role;
import jakarta.persistence.Embedded;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MemberAddDto {
    private Long id;

    private String loginId;

    private String password;

    private String name;

    private String email;

    private String birth;

    private Gender gender;

    private Role role;

    private Address address;

    public MemberAddDto() {
    }
}
