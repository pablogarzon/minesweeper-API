package com.example.minesweeperAPI.services;

import com.example.minesweeperAPI.dto.MoveResultDTO;
import com.example.minesweeperAPI.exceptions.MineSweeperException;
import com.example.minesweeperAPI.models.CellState;
import com.example.minesweeperAPI.models.Game;

public interface GameService {
	
	public Game create(int columns, int rows, int mines) throws MineSweeperException;
	
	public MoveResultDTO start(int gameId, int col, int row) throws MineSweeperException;
	
	public MoveResultDTO move(int gameId, int col, int row) throws MineSweeperException;
	
	public void pause(int gameId, long time) throws MineSweeperException;
	
	public void resume(int gameId) throws MineSweeperException;
	
	public void updateCellState(int gameId, int col, int row, CellState state) throws MineSweeperException;
}
