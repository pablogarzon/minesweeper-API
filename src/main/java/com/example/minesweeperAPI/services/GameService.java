package com.example.minesweeperAPI.services;

import com.example.minesweeperAPI.dto.MoveResultDTO;
import com.example.minesweeperAPI.models.Game;

public interface GameService {
	
	public Game create(int columns, int rows, int mines);
	
	public MoveResultDTO start(int gameId, int col, int row);
	
	public MoveResultDTO move(int gameId, int col, int row);
	
	public void pause(int gameId, long time);
	
	public void resume(int gameId);	
}
