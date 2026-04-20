package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.model.ApiResponse;
import com.example.demo.model.RegisterRequest;
import com.example.demo.service.AuthService;
import com.example.demo.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private AuthService authService;

	@Autowired
	@Qualifier("userService")
	private UserService userService;

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody Map<String, String> request) {

		try {
			String email = request.get("email");
			String password = request.get("password");
			String token = authService.login(email, password);

			User user = userService.getUserByEmail(email);
			Map<String, Object> data = Map.of("id", user.getId(), "email", user.getEmail(), "role", user.getRole(),
					"token", token);
			return ResponseEntity.ok(new ApiResponse<>(true, data, "Login successful"));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(false, null, e.getMessage()));
		}
	}

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
		try {
			User newUser = new User();
			newUser.setName(request.getName());
			newUser.setSecondName(request.getSecondName());
			newUser.setEmail(request.getEmail());
			newUser.setPassword(request.getPassword());
			newUser.setAddress(request.getAddress());

			authService.register(newUser);

			Map<String, Object> data = Map.of("id", newUser.getId(), "email", newUser.getEmail(), "name",
					newUser.getName(), "secondName", newUser.getSecondName());
			return ResponseEntity.ok(new ApiResponse<>(true, data, "User registered successfully"));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false, null, e.getMessage()));
		}
	}
}
