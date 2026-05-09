package com.example.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Category;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.service.CategoryService;

@Service("categoryService")
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	@Qualifier("categoryRepository")
	private CategoryRepository categoryRepository;

	@Override
	public List<Category> getAllCategories() {
		return categoryRepository.findAll();
	}

	@Override
	public Category getById(Long id) {
		return categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
	}

	@Override
	public Category getByName(String name) {
		return categoryRepository.findByName(name).orElseThrow(() -> new RuntimeException("Category not found"));
	}

	@Override
	public Category createCategory(Category category) {
		if (categoryRepository.existsByName(category.getName())) {
			throw new IllegalArgumentException("Category already exists with this name");
		}
		return categoryRepository.save(category);
	}

	@Override
	public Category updateCategory(Long id, Category categoryData) {
		Category category = getById(id);

		if (!categoryData.getName().equalsIgnoreCase(category.getName())
				&& categoryRepository.existsByName(categoryData.getName())) {
			throw new IllegalArgumentException("Category already exists with this name");
		}
		category.setName(categoryData.getName());
		category.setImage(categoryData.getImage());

		return categoryRepository.save(category);
	}

	@Override
	public void deleteCategory(Long id) {
		Category category = getById(id);
		categoryRepository.delete(category);
	}

}
