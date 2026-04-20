package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.User;
import com.example.demo.model.UpdateUserRequest;

public interface UserService {

	List<User> listAllUsers();

	User getUserById(Long id);

	User getUserByEmail(String email);

	User updateUser(Long id, UpdateUserRequest request);

	void deleteUser(Long id);

	void activateUser(Long id);

	void deactivateUser(Long id);

	void addPointsToUser(Long id, int points);

}
