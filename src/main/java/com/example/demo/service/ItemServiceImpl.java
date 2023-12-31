package com.example.demo.service;

import com.example.demo.domain.Category;
import com.example.demo.domain.CategoryItem;
import com.example.demo.domain.Member;
import com.example.demo.domain.item.Album;
import com.example.demo.domain.item.Book;
import com.example.demo.domain.item.Item;
import com.example.demo.domain.item.Movie;
import com.example.demo.dto.item.ItemAddDto;
import com.example.demo.dto.item.ItemGetDto;
import com.example.demo.dto.item.ItemUpdateDto;
import com.example.demo.dto.item.ItemsGetDto;
import com.example.demo.dto.member.MemberGetDto;
import com.example.demo.reopsitory.*;
import com.example.demo.web.CategoryAddDto;
import com.example.demo.web.ItemSearch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional(readOnly =true)
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final DslItemRepository dslItemRepository;
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;
    private final FileStore fileStore;
    @Override
    @Transactional
    public void saveItem(ItemAddDto item, String username) throws IOException {
        Category category = categoryRepository.findById(item.getCategoryId()).get();
        Optional<Member> findMember = memberRepository.findByLoginId(username);
        Item saveItem = null;
        if(findMember.isPresent()) {
            switch (item.getSelectedOption()) {
                case "book" -> {
                    Book book = Book.builder()
                            .name(item.getName())
                            .price(item.getPrice())
                            .stockQuantity(item.getStockQuantity())
                            .selectedOption("book")
                            .author(item.getAuthor())
                            .isbn(item.getIsbn())
                            .member(findMember.get())
                            .build();
                    saveItem = itemRepository.save(book);
                }
                case "album" -> {
                    Album album = Album.builder()
                            .name(item.getName())
                            .price(item.getPrice())
                            .stockQuantity(item.getStockQuantity())
                            .selectedOption("album")
                            .artist(item.getArtist())
                            .etc(item.getEtc())
                            .member(findMember.get())
                            .build();
                    saveItem = itemRepository.save(album);
                }
                case "movie" -> {
                    Movie movie = Movie.builder()
                            .name(item.getName())
                            .price(item.getPrice())
                            .stockQuantity(item.getStockQuantity())
                            .selectedOption("movie")
                            .actor(item.getActor())
                            .director(item.getDirector())
                            .member(findMember.get())
                            .build();
                    saveItem =itemRepository.save(movie);
                }
            }
            if (!item.getImageFiles().isEmpty()) {
                saveItem.addImageFile(fileStore.storeFiles(item.getImageFiles(),saveItem));
            }
            CategoryItem categoryItem = CategoryItem.builder()
                    .category(category)
                    .item(saveItem)
                    .build();
            Objects.requireNonNull(saveItem).addCategoryItem(categoryItem);
        }
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
    public Page<ItemsGetDto> getItems(Pageable pageable, ItemSearch itemSearch) {
        return dslItemRepository.findByItems(pageable,itemSearch).map(this::convertToDTO);
    }
    private ItemsGetDto convertToDTO(Item item) {
        return ItemsGetDto.builder()
                .id(item.getId())
                .name(item.getName())
                .price(item.getPrice())
                .stockQuantity(item.getStockQuantity())
                .imageFiles(item.getImageFiles())
                .build();
    }
    @Override
    public ItemGetDto getItem(Long itemId) {
        Optional<Item> item = itemRepository.findById(itemId);
        if (item.isPresent()) {
            Item findItem = item.get();
            return ItemGetDto.builder()
                    .id(findItem.getId())
                    .name(findItem.getName())
                    .memberName(findItem.getMember().getLoginId())
                    .price(findItem.getPrice())
                    .stockQuantity(findItem.getStockQuantity())
                    .build();
        }
        return null;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Optional<Item> item = itemRepository.findById(id);
        item.ifPresent(itemRepository::delete);
    }

    @Override
    @Transactional
    public void addCategory(CategoryAddDto categoryAddDto) {
        Category category = Category.builder()
                .name(categoryAddDto.getName())
                .build();
        categoryRepository.save(category);

    }

    @Override
    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }
}
