package com.example.demo.dto.item;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ItemAddDto {
    private String name;
    private Long price;
    private Long stockQuantity;
    private String selectedOption;

    private String artist;
    private String etc;
    private String author;
    private String isbn;
    private String director;
    private String actor;

}
