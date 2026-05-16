package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtTokenProvider;

@Service
public class AuthService {

	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private JwtTokenProvider tokenProvider;
	@Autowired
	private EmailService emailService;

	public String login(String email, String password) {

		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("Invalid username or password"));

		if (user.isDeleted() || !user.isActivated()) {
			throw new RuntimeException("User is deleted or deactivated");
		}
		if (!user.isVerified()) {
			throw new RuntimeException("Please, verify your email before logging in");
		}

		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(email, password));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		return tokenProvider.generateToken(authentication);
	}

	public User register(User user) {
		if (userRepository.findByEmail(user.getEmail()).isPresent()) {
			throw new RuntimeException("Email is already registered!");
		}

		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRole("ROLE_USER"); // Default role
		user.setVerified(false);
		user.setActivated(true);
		user.setDeleted(false);
		user.setPoints(0);

		User savedUser = userRepository.save(user);

		String verificationToken = tokenProvider.generateTokenFromEmail(savedUser.getEmail());
		emailService.sendVerificationEmail(savedUser.getEmail(), savedUser.getName(), verificationToken);

		return savedUser;
	}

	public void verifyTokenAndActivateUser(String token) {
		try {
			String email = tokenProvider.getEmailFromJWT(token);
			User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
			user.setVerified(true);
			userRepository.save(user);
		} catch (Exception e) {
			throw new RuntimeException("The link is invalid or has expired");
		}
	}

}
