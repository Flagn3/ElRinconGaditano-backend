package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Category;
import com.example.demo.model.ApiResponse;
import com.example.demo.service.CategoryService;

@RestController
@RequestMapping("/categories")
public class CategoryController {

	@Autowired
	@Qualifier("categoryService")
	private CategoryService categoryService;

	// GET /categories
	@GetMapping
	public ResponseEntity<?> getAllCategories() {
		List<Category> categories = categoryService.getAllCategories();
		return ResponseEntity.ok(new ApiResponse<>(true, categories, "Categories retrieved successfully."));
	}

	// GET /categories/{id}
	public ResponseEntity<?> getCategoryById(@PathVariable Long id) {
		try {
			Category category = categoryService.getById(id);
			return ResponseEntity.ok(new ApiResponse<>(true, category, "Category retrieved successfully."));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, null, e.getMessage()));
		}
	}

	// POST /categories
	@PostMapping
	public ResponseEntity<?> createCategory(@RequestBody Category category) {
		try {
			Category newCategory = categoryService.createCategory(category);
			return ResponseEntity.status(HttpStatus.CREATED)
					.body(new ApiResponse<>(true, newCategory, "Category created successfully."));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false, null, e.getMessage()));
		}
	}

	// PUT /categories/{id}
	@PutMapping("/{id}")
	public ResponseEntity<?> updateCategory(@PathVariable Long id, @RequestBody Category category) {
		try {
			Category updatedCategory = categoryService.updateCategory(id, category);
			return ResponseEntity.ok(new ApiResponse<>(true, updatedCategory, "Category updated successfully."));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false, null, e.getMessage()));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, null, e.getMessage()));
		}
	}

	// DELETE /categories/{id}
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
		try {
			categoryService.deleteCategory(id);
			return ResponseEntity.ok(new ApiResponse<>(true, null, "Category deleted successfully."));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, null, e.getMessage()));
		}
	}

}
