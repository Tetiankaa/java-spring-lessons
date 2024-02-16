package com.example.javaspringlessons.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@Entity(name = "orders")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    double totalPrice;

    @OneToMany(mappedBy = "id",cascade = CascadeType.ALL)
    List<Product> products;

    @OneToOne
    @JoinColumn(name = "user_id")
    User user;

    public Order(int id, List<Product> products, User user) {
        double sum = products.stream()
                .mapToDouble(product -> {
                    int quantity = product.getQuantity();
                    double pricePerPiece = product.getPricePerPiece();
                    return quantity * pricePerPiece;
                })
                .sum();

        this.id = id;
        this.totalPrice = sum;
        this.user = user;
        this.products = products;


    }
}
