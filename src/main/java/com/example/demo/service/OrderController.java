package com.example.demo.service;

import com.example.demo.domain.Order;
import com.example.demo.dto.item.ItemsGetDto;
import com.example.demo.dto.order.OrderDto;
import com.example.demo.exception.NotEnoughStockException;
import com.example.demo.web.ItemSearch;
import com.example.demo.web.OrderSearch;
import com.example.demo.web.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
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
    public String order(@Validated @ModelAttribute OrderDto orderDto, BindingResult bindingResult, Authentication authentication, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            log.info("error={}", bindingResult);
            return "redirect:/order";
        }
        try {
            orderService.order(authentication.getName(), orderDto);
        } catch (NotEnoughStockException e) {
            redirectAttributes.addFlashAttribute("flag", true);
            return "redirect:/order";
        }
        return "redirect:/orders";
    }

    @GetMapping("/orders")
    public String getOrders(@ModelAttribute("orderSearch") OrderSearch orderSearch, Model model, @PageableDefault Pageable pageable) {
        Page<Order> orders = orderService.getOrders(orderSearch, pageable);
        model.addAttribute("orders", orders);
        return "orders/orders";
    }

    @PostMapping("/orders/{orderId}/cancel")
    public String cancelOrder(@PathVariable("orderId") Long orderId,Authentication authentication,RedirectAttributes redirectAttributes) {
        try {
            orderService.cancelOrder(orderId, authentication.getName());
        } catch (RuntimeException e) { //서비스에서 주문한 사람의 loginId와 로그인되어있는 사람의 id 가 같지 않으면 주문취소를 취소
            redirectAttributes.addFlashAttribute("flag", true);
        }
        return "redirect:/orders";
    }
}
