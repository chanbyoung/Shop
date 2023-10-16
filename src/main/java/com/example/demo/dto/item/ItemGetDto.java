package com.example.demo.dto.item;

import com.example.demo.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class ItemGetDto {
    private Long id;
    private String name;
    private String memberName;
    private Long price;
    private Long stockQuantity;

}
