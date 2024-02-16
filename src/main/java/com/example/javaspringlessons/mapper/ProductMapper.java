package com.example.javaspringlessons.mapper;

import com.example.javaspringlessons.dto.ProductDTO;
import com.example.javaspringlessons.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductDTO convertToDTO(Product product){
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .quantity(product.getQuantity())
                .pricePerPiece(product.getPricePerPiece())
                .totalPrice(product.getTotalPrice())
                .build();
    }

    public Product convertToProduct(ProductDTO productDTO){
        int quantity = productDTO.getQuantity();
        double pricePerPiece = productDTO.getPricePerPiece();


        Product product = new Product();
        product.setId(productDTO.getId());
        product.setName(productDTO.getName());
        product.setQuantity(quantity);
        product.setPricePerPiece(pricePerPiece);

        product.setTotalPrice(quantity * pricePerPiece);

        return product;
    }
}
