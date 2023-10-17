package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;

import static jakarta.persistence.FetchType.*;

@Entity
@Table(name = "orders")
@Getter
@Builder
@AllArgsConstructor
public class Order {
    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery")
    private Delivery delivery;

    private LocalDateTime localDateTime;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    public Order() {
    }

    public static Order createOrder(Member member, Delivery delivery, OrderItem...  orderItems) {
        Order order = Order.builder()
                .member(member)
                .delivery(delivery)
                .status(OrderStatus.ORDER)
                .localDateTime(LocalDateTime.now())
                .build();
        for (OrderItem orderItem : orderItems) {
            orderItem.setOrder(order);
        }
        return order;
    }
}
