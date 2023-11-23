package com.example.demo.web;

import com.example.demo.domain.Category;
import com.example.demo.domain.item.Item;
import com.example.demo.dto.item.ItemAddDto;
import com.example.demo.dto.item.ItemGetDto;
import com.example.demo.dto.item.ItemUpdateDto;
import com.example.demo.dto.item.ItemsGetDto;
import com.example.demo.dto.member.MemberUpdateDto;
import com.example.demo.reopsitory.FileStore;
import com.example.demo.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.h2.engine.Mode;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
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

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
@RequestMapping("/items")
@Slf4j
public class ItemController {
    private final ItemService itemService;
    private final FileStore fileStore;

    @GetMapping
    public String getItems(Model model, @PageableDefault(size = 5) Pageable pageable, @ModelAttribute ItemSearch itemSearch) {
        log.info("itemSearch={}", itemSearch);
        Page<ItemsGetDto> items = itemService.getItems(pageable, itemSearch);
        List<Category> categories = itemService.getCategories();
        model.addAttribute("items", items);
        model.addAttribute("categories", categories);
        model.addAttribute("pageable", pageable);
        return "items/items";
    }

    @GetMapping("/{itemId}")
    public String getItem(Model model, @PathVariable Long itemId) {
        ItemGetDto item = itemService.getItem(itemId);
        model.addAttribute("item", item);
        return "items/item";
    }

    @GetMapping("/new")
    public String addItemForm(Model model) {
        List<Category> categories = itemService.getCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("item", new ItemAddDto());
        return "items/addItemForm";
    }

    @PostMapping("/new")
    public String addItem(@Validated @ModelAttribute(name = "item") ItemAddDto itemAddDto, BindingResult bindingResult, Authentication authentication) throws IOException {
        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "items/addItemForm";
        }
        log.info("itemAddDto ={} ", itemAddDto);
        itemService.saveItem(itemAddDto, authentication.getName());
        return "redirect:/";
    }

    @GetMapping("{id}/edit")
    public String updateItemForm(@PathVariable Long id, Model model, Authentication authentication, RedirectAttributes redirectAttributes) {
        ItemGetDto item = itemService.getItem(id);
        if (!Objects.equals(item.getMemberName(), authentication.getName())) {
            redirectAttributes.addFlashAttribute("flag", true);
            return "redirect:/items/{id}";
        }
        model.addAttribute("item", item);
        return "items/edit";
    }

    @PostMapping("{id}/edit")
    public String updateItem(@PathVariable Long id, @Validated @ModelAttribute(name = "item") ItemUpdateDto itemUpdateDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.info("error={}", bindingResult);
            return "items/edit";
        }
        itemService.updateItem(id, itemUpdateDto);
        return "redirect:/items";
    }

    @PostMapping("/{id}/delete")
    public String deleteItem(@PathVariable Long id, Authentication authentication, RedirectAttributes redirectAttributes) {
        ItemGetDto item = itemService.getItem(id);
        if (!Objects.equals(item.getMemberName(), authentication.getName())) {
            redirectAttributes.addFlashAttribute("flag", true);
            return "redirect:/items/{id}";
        }
        itemService.delete(id);
        return "redirect:/items";
    }

    @GetMapping("/category/add")
    public String addCategoryForm(Model model) {
        model.addAttribute("category", new CategoryAddDto());
        return "categories/add";
    }

    @PostMapping("/category/add")
    public String addCategory(@ModelAttribute(name = "category") CategoryAddDto categoryAddDto) {
        itemService.addCategory(categoryAddDto);
        return "redirect:/";
    }

    @ResponseBody
    @GetMapping("/images/{filename}")
    public Resource downloadImage(@PathVariable String filename) throws MalformedURLException {
        return new UrlResource("file:" + fileStore.getFullPath(filename));
    }
}
