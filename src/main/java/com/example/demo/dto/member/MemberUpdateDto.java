package com.example.demo.dto.member;

import com.example.demo.domain.Address;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberUpdateDto {
    private Long id;

    @NotEmpty
    private String name;
    @NotEmpty
    private String birth;
    private Address address;

}
