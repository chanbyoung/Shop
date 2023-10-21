package com.example.demo.reopsitory;

import com.example.demo.domain.*;
import com.example.demo.web.OrderSearch;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Query;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.example.demo.domain.QOrder.*;

@Repository
@RequiredArgsConstructor
@Slf4j
public class DslOrderRepository {
    private final JPAQueryFactory query;
    public Page<Order> getOrders(OrderSearch orderSearch, Pageable pageable) {
        log.info("orderSearch = {}", orderSearch);
        String memberName = orderSearch.getMemberName();
        OrderStatus orderStatus = orderSearch.getOrderStatus();
        BooleanBuilder builder = new BooleanBuilder();
        if (StringUtils.hasText(memberName)) {
            builder.and(order.member.name.eq(memberName));
        }
        if (orderStatus != null) {
            builder.and(order.status.eq(orderStatus));
        }
        List<Order> orders = query.select(order)
                .from(order)
                .leftJoin(order.member, QMember.member)
                .fetchJoin()
                .leftJoin(order.delivery, QDelivery.delivery)
                .fetchJoin()
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .distinct().fetch();

        long count = query.select(order.count())
                .from(order)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(orders, pageable, count);
    }
}
