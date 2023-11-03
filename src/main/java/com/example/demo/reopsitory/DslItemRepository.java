package com.example.demo.reopsitory;

import com.example.demo.domain.Category;
import com.example.demo.domain.CategoryItem;
import com.example.demo.domain.item.Item;
import com.example.demo.domain.item.QItem;
import com.example.demo.web.ItemSearch;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.demo.domain.QCategoryItem.categoryItem;
import static com.example.demo.domain.item.QItem.*;

@Repository
@RequiredArgsConstructor
public class DslItemRepository {
    private final JPAQueryFactory query;

    public Page<Item> findByItems(Pageable pageable, ItemSearch itemSearch) {
        BooleanBuilder builder = new BooleanBuilder();
        where(itemSearch, builder);
        List<Item> findItems = query.select(item)
                .from(item)
                .where(builder)
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .distinct().fetch();

        Long count = query.select(item.count())
                .from(item)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(findItems, pageable, count);
    }

    private static void where(ItemSearch itemSearch, BooleanBuilder builder) {
        String name = itemSearch.getName();
        Long category = itemSearch.getCategory();
        if (category != null) {
            builder.and(item.categoryItems.any().category.id.eq(category));
        }
        if (name != null) {
            builder.and(item.name.contains(name));
        }
    }


}
