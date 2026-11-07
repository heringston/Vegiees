package com.taskflow.vegiees.repository;

import com.taskflow.vegiees.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
