package com.example.demo.web;

import com.example.demo.domain.OrderStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrderSearch {
    private String memberName;
    private OrderStatus orderStatus;
}
