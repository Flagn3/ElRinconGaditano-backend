package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Product;
import com.example.demo.model.ApiResponse;
import com.example.demo.service.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {

	@Autowired
	@Qualifier("productService")
	private ProductService productService;

	// GET /products
	@GetMapping
	public ResponseEntity<?> getAllProducts() {
		List<Product> products = productService.getAllProducts();
		return ResponseEntity.ok(new ApiResponse<>(true, products, "Products retrieved successfully."));
	}

	// GET /products/available
	@GetMapping("/available")
	public ResponseEntity<?> getAvailableProducts() {
		List<Product> products = productService.getAvailable();
		return ResponseEntity.ok(new ApiResponse<>(true, products, "Products retrieved successfully."));
	}

	// GET /products/{id}
	@GetMapping("/{id}")
	public ResponseEntity<?> getProductById(@PathVariable Long id) {
		try {
			Product product = productService.getById(id);
			return ResponseEntity.ok(new ApiResponse<>(true, product, "Product retrieved successfully."));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, null, e.getMessage()));
		}
	}

	// GET /products/category/{category}

	// GET /products/admin/category/{category}

	// POST /products

	// DELETE /products/{id}
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
		try {
			productService.deleteProduct(id);
			return ResponseEntity.ok(new ApiResponse<>(true, null, "Product deleted successfully."));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, null, e.getMessage()));
		}
	}

	// PUT /products/{id}

	// PUT /products/{id}/switchAvailable

}
