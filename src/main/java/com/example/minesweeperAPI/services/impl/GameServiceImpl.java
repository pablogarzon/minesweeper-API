package com.example.minesweeperAPI.services.impl;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.Consumer;

import org.springframework.stereotype.Service;

import com.example.minesweeperAPI.dto.UncoveredCellDTO;
import com.example.minesweeperAPI.dto.ActivatedCellResultDTO;
import com.example.minesweeperAPI.exceptions.BoardDimensionException;
import com.example.minesweeperAPI.exceptions.GameNotFoundException;
import com.example.minesweeperAPI.exceptions.InvalidCoordinatesException;
import com.example.minesweeperAPI.exceptions.InvalidOperationException;
import com.example.minesweeperAPI.exceptions.OperationNotAllowedException;
import com.example.minesweeperAPI.models.Cell;
import com.example.minesweeperAPI.models.CellCoordinates;
import com.example.minesweeperAPI.models.CellState;
import com.example.minesweeperAPI.models.Game;
import com.example.minesweeperAPI.models.GameState;
import com.example.minesweeperAPI.repository.GameRepository;
import com.example.minesweeperAPI.services.GameService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {
	
	private final GameRepository repository;
	
	private final SequenceGeneratorService sequenceGenerator;
	
	@Override
	public Game create(int columns, int rows, int mines) throws BoardDimensionException {
		if (mines > rows * columns) {
			throw new BoardDimensionException();
		}

		var game = Game.builder()
				.id(sequenceGenerator.generateSequence(Game.SEQUENCE_NAME))
				.rows(rows)
				.columns(columns)
				.mines(mines)
				.build();
		
		repository.save(game);
		
		return game;
	}
	
	@Override
	public ActivatedCellResultDTO start(int gameId, int col, int row) throws GameNotFoundException, InvalidCoordinatesException, InvalidOperationException {
		Game game = findGame(gameId);
		if (!game.getState().isNotStarted()) {
			throw InvalidOperationException.GameAlreadyStarted();
		}
		if (game.getColumns() -1 < col  || game.getRows() -1 < row) {
			throw new InvalidCoordinatesException();
		}
		
		var board = createMinefield(game, col, row);
		countMinesAroundCell(board);
		
		var uncoveredCells = new HashSet<UncoveredCellDTO>(); 
		uncoverCell(board, uncoveredCells, board[row][col]);
		
		game.incrementUncoveredCells(uncoveredCells.size());
		game.setBoard(board);
		game.startGame();
		checkVictory(game);
		repository.save(game);
		
		return new ActivatedCellResultDTO(game.getState().getState(), uncoveredCells);
	}
	
	@Override
	public ActivatedCellResultDTO move(int gameId, int col, int row) throws GameNotFoundException, InvalidCoordinatesException, OperationNotAllowedException, InvalidOperationException {
		var game = findGame(gameId);
		if (!game.getState().isActive()) {
			throw OperationNotAllowedException.GameIsNotStarted();
		}
		if (game.getColumns() -1 < col  || game.getRows() -1 < row) {
			throw new InvalidCoordinatesException();
		}		
		var cell = game.getBoard()[row][col];
		if (cell.getState().isUnCovered()) {
			throw InvalidOperationException.CellIsAlreadyUncovered();
		}
		
		final var uncoveredCells = new HashSet<UncoveredCellDTO>();
		
		if (checkGameOver(cell)) {
			uncoverMines(game.getBoard(), uncoveredCells);
			game.endGame(GameState.FAILED);
		} else {
			uncoverCell(game.getBoard(), uncoveredCells, cell);
			game.incrementUncoveredCells(uncoveredCells.size());
			if (checkVictory(game)) {
				game.endGame(GameState.VICTORY);
			}
		}
		repository.save(game);
		
		return new ActivatedCellResultDTO(game.getState().getState(), uncoveredCells);
	}

	@Override
	public void pause(int gameId, long time) throws GameNotFoundException, OperationNotAllowedException {
		var previous = findGame(gameId);
		if (!previous.getState().isActive()) {
			throw OperationNotAllowedException.GameIsNotActive();
		}
		repository.updateGameToPaused(gameId, time);
	}

	@Override
	public void resume(int gameId) throws GameNotFoundException, InvalidOperationException, OperationNotAllowedException {
		var previous = findGame(gameId);
		if (!previous.getState().isPaused()) {
			throw OperationNotAllowedException.GameIsNotActive();
		} 
		if (previous.getState().isActive()) {
			throw InvalidOperationException.GameIsAlreadyActive();
		}
		repository.updateGameToActive(gameId);
	}
	
	@Override
	public void updateCellState(int gameId, int col, int row, CellState state) throws OperationNotAllowedException, GameNotFoundException, InvalidCoordinatesException {
		var previousState = repository.findCellPreviousState(gameId, new CellCoordinates(col, row));

		boolean isfromCoveredToRedFlag = previousState.isCovered() && state.isMarkedWithFlag();		
		boolean isfromRedFlagToQuestion = previousState.isMarkedWithFlag() && state.isMarkedWithQuestion();
		if (isfromCoveredToRedFlag || isfromRedFlagToQuestion || !previousState.isUnCovered()) {
			repository.updateCellState(gameId, new CellCoordinates(col, row), state);
		} else {
			throw OperationNotAllowedException.CellStateChangeNotAllowed();
		}
		
	}
	
	private Game findGame(int gameId) throws GameNotFoundException {
		Game game = null;
		try {
			game = repository.findById(gameId).get();
		} catch (NoSuchElementException e) {
			throw new GameNotFoundException();
		}
		return game;
	}

	private Cell[][] createMinefield(Game game, int xFirstRevealed, int yFirstRevealed) {
		int rows = game.getRows();
		int cols = game.getColumns();
		int mines = game.getMines();
		
		var board = new Cell[rows][cols];
		int y, x = 0;

		for (int i = 0; i < mines; i++) {
			do {
				y = (int) (Math.random() * (rows));
				x = (int) (Math.random() * (cols));
			} while (board[y][x] != null || (x == xFirstRevealed && y == yFirstRevealed));

			// cell with mine
			var cell = Cell.builder() 
				.coordinates(new CellCoordinates(x, y))
				.hasMine(true)
				.build();

			board[y][x] = cell;
		}
		
		return board;
	}

	private void countMinesAroundCell(Cell[][] board) {
		int columns = board[0].length;
		int rows = board.length;
		
		for (int y = 0; y < rows; y++) {
			for (int x = 0; x < columns; x++) {

				if (board[y][x] != null && board[y][x].isHasMine()) {
					continue;
				}
				
				var cell = Cell.builder() 
						.coordinates(new CellCoordinates(x, y))
						.hasMine(false)
						.build();
				
				inspectAdjacentCells(board, cell, (currentCell) -> {
					if (currentCell != null && currentCell.isHasMine()) {
						cell.addMinesAround();
					}
				});				

				board[y][x] = cell;
			}
		}
	}
	
	private void uncoverMines(Cell[][] board, HashSet<UncoveredCellDTO> uncoveredCells) {
		int columns = board[0].length;
		int rows = board.length;
		
		for (int y = 0; y < rows; y++) {
			for (int x = 0; x < columns; x++) {
				var cell = board[y][x];
				if (board[y][x].isHasMine()) {
					var move = new UncoveredCellDTO(cell.getCoordinates(), cell.getValue());
					uncoveredCells.add(move);
				}
			}
		}
	}
	
	private void uncoverCell(Cell[][] board, Set<UncoveredCellDTO> uncoveredCells, Cell cell) {
		if (cell.getState().isUnCovered()) {
			return;
		}
		cell.setState(CellState.UNCOVERED);
		
		var move = new UncoveredCellDTO(cell.getCoordinates(), cell.getValue());
		uncoveredCells.add(move);
		
		if (cell.getValue() == 0 && !cell.isHasMine()) {
			inspectAdjacentCells(board, cell, (current) -> {
				uncoverCell(board, uncoveredCells, current);
			});
		}
	}

	private void inspectAdjacentCells(Cell[][] board, Cell current, Consumer<Cell> callback) {
		int columns = board[0].length;
		int rows = board.length;
		
		int x = current.getCoordinates().getX();
		int y = current.getCoordinates().getY();
		
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
	
	private boolean checkGameOver(Cell cell) {
		return cell.isHasMine();
	}
	
	private boolean checkVictory(Game game) {
		return game.isGameWon();
	}
}
