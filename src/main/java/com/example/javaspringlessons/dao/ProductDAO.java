package com.example.javaspringlessons.dao;

import com.example.javaspringlessons.modules.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDAO extends JpaRepository<Product,Integer> {

    List<Product> findByName(String name);
}
