package com.example.javaspringlessons.controller;

import com.example.javaspringlessons.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.example.rest.model.ProductDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ProductController implements org.example.rest.controller.ProductsApi {

    private final ProductService productService;

    @Override
    public ResponseEntity<ProductDto> createProduct(ProductDto productDto) {
       return ResponseEntity.ok(productService.createProduct(productDto));
    }

    @Override
    public ResponseEntity<ProductDto> getProduct(Long id) {
        return ResponseEntity.of(productService.fingProduct(id));
    }

    @Override
    public ResponseEntity<List<ProductDto>> getProducts() {
        return ResponseEntity.ok(productService.getProducts());
    }

    @Override
    public ResponseEntity<ProductDto> modifyProduct(Long id, ProductDto productDto) {
        return ResponseEntity.ok(productService.updateProduct(id, productDto));
    }

    @Override
    public ResponseEntity<ProductDto> modifyProductPartially(Long id, ProductDto productDto) {
        return ResponseEntity.ok(productService.patchProduct(id, productDto));
    }
}
