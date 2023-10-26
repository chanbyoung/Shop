package com.example.demo.dto.order;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDto {
    @NotNull
    private Long itemId;
    @NotNull
    private int count;

}
