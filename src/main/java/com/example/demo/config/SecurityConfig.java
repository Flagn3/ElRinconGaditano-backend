package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.demo.security.CustomUserDetailsService;
import com.example.demo.security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private CustomUserDetailsService userDetailsService;
	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(auth -> auth
						// all
						.requestMatchers("/auth/**").permitAll().requestMatchers(HttpMethod.GET, "/products/available")
						.permitAll().requestMatchers(HttpMethod.GET, "/products/category/*").permitAll()
						.requestMatchers(HttpMethod.GET, "/products/*").permitAll()
						// products admin
						.requestMatchers(HttpMethod.GET, "/products").hasRole("ADMIN")
						.requestMatchers(HttpMethod.GET, "/products/admin/**").hasRole("ADMIN")
						.requestMatchers(HttpMethod.POST, "/products").hasRole("ADMIN")
						.requestMatchers(HttpMethod.PUT, "/products/**").hasRole("ADMIN")
						.requestMatchers(HttpMethod.DELETE, "/products/**").hasRole("ADMIN")
						// orders admin
						.requestMatchers(HttpMethod.GET, "/orders").hasRole("ADMIN")
						.requestMatchers(HttpMethod.GET, "/orders/status/**").hasRole("ADMIN")
						.requestMatchers(HttpMethod.PUT, "/orders/*/status").hasRole("ADMIN")
						// users
						.requestMatchers(HttpMethod.GET, "/users").hasRole("ADMIN")
						.requestMatchers(HttpMethod.DELETE, "/users/**").hasRole("ADMIN")
						.requestMatchers(HttpMethod.PUT, "/users/*/activate").hasRole("ADMIN")
						.requestMatchers(HttpMethod.PUT, "/users/*/deactivate").hasRole("ADMIN")
						.requestMatchers(HttpMethod.GET, "/users/*").authenticated()
						.requestMatchers(HttpMethod.PUT, "/users/*").authenticated()
						// create orders
						.requestMatchers(HttpMethod.POST, "/orders").authenticated()
						.requestMatchers(HttpMethod.GET, "/orders/*").authenticated()
						.requestMatchers(HttpMethod.GET, "/orders/user/**").authenticated()
						.requestMatchers(HttpMethod.PUT, "/orders/*/cancel").authenticated()
						// other
						.anyRequest().authenticated())
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}