package com.example.javaspringlessons.service;

import org.example.rest.model.ProductDto;
import com.example.javaspringlessons.entity.Product;
import com.example.javaspringlessons.mapper.ProductMapper;
import com.example.javaspringlessons.repository.ProductDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductMapper mapper;
    private final ProductDAO productDAO;

    public ProductDto createProduct(ProductDto productDTO){
        Product product = mapper.toProduct(productDTO);
        Product saved = productDAO.save(product);
        return mapper.toDto(saved);
    }

    public Optional<ProductDto> fingProduct(Long id){
       return productDAO.findById(id).map(mapper::toDto);
    }

    public List<ProductDto> getProducts(){
        List<Product> products = productDAO.findAll();

       return products.stream()
                .map(mapper::toDto)
                .toList();
    }

    public ProductDto updateProduct(Long targetProductId, ProductDto source){
        Product targetProduct = productDAO
                .findById(targetProductId)
                .orElseThrow();

        mapper.updateProduct(targetProduct, source);

        Product modifiedProduct = productDAO.save(targetProduct);

        return mapper.toDto(modifiedProduct);

    }

    public ProductDto patchProduct(Long targetProductId, ProductDto source){
        Product targetProduct = productDAO
                .findById(targetProductId)
                .orElseThrow();

        mapper.patchProduct(targetProduct,source);

        Product modified = productDAO.save(targetProduct);

        return mapper.toDto(modified);
    }
}
