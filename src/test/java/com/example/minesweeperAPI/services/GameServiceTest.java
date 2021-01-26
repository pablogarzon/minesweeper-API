package com.example.minesweeperAPI.services;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.example.minesweeperAPI.models.Cell;
import com.example.minesweeperAPI.models.CellCoordinates;
import com.example.minesweeperAPI.models.Game;
import com.example.minesweeperAPI.repository.GameRepository;
import com.example.minesweeperAPI.services.impl.GameServiceImpl;

public class GameServiceTest {
	
	private GameService service;
	
	private GameRepository repository;
	
	@BeforeEach
	public void init() {
		repository = Mockito.mock(GameRepository.class);
		
		service = new GameServiceImpl(repository);
	}

	private Game createTestGame() {
		
		Cell[][] board = {
			{
				Cell.builder()
					.coordinates(new CellCoordinates(0, 0))
					.hasMine(false)
					.value(0)
					.build(),
				Cell.builder()
					.coordinates(new CellCoordinates(1, 0))
					.hasMine(false)
					.value(2)
					.build(),
				new Cell(new CellCoordinates(2, 0), true)
			},
			{
				Cell.builder()
					.coordinates(new CellCoordinates(0, 1))
					.hasMine(false)
					.value(1)
					.build(),
				Cell.builder()
					.coordinates(new CellCoordinates(1, 1))
					.hasMine(false)
					.value(2)
					.build(),
				new Cell(new CellCoordinates(2, 1), true)
			},
			{
				new Cell(new CellCoordinates(0, 2), true),
				Cell.builder()
					.coordinates(new CellCoordinates(1, 2))
					.hasMine(false)
					.value(2)
					.build(),
				Cell.builder()
					.coordinates(new CellCoordinates(2, 2))
					.hasMine(false)
					.value(1)
					.build()
			}
		};
		
		var game = Game.builder()
				.id(1) //generated id
				.rows(3)
				.columns(3)
				.mines(2)
				.board(board)
				.build();
		
		return game;
	}
	
	@Test
	public void createGameTest() {		
		// given
		int value = 3;
		
		// when		
		when(repository.save(Mockito.any())).thenReturn(new Game(value, value,  value));
		
		var result = service.create(value, value, value);
		
		// then
		assertTrue(result != null);
		assertTrue(result.getMines() == 3);
	}
	
	@Test
	public void startGameTest() {
		// given
		int gameId = 1;
		int x = 2;
		int y = 0;
		var emptyGame = Game.builder()
				.id(1) //generated id
				.rows(3)
				.columns(3)
				.mines(2)
				.build();
		
		//when
		when(repository.findById(Mockito.any())).thenReturn(Optional.of(emptyGame));
		
		var result = service.start(gameId, x, y);
		
		// then
		assertTrue(result != null && result.size() > 0);
		assertTrue(!result.iterator().next().isHasMine());
	}
	
	@Test
	public void uncoverCellTest() {
		// given
		int gameId = 1;
		int x = 0;
		int y = 0;
		
		// when
		when(repository.findById(Mockito.any())).thenReturn(Optional.of(createTestGame()));
		
		var result = service.move(gameId, x, y);

		// then
		assertTrue(result != null && result.size() == 4);
		
		
		var cellWithTwoMinesAround = result.stream()
				.filter(c -> c.getCoordinates().getY() == 0 && c.getCoordinates().getX() == 1)
				.findFirst()
				.get();
		
		assertTrue(cellWithTwoMinesAround.getValue() == 2);
	}
	
	@Test
	public void uncoverCellWithOneMineAdjacentTest() {
		// given
		int gameId = 1;
		int x = 0;
		int y = 1;
		
		// when
		when(repository.findById(Mockito.any())).thenReturn(Optional.of(createTestGame()));
		
		var result = service.move(gameId, x, y);

		// then
		assertTrue(result != null && result.size() == 1);		
		assertTrue(result.iterator().next().getValue() == 1);
	}
}
