package com.example.demo.dto.order;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class GetOrderItem {
    private String itemName;
    private Long price;
    private int count;

}
