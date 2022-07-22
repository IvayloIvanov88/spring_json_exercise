package com.example.demo.repositories;

import com.example.demo.models.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

Optional<List<Product>>findByNameAndPrice(String name, BigDecimal price);

List<Product> findAllByPriceBetweenAndBuyerIsNull(BigDecimal lower, BigDecimal higher);

}
