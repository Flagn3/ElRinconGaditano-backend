package com.example.demo.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Product;

@Repository("productRepository")
public interface ProductRepository extends JpaRepository<Product, Serializable> {

	List<Product> findByCategory(String category);

	List<Product> findByAvailableTrue();

	List<Product> findByCategoryAndAvailableTrue(String category);

}
