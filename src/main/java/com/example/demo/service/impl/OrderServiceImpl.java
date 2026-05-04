package com.example.demo.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Order;
import com.example.demo.entity.OrderLine;
import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import com.example.demo.model.OrderLineRequest;
import com.example.demo.model.OrderRequest;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.OrderService;

@Service("orderService")
public class OrderServiceImpl implements OrderService {

	@Autowired
	@Qualifier("orderRepository")
	private OrderRepository orderRepository;

	@Autowired
	@Qualifier("productRepository")
	private ProductRepository productRepository;

	@Autowired
	@Qualifier("userRepository")
	private UserRepository userRepository;

	@Override
	public List<Order> getAllOrders() {
		return orderRepository.findAll();
	}

	@Override
	public List<Order> getByUser(Long userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
		return orderRepository.findByUser(user);
	}

	@Override
	public Order createOrder(OrderRequest request) {

		Order order = new Order();
		order.setDate(LocalDateTime.now());
		order.setStatus("PENDING");

		User user = userRepository.findById(request.getUserId())
				.orElseThrow(() -> new RuntimeException("User not found"));
		order.setUser(user);

		List<OrderLine> lines = new ArrayList<>();
		double totalPrice = 0.0;

		for (OrderLineRequest item : request.getItems()) {
			Product product = productRepository.findById(item.getProductId())
					.orElseThrow(() -> new RuntimeException("Product not found"));

			OrderLine line = new OrderLine();
			line.setOrder(order);
			line.setProduct(product);
			line.setAmount(item.getAmount());
			line.setUnitPrice(product.getPrice());

			lines.add(line);
			totalPrice += product.getPrice() * item.getAmount();
		}

		order.setLines(lines);
		order.setTotalPrice(totalPrice);
		return orderRepository.save(order);
	}

	@Override
	public Order getById(Long id) {
		return orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
	}

	@Override
	public Order updateStatus(Long id, String status) {
		Order order = getById(id);
		order.setStatus(status);
		return orderRepository.save(order);
	}

	@Override
	public void cancelOrder(Long id) {
		Order order = getById(id);
		if (!order.getStatus().equalsIgnoreCase("PENDING")) {
			throw new RuntimeException("Only pending orders can be cancelled");
		}
		order.setStatus("CANCELLED");
		orderRepository.save(order);
	}

	@Override
	public List<Order> getByStatus(String status) {
		return orderRepository.findByStatus(status);
	}

}
