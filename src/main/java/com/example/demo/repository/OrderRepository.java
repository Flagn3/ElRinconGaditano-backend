package com.example.demo.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Order;
import java.util.List;
import com.example.demo.entity.User;

@Repository("orderRepository")
public interface OrderRepository extends JpaRepository<Order, Serializable> {

	List<Order> findByUser(User user);

	List<Order> findByStatus(String status);

}
