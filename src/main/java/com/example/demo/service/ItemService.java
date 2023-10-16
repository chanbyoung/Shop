package com.example.demo.service;

import com.example.demo.domain.item.Item;
import com.example.demo.dto.item.ItemAddDto;
import com.example.demo.dto.item.ItemGetDto;
import com.example.demo.dto.item.ItemUpdateDto;
import com.example.demo.dto.item.ItemsGetDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ItemService {
     void saveItem(ItemAddDto item,String username);
     void updateItem(Long itemId, ItemUpdateDto itemUpdateDto);

    Page<ItemsGetDto> getItems(Pageable pageable);

    ItemGetDto getItem(Long itemId);

    void delete(Long id);
}
