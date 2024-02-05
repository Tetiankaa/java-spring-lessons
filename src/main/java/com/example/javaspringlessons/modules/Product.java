package com.example.javaspringlessons.modules;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data
public class Product {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    @NotBlank(message = "Name can not be blank")
    private String name;

    private double price;

    @Min(value = 0,message = "Quantity can not be lower that 0")
    @Max(value = 100, message = "Quantity can not be higher that 100")
    private Integer quantity;
}
