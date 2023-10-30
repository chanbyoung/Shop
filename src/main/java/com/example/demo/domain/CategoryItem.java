package com.example.demo.domain;

import com.example.demo.domain.item.Item;
import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    public void setItem(Item item) {
        this.item = item;
    }
}
