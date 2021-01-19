package com.example.minesweeperAPI.services.impl;

import org.springframework.stereotype.Service;

import com.example.minesweeperAPI.models.Cell;
import com.example.minesweeperAPI.models.Game;
import com.example.minesweeperAPI.models.GameState;
import com.example.minesweeperAPI.services.GameService;

@Service
public class GameServiceImpl implements GameService {

	@Override
	public Game start(int rows, int columns, int mines) {
		if (mines > rows * columns) {
			// throw error
		}
		
		var board = createMinefield(rows, columns, mines);
		
		var game = Game.builder()
				.rows(rows)
				.columns(columns)
				.mines(mines)
				.board(board)
				.build();
		
		return game;
	}

	private Cell[][] createMinefield(int rows, int cols, int mines) {
		var board = new Cell[rows][cols];
		int y, x = 0;
		
		for (int i = 0; i < mines; i++) {
			do {
				y = (int) (Math.random() * (rows));
				x = (int) (Math.random() * (cols));
			} while (board[y][x] != null);

			var square = new Cell(y, x, true);

			board[y][x] = square;
		}
		
		return board;
	}

	@Override
	public Cell[][] uncoverCell(int gameId, int row, int col) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void pause(int gameId, long time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume(int gameId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveResult(int gameId, GameState gameState) {
		// TODO Auto-generated method stub
		
	}

}
