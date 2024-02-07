package com.example.javaspringlessons.controller;

import com.example.javaspringlessons.dto.ProductDTO;
import com.example.javaspringlessons.service.ProductService;
import com.example.javaspringlessons.service.UploadProductService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final UploadProductService uploadProductService;

    @GetMapping("")
    public ResponseEntity<List<ProductDTO>>  getProducts(){
        return ResponseEntity.ok(productService.getAll());
    }

    @PostMapping("")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO product){
        ProductDTO savedProduct = productService.save(product);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedProduct.getId()).toUri();
        return ResponseEntity.created(uri).body(savedProduct);
    }

    @PostMapping("/upload")
    @SneakyThrows
    public ResponseEntity<List<ProductDTO>> uploadProducts(@RequestParam("file")MultipartFile file){

        return ResponseEntity.ok(uploadProductService.upload(file));
    }
}
