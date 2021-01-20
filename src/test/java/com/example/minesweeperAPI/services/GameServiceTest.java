package com.example.minesweeperAPI.services;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.example.minesweeperAPI.services.impl.GameServiceImpl;

public class GameServiceTest {
	
	private GameService service;
	
	@Test
	public void startTest() {
		
		// given
		service = new GameServiceImpl();
		int value = 3;
		
		// when
		var result = service.start(value, value, value, 0, 0);
		System.out.println(result.toString());
		
		// then
		assertTrue(result.getBoard() != null);
		assertTrue(result.getBoard()[0].length == value);
		assertTrue(result.getMines() == 3);
		assertTrue(!result.getBoard()[0][0].isHasMine());
	}
}
