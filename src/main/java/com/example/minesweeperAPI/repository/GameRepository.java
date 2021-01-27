package com.example.minesweeperAPI.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.minesweeperAPI.models.Game;

public interface GameRepository extends MongoRepository<Game, Integer>, GameRepositoryCustom {
	
}
