package com.example.demo.service;

import com.example.demo.domain.Order;
import com.example.demo.dto.item.ItemsGetDto;
import com.example.demo.web.ItemSearch;
import com.example.demo.web.OrderSearch;
import com.example.demo.web.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final ItemService itemService;

    @GetMapping("/order")
    public String getOrder(Model model, @PageableDefault(size = 5) Pageable pageable) {
        Page<ItemsGetDto> items = itemService.getItems(pageable, new ItemSearch());
        model.addAttribute("items", items);
        return "orders/order";
    }

    @PostMapping("/order")
    public String order(@RequestParam("itemId") Long itemId, @RequestParam("count") int count, Authentication authentication) {
        orderService.order(authentication.getName(), itemId, count);
        return "redirect:/orders";
    }

    @GetMapping("/orders")
    public String getOrders(@ModelAttribute("orderSearch") OrderSearch orderSearch, Model model, @PageableDefault Pageable pageable) {
        Page<Order> orders = orderService.getOrders(orderSearch, pageable);
        model.addAttribute("orders", orders);
        return "orders/orders";
    }

    @PostMapping("/orders/{orderId}/cancel")
    public String cancelOrder(@PathVariable("orderId") Long orderId) {
        orderService.cancelOrder(orderId);
        return "redirect:/orders";
    }
}
