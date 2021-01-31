package com.example.minesweeperAPI.repository;

import com.example.minesweeperAPI.exceptions.GameNotFoundException;
import com.example.minesweeperAPI.exceptions.InvalidCoordinatesException;
import com.example.minesweeperAPI.models.CellCoordinates;
import com.example.minesweeperAPI.models.CellState;

public interface GameRepositoryCustom {
	
	void updateGameToPaused(long gameId, long time);
	
	void updateGameToActive(long gameId);
	
	void updateCellState(long gameId, CellCoordinates coordinates, CellState cellState);
	
	CellState findCellPreviousState(long gameId, CellCoordinates coordinates) throws GameNotFoundException, InvalidCoordinatesException;
}
