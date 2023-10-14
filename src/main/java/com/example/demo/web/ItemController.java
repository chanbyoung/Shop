package com.example.demo.web;

import com.example.demo.domain.item.Item;
import com.example.demo.dto.item.ItemAddDto;
import com.example.demo.dto.item.ItemUpdateDto;
import com.example.demo.dto.item.ItemsGetDto;
import com.example.demo.dto.member.MemberUpdateDto;
import com.example.demo.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.h2.engine.Mode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/items")
@Slf4j
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public String getItems(Model model, @PageableDefault(size = 5) Pageable pageable) {
        Page<ItemsGetDto> items = itemService.getItems(pageable);
        model.addAttribute("items", items);
        return "items/items";
    }

    @GetMapping("/new")
    public String addItemForm(Model model) {
        model.addAttribute("item", new ItemAddDto());
        return "items/addItemForm";
    }

    @PostMapping("/new")
    public String addItem(@ModelAttribute ItemAddDto itemAddDto) {
        log.info("itemAddDto ={} ", itemAddDto);
        itemService.saveItem(itemAddDto);
        return "redirect:/";
    }

    @GetMapping("{id}/edit")
    public String updateItemForm(@PathVariable Long id, Model model) {
        Item item = itemService.getItem(id);
        model.addAttribute("item", item);
        return "items/edit";
    }

    @PostMapping("{id}/edit")
    public String updateItem(@PathVariable Long id, @ModelAttribute ItemUpdateDto itemUpdateDto) {
        itemService.updateItem(id,itemUpdateDto);
        return "redirect:/";
    }
}
