package com.example.javaspringlessons.service;

import com.example.javaspringlessons.dto.OrderDTO;
import com.example.javaspringlessons.dto.ProductDTO;
import com.example.javaspringlessons.entity.Product;
import com.example.javaspringlessons.mapper.ProductMapper;
import com.example.javaspringlessons.repository.ProductDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductDAO dao;
    private final ProductMapper mapper;


    public ProductDTO save(ProductDTO productDTO){
        List<Product> products = findAll();

        Product product = mapper.convertToProduct(productDTO);

        products.add(product);

    }

    public List<Product> findAll(){
        return dao.findAll();
    }

}
