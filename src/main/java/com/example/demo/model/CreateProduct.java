package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateProduct {

	private String name;

	private String description;

	private Double price;

	private boolean available;

	private Long categoryId;

	private String image;

}
