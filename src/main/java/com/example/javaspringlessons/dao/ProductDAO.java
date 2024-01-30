package com.example.javaspringlessons.dao;

import com.example.javaspringlessons.models.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductDAO {
    private final EntityManager entityManager;

    public List<Product> getProducts(){
        TypedQuery<Product> selectPFromProductP = entityManager.createQuery("select p from Product p", Product.class);
        return selectPFromProductP.getResultList();
    }

    @Transactional
    public Product saveProduct(Product product){
        entityManager.persist(product);
        return product;
    }
}
