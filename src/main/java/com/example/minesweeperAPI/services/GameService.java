package com.example.minesweeperAPI.services;

import com.example.minesweeperAPI.models.Cell;
import com.example.minesweeperAPI.models.Game;
import com.example.minesweeperAPI.models.GameState;

public interface GameService {
	
	public Game create(int rows, int cols, int mines);
	
	public Cell[][] start(int gameId);
	
	public Cell[][] uncoverCell(int row, int col);
	
	public void pause(int gameId, long time);
	
	public void resume(int gameId);
	
	public void saveResult(int gameId, GameState gameState);
	
	
}
