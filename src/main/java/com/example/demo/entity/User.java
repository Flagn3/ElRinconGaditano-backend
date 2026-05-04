package com.example.demo.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String secondName;

	@Column(unique = true, nullable = false)
	private String email;

	@Column(nullable = false)
	@JsonProperty(access = Access.WRITE_ONLY)
	private String password;

	private String role;

	@Column(nullable = false)
	private String address;

	private boolean verified;

	private boolean activated;

	private boolean deleted;

	private int points;

}
