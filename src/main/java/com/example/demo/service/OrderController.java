package com.example.demo.service;

import com.example.demo.dto.item.ItemsGetDto;
import com.example.demo.web.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final ItemService itemService;

    @GetMapping("/order")
    public String getOrder(Model model, @PageableDefault(size = 5) Pageable pageable) {
        Page<ItemsGetDto> items = itemService.getItems(pageable);
        model.addAttribute("items", items);
        return "orders/order";
    }

    @PostMapping("/order")
    public String order(@RequestParam("itemId") Long itemId, @RequestParam("count") int count, Authentication authentication) {
        orderService.order(authentication.getName(), itemId, count);
        return "redirect:/orders";
    }

}
