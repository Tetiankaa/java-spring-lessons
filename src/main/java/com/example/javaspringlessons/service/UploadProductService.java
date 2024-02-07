package com.example.javaspringlessons.service;

import com.example.javaspringlessons.dto.ProductDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UploadProductService {
    private final ObjectMapper objectMapper;
    private final ProductService productService;

    @Transactional
    @SneakyThrows
    public List<ProductDTO> upload(MultipartFile file){
        List<ProductDTO> value = objectMapper.readValue(file.getBytes(), new TypeReference<>() {
        });

//        return value
//                .stream()
//                .map(productService::save)
//                .toList();

        return productService.createAll(value);
    }
}
