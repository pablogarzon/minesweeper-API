package com.example.minesweeperAPI.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.minesweeperAPI.dto.UserDTO;
import com.example.minesweeperAPI.models.User;
import com.example.minesweeperAPI.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {
	
	private final UserService service;

	@Operation(summary = "Get all users")
	@GetMapping("/users")
	public List<User> getUsers() {
		return service.findAll();
	}
	
	@Operation(summary = "Create a new user")
	@PostMapping("/users")
	public User createUser(@RequestBody UserDTO dto) {
		return service.create(dto);
	}
}
