package com.example.javaspringlessons.service;

import com.example.javaspringlessons.dto.ProductDTO;
import com.example.javaspringlessons.entity.Product;
import com.example.javaspringlessons.mapper.ProductMapper;
import com.example.javaspringlessons.repository.ProductDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductMapper mapper;
    private final ProductDAO productDAO;

    public ProductDTO createProduct(ProductDTO productDTO){
        Product product = mapper.toProduct(productDTO);
        Product saved = productDAO.save(product);
        return mapper.toDto(saved);
    }

    public Optional<ProductDTO> updateProduct(Long targetProductId, ProductDTO source){
        Product targetProduct = productDAO
                .findById(targetProductId)
                .orElseThrow();

        mapper.updateProduct(targetProduct, source);

        Product modifiedProduct = productDAO.save(targetProduct);

        return Optional.ofNullable(mapper.toDto(modifiedProduct));

    }

    public ProductDTO patchProduct(Long targetProductId, ProductDTO source){
        Product targetProduct = productDAO
                .findById(targetProductId)
                .orElseThrow();

        mapper.patchProduct(targetProduct,source);

        Product modified = productDAO.save(targetProduct);

        return mapper.toDto(modified);
    }
}
