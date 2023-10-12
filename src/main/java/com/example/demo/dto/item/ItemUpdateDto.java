package com.example.demo.dto.item;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemUpdateDto {
    private String name;
    private Long price;
    private Long stockQuantity;
}
