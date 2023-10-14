package com.example.demo.domain.item;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("B")
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Book extends Item {
    private String author;
    private String isbn;
}
