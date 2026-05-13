package com.example.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.example.demo.entity.User;
import com.example.demo.model.UpdateUserRequest;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;

@Service("userService")
public class UserServiceImpl implements UserService {

	@Autowired
	@Qualifier("userRepository")
	private UserRepository userRepository;

	@Override
	public List<User> listAllUsers() {
		return userRepository.findAll();
	}

	@Override
	public User getUserById(Long id) {
		return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
	}

	@Override
	public User getUserByEmail(String email) {
		return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
	}

	@Override
	public User updateUser(Long id, UpdateUserRequest request) {
		User u = getUserById(id);
		u.setName(request.getName());
		u.setSecondName(request.getSecondName());
		u.setAddress(request.getAddress());
		return userRepository.save(u);
	}

	@Override
	public void deleteUser(Long id) {
		User user = getUserById(id);
		user.setDeleted(true);
		userRepository.save(user);
	}

	@Override
	public void activateUser(Long id) {
		User user = getUserById(id);
		user.setActivated(true);
		userRepository.save(user);
	}

	@Override
	public void deactivateUser(Long id) {
		User user = getUserById(id);
		user.setActivated(false);
		userRepository.save(user);
	}

}
