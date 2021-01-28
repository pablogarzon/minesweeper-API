package com.example.minesweeperAPI.repository;

import com.example.minesweeperAPI.exceptions.MineSweeperException;
import com.example.minesweeperAPI.models.CellCoordinates;
import com.example.minesweeperAPI.models.CellState;

public interface GameRepositoryCustom {
	
	void updateGameToPaused(int gameId, long time);
	
	void updateGameToActive(int gameId);
	
	void updateCellState(int gameId, CellCoordinates coordinates, CellState cellState);
	
	CellState findCellPreviousState(int gameId, CellCoordinates coordinates) throws MineSweeperException;
}
