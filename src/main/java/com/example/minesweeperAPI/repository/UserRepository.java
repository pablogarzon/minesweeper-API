package com.example.minesweeperAPI.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.minesweeperAPI.models.User;

public interface UserRepository extends MongoRepository<User, String> {

}
