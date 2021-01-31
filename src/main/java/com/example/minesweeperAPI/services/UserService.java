package com.example.minesweeperAPI.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.minesweeperAPI.dto.UserDTO;
import com.example.minesweeperAPI.models.User;
import com.example.minesweeperAPI.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository repository;
	
	private final SequenceGeneratorService sequenceGenerator;

	public User create(UserDTO dto) {
		var user = new User();
		user.setId(sequenceGenerator.generateSequence(User.SEQUENCE_NAME));
		user.setUser(dto.getUser());
		repository.insert(user);
		return user;
	}

	public List<User> findAll() {
		return repository.findAll();
	}
}
