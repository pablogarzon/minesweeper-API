package com.example.minesweeperAPI.repository;

import java.util.Optional;

import com.example.minesweeperAPI.models.Game;

public interface GameRepository {

	Optional<Game> findById(Object any);

	Game save(Game game);
	
}
