package com.example.demo.domain;

import com.example.demo.domain.item.Item;
import jakarta.persistence.*;
import lombok.Getter;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter
public class CategoryItem {
    @Id @GeneratedValue
    @Column(name = "category_item_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name ="category_id")
    private Category category;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "item_id")
    private Item item;


}
