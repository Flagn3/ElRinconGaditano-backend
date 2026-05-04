package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
	@GetMapping("/category/{category}")
	public ResponseEntity<?> getAvailableByCategory(@PathVariable String category) {
		List<Product> products = productService.getAvailableByCategory(category);
		return ResponseEntity
				.ok(new ApiResponse<>(true, products, "Products for category " + category + " retrieved."));
	}

	// GET /products/admin/category/{category}
	@GetMapping("/admin/category/{category}")
	public ResponseEntity<?> getByCategory(@PathVariable String category) {
		List<Product> products = productService.getByCategory(category);
		return ResponseEntity
				.ok(new ApiResponse<>(true, products, "Products for category " + category + " retrieved."));
	}

	// POST /products
	@PostMapping
	public ResponseEntity<?> createProduct(@RequestBody Product product) {
		try {
			Product newProduct = productService.createProduct(product);
			return ResponseEntity.status(HttpStatus.CREATED)
					.body(new ApiResponse<>(true, newProduct, "Product created successfully."));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false, null, e.getMessage()));
		}
	}

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
	@PutMapping("/{id}")
	public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody Product product) {
		try {
			Product updatedProduct = productService.updateProduct(id, product);
			return ResponseEntity.ok(new ApiResponse<>(true, updatedProduct, "Product updated successfully."));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false, null, e.getMessage()));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, null, e.getMessage()));
		}
	}

	// PUT /products/{id}/switchAvailable
	@PutMapping("/{id}/switchAvailable")
	public ResponseEntity<?> switchAvailable(@PathVariable Long id) {
		try {
			productService.switchAvailable(id);
			return ResponseEntity.ok(new ApiResponse<>(true, null, "Product availability changed successfully."));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, null, e.getMessage()));
		}
	}

}
