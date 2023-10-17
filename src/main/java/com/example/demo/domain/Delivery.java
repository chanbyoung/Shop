package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.Builder;
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

    public Delivery(Address address) {
        this.status = DeliveryStatus.READY;
        this.address = address;
    }
}
