package com.example.demo.service;

import com.example.demo.domain.Category;
import com.example.demo.domain.Gender;
import com.example.demo.domain.Member;
import com.example.demo.domain.Role;
import com.example.demo.domain.item.Book;
import com.example.demo.domain.item.Item;
import com.example.demo.dto.item.ItemAddDto;
import com.example.demo.dto.item.ItemGetDto;
import com.example.demo.dto.item.ItemUpdateDto;
import com.example.demo.dto.item.ItemsGetDto;
import com.example.demo.reopsitory.CategoryRepository;
import com.example.demo.reopsitory.DslItemRepository;
import com.example.demo.reopsitory.ItemRepository;
import com.example.demo.reopsitory.MemberRepository;
import com.example.demo.web.CategoryAddDto;
import com.example.demo.web.ItemSearch;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {
    @InjectMocks ItemServiceImpl itemService;
    @Mock ItemRepository itemRepository;
    @Mock CategoryRepository categoryRepository;
    @Mock
    DslItemRepository dslItemRepository;
    @Mock MemberRepository memberRepository;

    @Test
    void saveItem() throws IOException {
        ItemAddDto itemAddDto = new ItemAddDto();
        itemAddDto.setCategoryId(1L);
        itemAddDto.setName("test");
        itemAddDto.setPrice(10000L);
        itemAddDto.setStockQuantity(1000L);
        itemAddDto.setSelectedOption("book");
        itemAddDto.setImageFiles(new ArrayList<>());
        Category category = new Category(); // Mock Category
        when(categoryRepository.findById(itemAddDto.getCategoryId())).thenReturn(Optional.of(category));

        Member member = new Member(); // Mock Member
        when(memberRepository.findByLoginId("username")).thenReturn(Optional.of(member));

        Item savedItem = new Book(); // Mock saved item
        when(itemRepository.save(any())).thenReturn(savedItem);

        // Call the method
        itemService.saveItem(itemAddDto, "username");

        // Verifications
        verify(categoryRepository, times(1)).findById(anyLong());
        verify(memberRepository, times(1)).findByLoginId(anyString());
        verify(itemRepository, times(1)).save(any());
        // Add more verifications based on the specific interactions you want to verify
    }

    @Test
    void updateItem() {
        //given
        Item book = Book.builder()
                .id(1L)
                .name("pcb")
                .price(1000L)
                .stockQuantity(1000L)
                .build();
        when(itemRepository.findById(book.getId())).thenReturn(Optional.of(book));
        ItemUpdateDto itemUpdateDto = new ItemUpdateDto();
        itemUpdateDto.setName("park");
        itemUpdateDto.setPrice(2000L);
        itemUpdateDto.setStockQuantity(2000L);

        //when
        itemService.updateItem(book.getId(), itemUpdateDto);

        //then
        assertThat(book.getName()).isEqualTo(itemUpdateDto.getName());
        assertThat(book.getPrice()).isEqualTo(itemUpdateDto.getPrice());
        assertThat(book.getStockQuantity()).isEqualTo(itemUpdateDto.getStockQuantity());
    }

    @Test
    void getItems() {
        //given
        String name = "";
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size); // 실제 Pageable 객체 생성

        Item book = Book.builder()
                .id(1L)
                .name("pcb")
                .price(1000L)
                .stockQuantity(1000L)
                .build();

        List<Item> itemList = Collections.singletonList(book);
        PageImpl<Item> itemsPage = new PageImpl<>(itemList, pageable, itemList.size());
        when(dslItemRepository.findByItems(eq(pageable),any())).thenReturn(itemsPage);

        Page<ItemsGetDto> result = itemService.getItems(pageable, new ItemSearch());

        assertThat(result.getTotalElements()).isEqualTo(itemsPage.getTotalElements());
    }

    @Test
    void getItem() {
        Member member = Member.builder()
                .id(1L)
                .name("TestMember")
                .loginId("testLogin")
                .build();
        Item book = Book.builder()
                .id(1L)
                .name("pcb")
                .member(member)
                .price(1000L)
                .stockQuantity(1000L)
                .build();
        when(itemRepository.findById(book.getId())).thenReturn(Optional.of(book));

        //then
        ItemGetDto findItem = itemService.getItem(book.getId());

        //then
        assertThat(book.getId()).isEqualTo(findItem.getId());
        assertThat(book.getName()).isEqualTo(findItem.getName());
        assertThat(book.getPrice()).isEqualTo(findItem.getPrice());

    }

    @Test
    void delete() {
        Item book = Book.builder()
                .id(1L)
                .name("pcb")
                .price(1000L)
                .stockQuantity(1000L)
                .build();

        when(itemRepository.findById(book.getId())).thenReturn(Optional.of(book));

        itemService.delete(book.getId());

        verify(itemRepository, times(1)).delete(book);
    }

    @Test
    void addCategory() {
        CategoryAddDto categoryAddDto = new CategoryAddDto();
        categoryAddDto.setName("TestCategory");

        itemService.addCategory(categoryAddDto);

        ArgumentCaptor<Category> categoryCaptor = ArgumentCaptor.forClass(Category.class);
        verify(categoryRepository, times(1)).save(categoryCaptor.capture());

        Category capturedCategory = categoryCaptor.getValue();

        assertThat(capturedCategory.getName()).isEqualTo(categoryAddDto.getName());
    }

    @Test
    void getCategories() {
        Category category1 = Category.builder()
                .id(1L)
                .name("category1")
                .build();
        Category category2 = Category.builder()
                .id(2L)
                .name("category2")
                .build();
        when(categoryRepository.findAll()).thenReturn(Arrays.asList(category1, category2));

        List<Category> categories = itemService.getCategories();

        verify(categoryRepository).findAll();

        assertThat(categories).isNotNull();
        assertThat(categories).hasSize(2);
    }
}