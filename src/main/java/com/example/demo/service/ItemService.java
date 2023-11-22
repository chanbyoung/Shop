package com.example.demo.service;

import com.example.demo.domain.Category;
import com.example.demo.domain.item.Item;
import com.example.demo.dto.item.ItemAddDto;
import com.example.demo.dto.item.ItemGetDto;
import com.example.demo.dto.item.ItemUpdateDto;
import com.example.demo.dto.item.ItemsGetDto;
import com.example.demo.web.CategoryAddDto;
import com.example.demo.web.ItemSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface ItemService {
     void saveItem(ItemAddDto item,String username) throws IOException;
     void updateItem(Long itemId, ItemUpdateDto itemUpdateDto);

    Page<ItemsGetDto> getItems(Pageable pageable, ItemSearch itemSearch);

    ItemGetDto getItem(Long itemId);

    void delete(Long id);

    void addCategory(CategoryAddDto categoryAddDto);

    List<Category> getCategories();
}
