package com.example.minesweeperAPI.exceptions;

public class BoardDimensionException extends MineSweeperException {

	private static final long serialVersionUID = -2288130837918287506L;
	
	public BoardDimensionException() {
		super("Invalid board dimensions");
	}
}
