package com.example.demo.service;

import com.example.demo.domain.item.Item;
import com.example.demo.dto.item.ItemUpdateDto;

import java.util.List;

public interface ItemService {
     void saveItem(Item item);
     void updateItem(Long itemId, ItemUpdateDto itemUpdateDto);

    List<Item> findItems();

    Item findOne(Long itemId);

}
