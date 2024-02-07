package com.example.javaspringlessons.service;

import com.example.javaspringlessons.dao.ProductDAO;
import com.example.javaspringlessons.dto.ProductDTO;
import com.example.javaspringlessons.entity.Product;
import com.example.javaspringlessons.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductDAO productDAO;
    private final ProductMapper mapper;
    private final MailService mailService;

    @Value("${spring.mail.username}")
    private String mailFrom;

    public List<ProductDTO> getAll(){
        List<Product> products = productDAO.findAll();

        return products
                .stream()
                .map(mapper::convertToDTO)
                .toList();
    }

    public ProductDTO save(ProductDTO productDTO){
        Product saved = productDAO.save(mapper.convertFromDTO(productDTO));

        mailService.sendEmail(mailFrom,"Your order is confirmed. Ordered products: " + saved.getName()+ ", " + saved.getQuantity() + " pieces, cost per one product - " + saved.getPrice() + "$","Order confirmation");

        return mapper.convertToDTO(saved);
    }

    public List<ProductDTO> createAll(List<ProductDTO> products){
        List<Product> productList = products.stream().map(mapper::convertFromDTO).toList();
        List<Product> savedAllProducts = productDAO.saveAll(productList);

        savedAllProducts.forEach(this::sendEmail);

        return savedAllProducts.stream().map(mapper::convertToDTO).toList();
    }

    public void sendEmail(Product product){
        mailService.sendEmail(mailFrom,"Your order is confirmed. Ordered products: " + product.getName()+ ", " + product.getQuantity() + " pieces, cost per one product - " + product.getPrice() + "$","Order confirmation");
    }

}
