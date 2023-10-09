package com.example.demo.domain;

import com.example.demo.dto.member.MemberUpdateDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {
    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String loginId;

    private String password;

    private String name;

    private String email;

    private String birth;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Enumerated(EnumType.STRING)
    private Role role;

    @Embedded
    private Address address;

    public void updateMember(MemberUpdateDto memberUpdateDto) {
        this.name = memberUpdateDto.getName();
        this.address = memberUpdateDto.getAddress();
    }
}
