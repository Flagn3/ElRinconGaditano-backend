package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Category;

public interface CategoryService {

	List<Category> getAllCategories();

	Category getById(Long id);

	Category getByName(String name);

	Category createCategory(Category category);

	Category updateCategory(Long id, Category category);

	void deleteCategory(Long id);

}
