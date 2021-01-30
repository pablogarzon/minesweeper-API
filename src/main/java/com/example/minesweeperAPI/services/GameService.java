package com.example.minesweeperAPI.services;

import com.example.minesweeperAPI.dto.ActivatedCellResultDTO;
import com.example.minesweeperAPI.exceptions.BoardDimensionException;
import com.example.minesweeperAPI.exceptions.GameNotFoundException;
import com.example.minesweeperAPI.exceptions.InvalidCoordinatesException;
import com.example.minesweeperAPI.exceptions.InvalidOperationException;
import com.example.minesweeperAPI.exceptions.OperationNotAllowedException;
import com.example.minesweeperAPI.models.CellState;
import com.example.minesweeperAPI.models.Game;

public interface GameService {
	
	Game create(int columns, int rows, int mines) throws BoardDimensionException;
	
	ActivatedCellResultDTO move(int gameId, int col, int row) throws GameNotFoundException, InvalidCoordinatesException, OperationNotAllowedException, InvalidOperationException;
	
	void pause(int gameId, long time) throws GameNotFoundException, OperationNotAllowedException;
	
	void resume(int gameId) throws GameNotFoundException, InvalidOperationException, OperationNotAllowedException;
	
	void updateCellState(int gameId, int col, int row, CellState state) throws OperationNotAllowedException, GameNotFoundException, InvalidCoordinatesException;
}
