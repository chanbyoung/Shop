package com.example.demo.domain.item;

import com.example.demo.dto.item.ItemUpdateDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
public abstract class Item {
    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;
    private String name;
    private Long price;
    private Long stockQuantity;

    public void updateItem(ItemUpdateDto itemUpdateDto) {
        name = itemUpdateDto.getName();
        price = itemUpdateDto.getPrice();
        stockQuantity = itemUpdateDto.getStockQuantity();
    }

}
