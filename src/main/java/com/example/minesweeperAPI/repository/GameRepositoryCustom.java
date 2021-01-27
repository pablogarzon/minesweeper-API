package com.example.minesweeperAPI.repository;

public interface GameRepositoryCustom {
	
	void updateGameToPaused(int gameId, long time);
	
	void updateGameToActive(int gameId);
}
