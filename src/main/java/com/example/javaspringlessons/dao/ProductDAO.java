package com.example.javaspringlessons.dao;

import com.example.javaspringlessons.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductDAO extends JpaRepository<Product,Integer> {
}
