package com.example.demo.dto.item;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class ItemsGetDto {
    private Long id;
    private String name;
    private Long price;
    private Long stockQuantity;
}
