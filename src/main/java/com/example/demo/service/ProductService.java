package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Product;
import com.example.demo.model.CreateProduct;

public interface ProductService {

	List<Product> getAllProducts();

	List<Product> getByCategory(String category);

	List<Product> getAvailable();

	List<Product> getAvailableByCategory(String category);

	Product getById(Long id);

	Product createProduct(CreateProduct product);

	Product updateProduct(Long id, CreateProduct product);

	void deleteProduct(Long id);

	void switchAvailable(Long id);

}
