package com.example.minesweeperAPI.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder(toBuilder = true)
@AllArgsConstructor
@RequiredArgsConstructor
@Getter @Setter
@ToString
public class Game {

	private int id;	
	
	private final int rows;
	
	private final int columns;
	
	private final int mines;
	
	private Cell[][] board;
	
	@Builder.Default
	private long time = 0L;
	
	@Builder.Default
	private GameState state = GameState.ACTIVE;
}
