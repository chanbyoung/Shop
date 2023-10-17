package com.example.demo.web;

import com.example.demo.domain.Delivery;
import com.example.demo.domain.Member;
import com.example.demo.domain.Order;
import com.example.demo.domain.OrderItem;
import com.example.demo.domain.item.Item;
import com.example.demo.reopsitory.ItemRepository;
import com.example.demo.reopsitory.MemberRepository;
import com.example.demo.reopsitory.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    @Transactional
    public Long order(String loginId, Long itemId, int count) {
        Optional<Member> member = memberRepository.findByLoginId(loginId);
        Optional<Item> item = itemRepository.findById(itemId);

        Member findMember = member.get();
        Item findItem = item.get();

        //배송 정보 생성
        Delivery delivery = new Delivery(findMember.getAddress());
        //주문 상품 생성
        OrderItem orderItem = OrderItem.builder()
                .item(findItem)
                .orderPrice(findItem.getPrice())
                .count(count)
                .build();
        //제고 수량 삭제
        findItem.removeStock(count);

        //주문 생성
        Order order = Order.createOrder(member.get(), delivery, orderItem);
        orderRepository.save(order);
        return order.getId();
    }
}

