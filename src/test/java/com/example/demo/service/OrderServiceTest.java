package com.example.demo.service;

import com.example.demo.domain.*;
import com.example.demo.domain.item.Book;
import com.example.demo.domain.item.Item;
import com.example.demo.dto.order.OrderDto;
import com.example.demo.dto.order.OrderGetDto;
import com.example.demo.reopsitory.DslOrderRepository;
import com.example.demo.reopsitory.ItemRepository;
import com.example.demo.reopsitory.MemberRepository;
import com.example.demo.reopsitory.OrderRepository;
import com.example.demo.web.OrderSearch;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @InjectMocks OrderService orderService;
    @Mock DslOrderRepository dslOrderRepository;
    @Mock OrderRepository orderRepository;
    @Mock ItemRepository itemRepository;
    @Mock MemberRepository memberRepository;
    @Mock Address address;

    @Test
    void order() {
        //given
        Member member = Member.builder()
                .id(1L)
                .name("testName")
                .loginId("pcb")
                .address(address)
                .build();
        Item item =Book.builder()
                .id(1L)
                .name("testItem")
                .price(1000L)
                .stockQuantity(100L)
                .build();
        OrderDto orderDto = new OrderDto();
        orderDto.setCount(10);
        orderDto.setItemId(item.getId());
        when(memberRepository.findByLoginId(member.getLoginId())).thenReturn(Optional.of(member));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        //when
        orderService.order(member.getLoginId(), orderDto);

        //then
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void getOrder() {
        Member member = Member.builder()
                .name("Test")
                .build();
        Item item = Book.builder()
                .name("testBook")
                .price(1000L)
                .build();
        OrderItem orderItem = OrderItem.builder()
                .id(1L)
                .item(item)
                .count(1)
                .build();
        Delivery delivery = new Delivery(mock(Address.class));

        Order order = Order.builder()
                .member(member)
                .delivery(delivery)
                .localDateTime(LocalDateTime.now())
                .status(OrderStatus.ORDER)
                .build();
        order.addOrderItem(orderItem);

        Long orderId = 1L;
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        //then
        OrderGetDto getOrder = orderService.getOrder(orderId);

        assertThat(getOrder.getMemberName()).isEqualTo(order.getMember().getName());
        assertThat(getOrder.getOrderStatus()).isEqualTo(order.getStatus());
        assertThat(getOrder.getDeliveryStatus()).isEqualTo(order.getDelivery().getStatus());
    }

    @Test
    void getOrders() {
        OrderSearch orderSearch = new OrderSearch();
        Pageable pageable = PageRequest.of(0, 10);
        Order order = Order.builder()
                .member(mock(Member.class))
                .delivery(mock(Delivery.class))
                .localDateTime(LocalDateTime.now())
                .status(OrderStatus.ORDER)
                .build();
        List<Order> orders = Collections.singletonList(order);
        PageImpl<Order> orderPage = new PageImpl<>(orders, pageable, orders.size());
        when(dslOrderRepository.getOrders(orderSearch, pageable)).thenReturn(orderPage);

        //when
        Page<Order> result = orderService.getOrders(orderSearch, pageable);

        assertThat(result.getTotalElements()).isEqualTo(orderPage.getTotalElements());
    }

    @Test
    void cancelOrder() {
        Member member = Member.builder()
                .loginId("test")
                .build();

        Order order = Order.builder()
                .member(member)
                .delivery(mock(Delivery.class))
                .localDateTime(LocalDateTime.now())
                .status(OrderStatus.ORDER)
                .build();
        Long orderId = 1L;
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        //when
        orderService.cancelOrder(orderId,"test");

        //then
        assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCEL);
    }
}