package com.example.javaspringlessons.dto;

import com.example.javaspringlessons.entity.Product;
import com.example.javaspringlessons.entity.User;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OrderDTO {
    int id;

    double totalPrice;

    List<Product> products;

    User user;
}
