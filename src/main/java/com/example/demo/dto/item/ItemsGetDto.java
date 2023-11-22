package com.example.demo.dto.item;

import com.example.demo.domain.item.UploadFile;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Builder
public class ItemsGetDto {
    private Long id;
    private String name;
    private Long price;
    private Long stockQuantity;
    private List<UploadFile> imageFiles;

}
