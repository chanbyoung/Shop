package com.example.demo.dto.item;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemUpdateDto {
    @NotEmpty
    private String name;
    @NotNull
    private Long price;
    @NotNull
    private Long stockQuantity;
}
