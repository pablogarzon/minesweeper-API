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
		
		countMinesAroundCell(board);

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

			var cell = new Cell(y, x, true);

			board[y][x] = cell;
		}
		
		return board;
	}

	private void countMinesAroundCell(Cell[][] board) {
		int rows = board.length;
		int columns = board[0].length;

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {

				if (board[i][j] != null && board[i][j].isHasMine()) {
					continue;
				}

				var cell = new Cell(i, j, false);

				if (i > 0 && j > 0) {
					if (board[i - 1][j - 1] != null && board[i - 1][j - 1].isHasMine()) {
						cell.addMinesAround();
					}
				}

				if (i > 0) {
					if (board[i - 1][j] != null && board[i - 1][j].isHasMine()) {
						cell.addMinesAround();
					}
				}

				if (i > 0 && j < columns - 1) {
					if (board[i - 1][j + 1] != null && board[i - 1][j + 1].isHasMine()) {
						cell.addMinesAround();
					}
				}

				if (j > 0) {
					if (board[i][j - 1] != null && board[i][j - 1].isHasMine()) {
						cell.addMinesAround();
					}
				}

				if (j < columns - 1) {
					if (board[i][j + 1] != null && board[i][j + 1].isHasMine()) {
						cell.addMinesAround();
					}
				}

				if (i < rows - 1 && j > 0) {
					if (board[i + 1][j - 1] != null && board[i + 1][j - 1].isHasMine()) {
						cell.addMinesAround();
					}
				}

				if (i < rows - 1) {
					if (board[i + 1][j] != null && board[i + 1][j].isHasMine()) {
						cell.addMinesAround();
					}
				}

				if (i < rows - 1 && j < columns - 1) {
					if (board[i + 1][j + 1] != null && board[i + 1][j + 1].isHasMine()) {
						cell.addMinesAround();
					}
				}

				board[i][j] = cell;
			}
		}

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