package com.example.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Category;
import com.example.demo.entity.Product;
import com.example.demo.model.CreateProduct;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.ProductService;

@Service("productService")
public class ProductServiceImpl implements ProductService {

	@Autowired
	@Qualifier("productRepository")
	private ProductRepository productRepository;

	@Autowired
	@Qualifier("categoryRepository")
	private CategoryRepository categoryRepository;

	@Override
	public List<Product> getAllProducts() {
		return productRepository.findAll();
	}

	@Override
	public List<Product> getByCategory(String category) {
		return productRepository.findByCategoryName(category);
	}

	@Override
	public List<Product> getAvailable() {
		return productRepository.findByAvailableTrue();
	}

	@Override
	public List<Product> getAvailableByCategory(String category) {
		return productRepository.findByCategoryNameAndAvailableTrue(category);
	}

	@Override
	public Product getById(Long id) {
		return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
	}

	@Override
	public Product createProduct(CreateProduct product) {
		if (product.getPrice() < 0) {
			throw new IllegalArgumentException("Price can't be negative");
		}

		Category category = categoryRepository.findById(product.getCategoryId())
				.orElseThrow(() -> new RuntimeException("Category not found"));

		Product newProduct = new Product();
		newProduct.setName(product.getName());
		newProduct.setDescription(product.getDescription());
		newProduct.setPrice(product.getPrice());
		newProduct.setAvailable(product.isAvailable());
		newProduct.setImage(product.getImage());
		newProduct.setCategory(category);

		return productRepository.save(newProduct);
	}

	@Override
	public Product updateProduct(Long id, CreateProduct productData) {
		Product product = getById(id);
		if (productData.getPrice() < 0) {
			throw new IllegalArgumentException("Price can't be negative");
		}

		Category category = categoryRepository.findById(productData.getCategoryId())
				.orElseThrow(() -> new RuntimeException("Category not found"));

		product.setName(productData.getName());
		product.setDescription(productData.getDescription());
		product.setPrice(productData.getPrice());
		product.setCategory(category);

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
