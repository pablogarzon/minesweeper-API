package com.example.minesweeperAPI.services;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.example.minesweeperAPI.services.impl.GameServiceImpl;

public class GameServiceTest {
	
	private GameService service;
	
	@Test
	public void createGameTest() {
		
		// given
		service = new GameServiceImpl();
		int value = 3;
		
		// when
		var result = service.create(value, value, value, 0, 0);
		System.out.println(result.toString());
		
		// then
		assertTrue(result.getBoard() != null);
		assertTrue(result.getBoard()[0].length == value);
		assertTrue(result.getMines() == 3);
		assertTrue(!result.getBoard()[0][0].isHasMine());
	}
	
	@Test
	public void startTest() {
		
		// given
		service = new GameServiceImpl();
		int value = 3;
		
		// when
		var result = service.start(value, value, value, 0, 0);
		System.out.println(result.toString());
		
		// then
		assertTrue(result != null);
		assertTrue(result.size() > 0);
		assertTrue(!result.iterator().next().isHasMine());
	}
	
	@Test
	public void uncoverCellTest() {
		// given
		service = new GameServiceImpl();
		
		// when
		var result = service.uncoverCell(1, 0, 0);

		// then
		assertTrue(result != null && result.size() == 4);
		
		var cellWithTwoMinesAround = result.stream()
				.filter(c -> c.getRow() == 1 && c.getColumn() == 1)
				.findFirst()
				.get();
		
		assertTrue(cellWithTwoMinesAround.getValue() == 2);
	}
}
