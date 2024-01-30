package com.example.javaspringlessons.controllers;

import com.example.javaspringlessons.dao.ProductDAO;
import com.example.javaspringlessons.models.Product;
import com.example.javaspringlessons.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductController {
    // 1st example with @Configuration annotation
//    private final Product productTest;
//
//    public ProductController(@Qualifier("productTest2") Product productTest) {
//        this.productTest = productTest;
//    }

//    @GetMapping("/products")
//    public ResponseEntity<List<Product>> getProducts(){
//        return ResponseEntity.ok(List.of(productTest));
//    }

    //2nd example with DAO
//    private final ProductDAO productDAO;
//
//    public ProductController(ProductDAO productDAO) {
//        this.productDAO = productDAO;
//    }
//
//    @GetMapping("/products")
//    public ResponseEntity<List<Product>> getProducts(){
//      return ResponseEntity.ok(productDAO.getProducts());
//    }
//
//    @PostMapping("/products")
//    public ResponseEntity<Product> createProduct(@RequestBody Product product){
//      Product createdProduct = productDAO.saveProduct(product);
//        return ResponseEntity.ok().body(createdProduct);
//    }

    // 3rd example with JpaRepository

    private final ProductRepository productRepository;

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getProducts(@RequestParam(required = false) String name){

        if (name == null){
            return ResponseEntity.ok(productRepository.findAll());
        }else{
            return ResponseEntity.ok(productRepository.findByName(name));
        }
    }

    @PostMapping("/products")
    public ResponseEntity<Product> createProduct(@RequestBody Product product){
        Product saved = productRepository.save(product);
        return ResponseEntity.ok().body(saved);
    }


}
