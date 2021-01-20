package com.example.minesweeperAPI.services.impl;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import org.springframework.stereotype.Service;

import com.example.minesweeperAPI.models.Cell;
import com.example.minesweeperAPI.models.CellState;
import com.example.minesweeperAPI.models.Game;
import com.example.minesweeperAPI.models.GameState;
import com.example.minesweeperAPI.services.GameService;

@Service
public class GameServiceImpl implements GameService {

	private Game testgame;
	
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
				
				lookCellsAround(x, y, board, (currentCell) -> {
					if (currentCell != null && currentCell.isHasMine()) {
						cell.addMinesAround();
					}
				});
				

				board[y][x] = cell;
			}
		}

	}

	private void lookCellsAround(int x, int y, Cell[][] board, Consumer<Cell> callback) {
		int rows = board.length;
		int columns = board[0].length;
		
		int up = y - 1;
		int down = y + 1;
		int left = x - 1;
		int right = x + 1;
		
		if (up >= 0 && left >= 0) {
			callback.accept(board[up][left]);
		}
		if (up >= 0) {
			callback.accept(board[up][x]);
		}
		if (up >= 0 && right <= columns - 1) {
			callback.accept(board[up][right]);
		}
		if (right <= columns - 1) {
			callback.accept(board[y][right]);
		}
		if (down <= rows - 1 && right <= columns - 1) {
			callback.accept(board[down][right]);
		}
		if (down <= rows - 1) {
			callback.accept(board[down][x]);
		}
		if (down <= rows - 1  && left >= 0) {
			callback.accept(board[down][left]);
		}
		if (left >= 0) {
			callback.accept(board[y][left]);
		}
	}


	@Override
	public Set<Cell> uncoverCell(int gameId, int row, int col) {
		var game = getCurrentGame(gameId);
		
		var cell = game.getBoard()[row][col];
		
		final Set<Cell> cells = new HashSet<>();
		
		cell.setState(CellState.UNCOVERED);
		cells.add(cell);
		
		if (cell.getValue() == 0 && !cell.isHasMine()) {			
			lookCellsAround(row, col, game.getBoard(), (currentCell) -> {
				cells.addAll(uncoverCell(gameId, currentCell.getRow(), currentCell.getColumn()));
			});
		}
		
		return cells;
	}

	private Game getCurrentGame(int gameId) {
		if (this.testgame != null) {
			return this.testgame;
		}
		var board = new Cell[][] {
			{null, null, new Cell(0, 2, true)},
			{null, null, null},
			{new Cell(2, 0, true), null, null},
		};
		
		countMinesAroundCell(board);
		
		var game = Game.builder()
				.id(1) //generated id
				.rows(3)
				.columns(3)
				.mines(2)
				.board(board)
				.build();
		this.testgame = game;
		return game;
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
