package com.example.minesweeperAPI.services;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.example.minesweeperAPI.dto.CreateGameDTO;
import com.example.minesweeperAPI.dto.UncoverCellDTO;
import com.example.minesweeperAPI.exceptions.MineSweeperException;
import com.example.minesweeperAPI.models.Cell;
import com.example.minesweeperAPI.models.CellCoordinates;
import com.example.minesweeperAPI.models.Game;
import com.example.minesweeperAPI.models.GameState;
import com.example.minesweeperAPI.repository.GameRepository;

public class GameServiceTest {
	
	private GameService service;
	
	private GameRepository repository;
	
	private SequenceGeneratorService sequenceGenerator;
	
	@BeforeEach
	public void init() {
		repository = Mockito.mock(GameRepository.class);
		sequenceGenerator = Mockito.mock(SequenceGeneratorService.class);
		
		service = new GameServiceImpl(repository, sequenceGenerator);
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
				Cell.builder() // cell with mine
					.coordinates(new CellCoordinates(2, 0))
					.hasMine(true)
					.value(0)
					.build()
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
				Cell.builder() // cell with mine
					.coordinates(new CellCoordinates(2, 1))
					.hasMine(true)
					.value(0)
					.build()
			},
			{
				Cell.builder() // cell with mine
					.coordinates(new CellCoordinates(0, 2))
					.hasMine(true)
					.value(0)
					.build(),
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
				.state(GameState.ACTIVE)
				.board(board)
				.build();
		
		return game;
	}
	
	@Test
	public void createGameTest() throws MineSweeperException {		
		// given
		int value = 3;
		var game = Game.builder()
				.columns(value)
				.rows(value)
				.mines(value)
				.build();
		
		// when
		when(repository.save(Mockito.any())).thenReturn(game);
		when(sequenceGenerator.generateSequence(Mockito.any())).thenReturn(1L);
		
		var result = service.create(new CreateGameDTO(value, value, value, 1));
		
		// then
		assertTrue(result != null);
		assertTrue(result.getMines() == 3);
	}
	
	@Test
	public void startGameTest() throws MineSweeperException {
		// given
		long gameId = 1;
		int x = 2;
		int y = 0;
		var emptyGame = Game.builder()
				.id(1) //generated id
				.rows(15)
				.columns(10)
				.mines(2)
				.build();
		
		//when
		when(repository.findById(Mockito.any())).thenReturn(Optional.of(emptyGame));
		
		var result = service.uncoverCell(new UncoverCellDTO(new CellCoordinates(x, y), gameId, 0L));
		
		// then
		assertTrue(result != null && result.getUncoveredCells().size() > 0);
		assertTrue(!result.getGameState().equals(GameState.FAILED.name()));
	}
	
	@Test
	public void uncoverCellTest() throws MineSweeperException {
		// given
		long gameId = 1; 
				
		int x = 0;
		int y = 0;
		
		// when
		when(repository.findById(Mockito.any())).thenReturn(Optional.of(createTestGame()));
		
		var result = service.uncoverCell(new UncoverCellDTO(new CellCoordinates(x, y), gameId, 0L));

		// then
		assertTrue(result != null && result.getUncoveredCells().size() == 4);
		
		var cellWithTwoMinesAround = result.getUncoveredCells().stream()
				.filter(c -> c.getCoordinates().getY() == 0 && c.getCoordinates().getX() == 1)
				.findFirst()
				.get();
		
		assertTrue(cellWithTwoMinesAround.getValue() == 2);
	}
	
	@Test
	public void uncoverCellWithOneMineAdjacentTest() throws MineSweeperException {
		// given
		long gameId = 1;
		int x = 0;
		int y = 1;
		
		// when
		when(repository.findById(Mockito.any())).thenReturn(Optional.of(createTestGame()));
		
		var result = service.uncoverCell(new UncoverCellDTO(new CellCoordinates(x, y), gameId, 0L));

		// then
		assertTrue(result != null && result.getUncoveredCells().size() == 1);		
		assertTrue(result.getUncoveredCells().iterator().next().getValue() == 1);
	}
}
