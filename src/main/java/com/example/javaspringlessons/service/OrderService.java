package com.example.javaspringlessons.service;

import com.example.javaspringlessons.dto.OrderDTO;
import com.example.javaspringlessons.entity.Order;
import com.example.javaspringlessons.entity.Product;
import com.example.javaspringlessons.mapper.OrderMapper;
import com.example.javaspringlessons.repository.OrderDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderDAO orderDAO;
    private final OrderMapper mapper;

    public void saveOrder(List<Product> products){
        OrderDTO orderDto = OrderDTO.builder()
                .products(products)
                .build();

        Order order = mapper.convertToOrder(orderDto);

        orderDAO.save(order);
    }

}
