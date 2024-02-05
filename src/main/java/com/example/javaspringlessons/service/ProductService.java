package com.example.javaspringlessons.service;

import com.example.javaspringlessons.dao.ProductDAO;
import com.example.javaspringlessons.dto.ProductDTO;
import com.example.javaspringlessons.mapper.ProductMapper;
import com.example.javaspringlessons.modules.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductDAO productDAO;
    private final ProductMapper productMapper;

    public List<ProductDTO> getProducts(String name){
        return Optional.ofNullable(name)
                .map(productDAO::findByName)
                .orElseGet(productDAO::findAll)
                .stream()
                .map(productMapper::toDTO)
                .toList();
    }

    public ProductDTO saveProduct(ProductDTO productDTO){
        Product product = productMapper.fromDTO(productDTO);
        Product savedProduct = productDAO.save(product);

        return productMapper.toDTO(savedProduct);

    }


}
