package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.User;
import com.example.demo.model.ApiResponse;
import com.example.demo.model.UpdateUserRequest;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	@Qualifier("userService")
	private UserService userService;

	// GET /users
	@GetMapping
	public ResponseEntity<?> getAllUsers() {
		List<User> users = userService.listAllUsers();
		return ResponseEntity.ok(new ApiResponse<>(true, users, "Users retrieved successfully."));
	}

	// GET /users/{id}
	@GetMapping("/{id}")
	public ResponseEntity<?> getUserById(@PathVariable Long id) {
		try {
			User user = userService.getUserById(id);
			return ResponseEntity.ok(new ApiResponse<>(true, user, "User retrieved successfully"));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, null, e.getMessage()));
		}
	}

	// DELETE /users
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable Long id) {
		try {
			userService.deleteUser(id);
			return ResponseEntity.ok(new ApiResponse<>(true, null, "User deleted successfully."));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, null, e.getMessage()));
		}
	}

	// PUT /users/{id}/activate
	@PutMapping("/{id}/activate")
	public ResponseEntity<?> activateUser(@PathVariable Long id) {
		try {
			userService.activateUser(id);
			return ResponseEntity.ok(new ApiResponse<>(true, null, "User activated successfully"));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, null, e.getMessage()));
		}
	}

	// PUT /users/{id}/deactivate
	@PutMapping("/{id}/deactivate")
	public ResponseEntity<?> deactivateUser(@PathVariable Long id) {
		try {
			userService.deactivateUser(id);
			return ResponseEntity.ok(new ApiResponse<>(true, null, "User deactivated successfully"));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, null, e.getMessage()));
		}
	}

	// PUT /users/{id}
	@PutMapping("/{id}")
	public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UpdateUserRequest request) {
		try {
			User updateUser = userService.updateUser(id, request);
			return ResponseEntity.ok(new ApiResponse<>(true, updateUser, "User updated successfully"));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, null, e.getMessage()));
		}
	}

}
