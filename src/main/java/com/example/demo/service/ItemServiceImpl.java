package com.example.demo.service;

import com.example.demo.domain.item.Item;
import com.example.demo.dto.item.ItemUpdateDto;
import com.example.demo.reopsitory.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly =true)
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    @Override
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    @Override
    @Transactional
    public void updateItem(Long itemId, ItemUpdateDto itemUpdateDto) {
        Optional<Item> item = itemRepository.findById(itemId);
        if (item.isPresent()) {
            Item updateItem = item.get();
            updateItem.updateItem(itemUpdateDto);
        }
    }

    @Override
    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    @Override
    public Item findOne(Long itemId) {
        Optional<Item> item = itemRepository.findById(itemId);
        return item.orElse(null);
    }
}
