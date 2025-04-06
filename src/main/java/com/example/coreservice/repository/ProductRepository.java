package com.example.coreservice.repository;

import com.example.coreservice.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // Spring Data JPA автоматически создаст реализацию
}