package com.example.javaspringlessons.dto;

import lombok.Data;


@Data
public class ProductDTO {

    private Long id;

    private String name;

    private Double price;

    private Integer quantity;

    private String description;
    private String status;

    public enum StatusEnum{
                OUT_OF_STOCK,
                IN_STOCK,
                DISCONTINUED
    }
}
