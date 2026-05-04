package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
	@GetMapping("/{id}")
	public ResponseEntity<?> getOrderById(@PathVariable Long id) {
		try {
			Order order = orderService.getById(id);
			return ResponseEntity.ok(new ApiResponse<>(true, order, "Order retrieved successfully."));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, null, e.getMessage()));
		}
	}

	// GET /orders/status/{status}
	@GetMapping("/status/{status}")
	public ResponseEntity<?> getOrdersByUser(@PathVariable String status) {
		try {
			List<Order> orders = orderService.getByStatus(status);
			return ResponseEntity.ok(new ApiResponse<>(true, orders, "Orders retrieved successfully."));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ApiResponse<>(false, null, e.getMessage()));
		}
	}

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
	@PutMapping("/{id}/status")
	public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
		try {
			String status = body.get("status").toUpperCase();
			Order updatedOrder = orderService.updateStatus(id, status);
			return ResponseEntity.ok(new ApiResponse<>(true, updatedOrder, "Order status updated successfully."));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, null, e.getMessage()));
		}
	}

	// PUT /orders/{id}/cancel
	@PutMapping("/{id}/cancel")
	public ResponseEntity<?> cancelOrder(@PathVariable Long id, Authentication authentication) {
		try {
			Order order = orderService.getById(id);
			String email = authentication.getName();
			if (!order.getUser().getEmail().equals(email)) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN)
						.body(new ApiResponse<>(false, null, "You don't have permissions to cancel this order."));
			}

			orderService.cancelOrder(id);
			return ResponseEntity.ok(new ApiResponse<>(true, null, "Order cancelled successfully."));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false, null, e.getMessage()));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, null, e.getMessage()));
		}
	}

}
