package com.example.javaspringlessons.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ProductDTO{
    int id;

    String name;
    int quantity;
    double pricePerPiece;

    double totalPrice;
}
