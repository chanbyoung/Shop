package com.example.demo.dto.item;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@ToString

public class ItemAddDto {
    private Long categoryId;
    @NotEmpty
    private String name;
    @NotNull
    private Long price;
    @NotNull
    private Long stockQuantity;
    @NotEmpty
    private String selectedOption;

    private List<MultipartFile> imageFiles;

    private String artist;
    private String etc;
    private String author;
    private String isbn;
    private String director;
    private String actor;

}
