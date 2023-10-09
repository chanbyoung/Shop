package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Delivery {
    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;
    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;
    @Embedded
    private Address address;

    public Delivery() {
    }
}
