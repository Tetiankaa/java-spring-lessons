package com.example.javaspringlessons.service;

import com.example.javaspringlessons.dto.ProductDTO;
import com.example.javaspringlessons.entity.Product;
import com.example.javaspringlessons.mapper.ProductMapper;
import com.example.javaspringlessons.repository.ProductDAO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductDAO productDAO;

    private final ProductMapper mapper;
    private final MailService mailService;

    public ProductService(ProductDAO productDAO, ProductMapper mapper, MailService mailService) {
        this.productDAO = productDAO;
        this.mapper = mapper;
        this.mailService = mailService;
    }

    //    @Value("${spring.mail.username}")
    private String mailFrom = "tanyakorsun0208@gmail.com";

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
