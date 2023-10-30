package com.example.demo.service;

import com.example.demo.domain.Delivery;
import com.example.demo.domain.Member;
import com.example.demo.domain.Order;
import com.example.demo.domain.OrderItem;
import com.example.demo.domain.item.Item;
import com.example.demo.dto.order.OrderDto;
import com.example.demo.reopsitory.DslOrderRepository;
import com.example.demo.reopsitory.ItemRepository;
import com.example.demo.reopsitory.MemberRepository;
import com.example.demo.reopsitory.OrderRepository;
import com.example.demo.web.OrderSearch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final DslOrderRepository dslOrderRepository;
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    @Transactional
    public Long order(String loginId, OrderDto orderDto) {
        Optional<Member> member = memberRepository.findByLoginId(loginId);
        Optional<Item> item = itemRepository.findById(orderDto.getItemId());

        Member findMember = member.get();
        Item findItem = item.get();

        //배송 정보 생성
        Delivery delivery = new Delivery(findMember.getAddress());
        //주문 상품 생성
        OrderItem orderItem = OrderItem.builder()
                .item(findItem)
                .orderPrice(findItem.getPrice())
                .count(orderDto.getCount())
                .build();
        log.info("orderItem={}",orderItem);
        //제고 수량 삭제
        findItem.removeStock(orderDto.getCount());

        //주문 생성
        Order order = Order.createOrder(member.get(), delivery, orderItem);
        orderRepository.save(order);
        return order.getId();
    }

    public Page<Order> getOrders(OrderSearch orderSearch, Pageable pageable) {
        return dslOrderRepository.getOrders(orderSearch, pageable);
    }
    @Transactional
    public void cancelOrder(Long orderId,String loginId) {
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isPresent()) {
            Order findOrder = order.get();
            if (!Objects.equals(findOrder.getMember().getLoginId(), loginId)) {
                throw new RuntimeException();
            }
            findOrder.cancel();
        }
    }
}

