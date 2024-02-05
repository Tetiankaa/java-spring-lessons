package com.example.javaspringlessons.controller;

import com.example.javaspringlessons.dto.ProductDTO;
import com.example.javaspringlessons.service.ProductService;
import com.example.javaspringlessons.util.View;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    @JsonView(View.External.class)
    @GetMapping("")
    public ResponseEntity<List<ProductDTO>> getExternalProducts(@RequestParam(required = false) String name){
//        List<Product> products;
//
//        if (name != null){
//            products = productDAO.findByName(name);
//        }else {
//           products = productDAO.findAll();
//        }
//
//        List<ProductDTO> listProductsDTO = products.stream()
//                .map(productMapper::toDTO)
//                .toList();
//
//        return ResponseEntity.ok(listProductsDTO);

        return ResponseEntity.ok(productService.getProducts(name));

    }
    @JsonView(View.Internal.class)
    @GetMapping("/private")
    public ResponseEntity<List<ProductDTO>> getInternalProducts(@RequestParam(required = false) String name){
        return ResponseEntity.ok(productService.getProducts(name));

    }

    @PostMapping("")
    public ResponseEntity<ProductDTO> saveProduct(@RequestBody @Valid ProductDTO product){
      return ResponseEntity.ok().body(productService.saveProduct(product));
    }
}
