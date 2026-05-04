package com.example.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.ProductService;

@Service("productService")
public class ProductServiceImpl implements ProductService {

	@Autowired
	@Qualifier("productRepository")
	private ProductRepository productRepository;

	@Override
	public List<Product> getAllProducts() {
		return productRepository.findAll();
	}

	@Override
	public List<Product> getByCategory(String category) {
		return productRepository.findByCategory(category);
	}

	@Override
	public List<Product> getAvailable() {
		return productRepository.findByAvailableTrue();
	}

	@Override
	public List<Product> getAvailableByCategory(String category) {
		return productRepository.findByCategoryAndAvailableTrue(category);
	}

	@Override
	public Product getById(Long id) {
		return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
	}

	@Override
	public Product createProduct(Product product) {
		if (product.getPrice() < 0) {
			throw new IllegalArgumentException("Price can't be negative");
		}
		return productRepository.save(product);
	}

	@Override
	public Product updateProduct(Long id, Product productData) {
		Product product = getById(id);
		if (productData.getPrice() < 0) {
			throw new IllegalArgumentException("Price can't be negative");
		}

		product.setName(productData.getName());
		product.setDescription(productData.getDescription());
		product.setPrice(productData.getPrice());
		product.setCategory(productData.getCategory());

		return productRepository.save(product);
	}

	@Override
	public void deleteProduct(Long id) {
		Product product = getById(id);
		productRepository.delete(product);
	}

	@Override
	public void switchAvailable(Long id) {
		Product product = getById(id);
		product.setAvailable(!product.isAvailable());
		productRepository.save(product);
	}

}
