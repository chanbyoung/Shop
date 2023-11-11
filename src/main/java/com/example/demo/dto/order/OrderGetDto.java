package com.example.demo.dto.order;

import com.example.demo.domain.DeliveryStatus;
import com.example.demo.domain.OrderStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
public class OrderGetDto {
    private Long id;
    private String memberName;
    private List<GetOrderItem> orderItems = new ArrayList<>();
    private DeliveryStatus deliveryStatus;
    private LocalDateTime localDateTime;
    private OrderStatus orderStatus;

}
