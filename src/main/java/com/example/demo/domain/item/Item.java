package com.example.demo.domain.item;

import com.example.demo.domain.CategoryItem;
import com.example.demo.domain.Member;
import com.example.demo.exception.NotEnoughStockException;
import com.example.demo.dto.item.ItemUpdateDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.*;

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
    @NotEmpty
    private String name;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    @OneToMany(mappedBy = "item",cascade = CascadeType.ALL)
    private List<CategoryItem> categoryItems;
    @NotNull
    private Long price;
    @NotNull
    private Long stockQuantity;
    @NotEmpty
    private String selectedOption;


    //연관관계 메서드
    public void addCategoryItem(CategoryItem categoryItem) {
        if (categoryItems == null) {
            categoryItems = new ArrayList<>();
        }
        categoryItems.add(categoryItem);
        categoryItem.setItem(this);
    }

    public void updateItem(ItemUpdateDto itemUpdateDto) {
        name = itemUpdateDto.getName();
        price = itemUpdateDto.getPrice();
        stockQuantity = itemUpdateDto.getStockQuantity();
    }

    public void removeStock(int count) {
        long restStock = stockQuantity - count;
        if (restStock < 0) {
            throw new NotEnoughStockException("need more stock");
        }
        stockQuantity =restStock;
    }

    public void addStock(int count) {
        this.stockQuantity += count;
    }
}
