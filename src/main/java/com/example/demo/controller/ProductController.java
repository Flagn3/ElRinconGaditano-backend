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

}
