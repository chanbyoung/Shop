package com.example.demo.service;

import com.example.demo.domain.Delivery;
import com.example.demo.domain.Member;
import com.example.demo.domain.Order;
import com.example.demo.domain.OrderItem;
import com.example.demo.domain.item.Item;
import com.example.demo.dto.order.GetOrderItem;
import com.example.demo.dto.order.OrderDto;
import com.example.demo.dto.order.OrderGetDto;
import com.example.demo.dto.order.OrderItemDto;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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
        Optional<Member> memberOptional = memberRepository.findByLoginId(loginId);

        if (memberOptional.isEmpty()) {
            // 멤버를 찾을 수 없는 경우에 대한 처리
            // 예외 던지거나, 에러 메시지를 반환하거나, 다른 방식으로 처리해주세요.
            // 여기서는 간단하게 NullPointerException 예외를 던지도록 했습니다.
            throw new NullPointerException("해당 멤버를 찾을 수 없습니다.");
        }

        Member member = memberOptional.get();
        List<OrderItemDto> orderItems = orderDto.getOrderItems();
        Delivery delivery = new Delivery(member.getAddress());

        List<OrderItem> createdOrderItems = new ArrayList<>(); // 생성된 OrderItem들을 모아둘 리스트

        for (OrderItemDto orderItemDto : orderItems) {
            Item findItem = itemRepository.findById(orderItemDto.getItemId())
                    .orElseThrow(() -> new IllegalArgumentException("주문하려는 상품을 찾을 수 없습니다."));

            OrderItem orderItem = OrderItem.builder()
                    .item(findItem)
                    .orderPrice(findItem.getPrice() * orderItemDto.getQuantity())
                    .count(orderItemDto.getQuantity())
                    .build();

            createdOrderItems.add(orderItem); // 생성된 OrderItem을 리스트에 추가

            findItem.removeStock(orderItemDto.getQuantity()); // 재고 수량 조절
        }

        // 주문 생성 시 Order에 생성된 OrderItem 리스트를 전달해줍니다.
        OrderItem[] orderItemsArray = createdOrderItems.toArray(OrderItem[]::new);
        Order order = Order.createOrder(member, delivery, orderItemsArray);
        orderRepository.save(order);

        return order.getId();
    }


    public OrderGetDto getOrder(Long id) {
        Optional<Order> order = orderRepository.findById(id);
        if (order.isPresent()) {
            Order findOrder = order.get();
            List<GetOrderItem> getOrderItems = findOrder.getOrderItems().stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
            return OrderGetDto.builder()
                    .id(findOrder.getId())
                    .memberName(findOrder.getMember().getName())
                    .orderItems(getOrderItems)
                    .deliveryStatus(findOrder.getDelivery().getStatus())
                    .localDateTime(findOrder.getLocalDateTime())
                    .orderStatus(findOrder.getStatus())
                    .build();
        }
        else throw new IllegalArgumentException();
    }

    private GetOrderItem convertToDto(OrderItem orderItem) {
        return GetOrderItem.builder()
                .itemName(orderItem.getItem().getName())
                .price(orderItem.getItem().getPrice())
                .count(orderItem.getCount()).build();
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

