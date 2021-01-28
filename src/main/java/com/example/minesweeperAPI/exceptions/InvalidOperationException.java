package com.example.minesweeperAPI.exceptions;

public class InvalidOperationException extends MineSweeperException {

	private static final long serialVersionUID = -7057054469332277453L; 
	
	private InvalidOperationException(String message) {
		super(message);
	}
	
	public static InvalidOperationException GameAlreadyStarted() {
		return new InvalidOperationException("Game has already started");
	}

	public static InvalidOperationException CellIsAlreadyUncovered() {
		return new InvalidOperationException("Cell is already uncovered");
	}

	public static InvalidOperationException GameIsAlreadyActive() {
		return new InvalidOperationException("Game is already active");
	}

}
