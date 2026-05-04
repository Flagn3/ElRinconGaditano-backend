package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Order;
import com.example.demo.model.OrderRequest;

public interface OrderService {

	List<Order> getAllOrders();

	List<Order> getByUser(Long userId);

	Order createOrder(OrderRequest request);

	Order getById(Long id);

	Order updateStatus(Long id, String status);

	void cancelOrder(Long id);

	List<Order> getByStatus(String status);

}
