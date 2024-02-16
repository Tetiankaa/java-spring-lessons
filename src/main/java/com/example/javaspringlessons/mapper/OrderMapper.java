package com.example.javaspringlessons.mapper;

import com.example.javaspringlessons.dto.OrderDTO;
import com.example.javaspringlessons.entity.Order;
import com.example.javaspringlessons.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public OrderDTO convertToDto(Order order){
        return OrderDTO.builder()
                .id(order.getId())
                .totalPrice(order.getTotalPrice())
                .user(order.getUser())
                .products(order.getProducts())
                .build();
    }

    public Order convertToOrder(OrderDTO orderDTO){
        double totalSum = orderDTO.getProducts().stream().mapToDouble(Product::getTotalPrice).sum();

        Order order = new Order();
        order.setId(orderDTO.getId());
        order.setTotalPrice(totalSum);
        order.setProducts(orderDTO.getProducts());
        order.setUser(orderDTO.getUser());

        return order;

    }
}
