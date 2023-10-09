package com.example.demo.domain.item;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;

@Entity
@DiscriminatorValue("B")
@Getter
public class Book extends Item {
    private String author;
    private String isbn;
}
