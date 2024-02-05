package com.example.javaspringlessons.dto;

import com.example.javaspringlessons.util.View;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductDTO {
    @JsonView(View.Internal.class)
    private int id;

    @JsonView(View.External.class)
    private String name;

    @JsonView(View.External.class)
    private double price;

    @JsonView(View.External.class)
    private Integer quantity;
}
