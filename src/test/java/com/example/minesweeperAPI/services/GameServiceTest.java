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
		int value = 10;
		
		// when
		var result = service.start(value, value, value);
		System.out.println(result.toString());
		
		// then
		assertTrue(result.getBoard() != null);
		assertTrue(result.getBoard()[0].length == value);
		assertTrue(result.getMines() == value);
	}
}
