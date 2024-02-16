package com.example.javaspringlessons.repository;

import com.example.javaspringlessons.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDAO extends JpaRepository<Order,Integer> {
}
