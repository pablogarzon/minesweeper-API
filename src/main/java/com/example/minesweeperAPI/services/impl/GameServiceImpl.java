package com.example.minesweeperAPI.services.impl;

import org.springframework.stereotype.Service;

import com.example.minesweeperAPI.models.Cell;
import com.example.minesweeperAPI.models.Game;
import com.example.minesweeperAPI.models.GameState;
import com.example.minesweeperAPI.services.GameService;

@Service
public class GameServiceImpl implements GameService {

	@Override
	public Game start(int rows, int columns, int mines, int xFirstRevealed, int yFirstRevealed) {
		if (mines > rows * columns) {
			// throw error
		}
		
		var board = createMinefield(rows, columns, mines, xFirstRevealed, yFirstRevealed);
		
		countMinesAroundCell(board);

		var game = Game.builder()
				.id(1) //generated id
				.rows(rows)
				.columns(columns)
				.mines(mines)
				.board(board)
				.build();
		
		return game;
	}

	private Cell[][] createMinefield(int rows, int cols, int mines, int xFirstRevealed, int yFirstRevealed) {
		var board = new Cell[rows][cols];
		int y, x = 0;

		for (int i = 0; i < mines; i++) {
			do {
				y = (int) (Math.random() * (rows));
				x = (int) (Math.random() * (cols));
			} while (board[y][x] != null || (x == xFirstRevealed && y == yFirstRevealed));

			var cell = new Cell(y, x, true);

			board[y][x] = cell;
		}
		
		return board;
	}

	private void countMinesAroundCell(Cell[][] board) {
		int rows = board.length;
		int columns = board[0].length;

		for (int y = 0; y < rows; y++) {
			for (int x = 0; x < columns; x++) {

				if (board[y][x] != null && board[y][x].isHasMine()) {
					continue;
				}

				var cell = new Cell(y, x, false);
				
				int up = y - 1;
				int down = y + 1;
				int left = x - 1;
				int right = x + 1;
				
				if (up >= 0 && left >= 0) {
					if (board[up][left] != null && board[up][left].isHasMine()) {
						cell.addMinesAround();
					}
				}
				if (up >= 0) {
					if (board[up][x] != null && board[up][x].isHasMine()) {
						cell.addMinesAround();
					}
				}
				if (up >= 0 && right <= columns - 1) {
					if (board[up][right] != null && board[up][right].isHasMine()) {
						cell.addMinesAround();
					}
				}
				if (right <= columns - 1) {
					if (board[y][right] != null && board[y][right].isHasMine()) {
						cell.addMinesAround();
					}
				}
				if (down <= rows - 1 && right <= columns - 1) {
					if (board[down][right] != null && board[down][right].isHasMine()) {
						cell.addMinesAround();
					}
				}
				if (down <= rows - 1) {
					if (board[down][x] != null && board[down][x].isHasMine()) {
						cell.addMinesAround();
					}
				}
				if (down <= rows - 1  && left >= 0) {
					if (board[down][left] != null && board[down][left].isHasMine()) {
						cell.addMinesAround();
					}
				}
				if (left >= 0) {
					if (board[y][left] != null && board[y][left].isHasMine()) {
						cell.addMinesAround();
					}
				}

				board[y][x] = cell;
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
