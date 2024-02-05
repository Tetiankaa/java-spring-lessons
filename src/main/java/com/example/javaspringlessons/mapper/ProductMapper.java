package com.example.javaspringlessons.mapper;

import com.example.javaspringlessons.dto.ProductDTO;
import com.example.javaspringlessons.modules.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
   public ProductDTO toDTO(Product product){
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .build();
    }

    public Product fromDTO(ProductDTO productDTO){
       Product product = new Product();
       product.setId(productDTO.getId());
       product.setName(productDTO.getName());
       product.setPrice(productDTO.getPrice());
       product.setQuantity(productDTO.getQuantity());

       return product;
    }
}
