package com.example.minesweeperAPI.exceptions;

public class GameNotFoundException extends MineSweeperException {

	private static final long serialVersionUID = 1L;

	public GameNotFoundException() {
		super("Game not found");
	}

}
