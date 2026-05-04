package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Order;
import com.example.demo.model.ApiResponse;
import com.example.demo.model.OrderRequest;
import com.example.demo.service.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController {

	@Autowired
	@Qualifier("orderService")
	private OrderService orderService;

	// GET /orders
	@GetMapping
	public ResponseEntity<?> getAllOrders() {
		List<Order> orders = orderService.getAllOrders();
		return ResponseEntity.ok(new ApiResponse<>(true, orders, "Orders retrieved successfully."));
	}

	// GET /orders/user/{userId}
	@GetMapping("/user/{userId}")
	public ResponseEntity<?> getOrdersByUser(@PathVariable Long userId) {
		try {
			List<Order> orders = orderService.getByUser(userId);
			return ResponseEntity.ok(new ApiResponse<>(true, orders, "Orders retrieved successfully."));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, null, e.getMessage()));
		}
	}

	// GET /orders/{id}

	// GET /orders/status/{status}

	// POST /orders
	@PostMapping
	public ResponseEntity<?> createOrder(@RequestBody OrderRequest request) {

		try {
			Order newOrder = orderService.createOrder(request);
			return ResponseEntity.status(HttpStatus.CREATED)
					.body(new ApiResponse<>(true, newOrder, "Order created successfully."));

		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false, null, e.getMessage()));
		}

	}

	// PUT /orders/{id}/status

	// PUT /orders/{id}/cancel

}
