package com.example.minesweeperAPI.services;

import com.example.minesweeperAPI.dto.CellStateDTO;
import com.example.minesweeperAPI.dto.CreateGameDTO;
import com.example.minesweeperAPI.dto.PauseGameDTO;
import com.example.minesweeperAPI.dto.UncoverCellDTO;
import com.example.minesweeperAPI.dto.UncoverCellResultDTO;
import com.example.minesweeperAPI.exceptions.BoardDimensionException;
import com.example.minesweeperAPI.exceptions.GameNotFoundException;
import com.example.minesweeperAPI.exceptions.InvalidCoordinatesException;
import com.example.minesweeperAPI.exceptions.InvalidOperationException;
import com.example.minesweeperAPI.exceptions.OperationNotAllowedException;
import com.example.minesweeperAPI.models.Game;

public interface GameService {
	
	Game create(CreateGameDTO dto) throws BoardDimensionException;
	
	UncoverCellResultDTO uncoverCell(UncoverCellDTO dto) throws GameNotFoundException, InvalidCoordinatesException, OperationNotAllowedException, InvalidOperationException;
	
	void pause(long gameId, PauseGameDTO dto) throws GameNotFoundException, OperationNotAllowedException;
	
	void resume(long gameId) throws GameNotFoundException, InvalidOperationException, OperationNotAllowedException;
	
	void updateCellState(long gameId, CellStateDTO dto) throws OperationNotAllowedException, GameNotFoundException, InvalidCoordinatesException;
}
