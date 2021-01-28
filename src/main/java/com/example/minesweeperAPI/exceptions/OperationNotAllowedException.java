package com.example.minesweeperAPI.exceptions;

public class OperationNotAllowedException extends MineSweeperException {


	private static final long serialVersionUID = -3227396664267385545L;
	
	private OperationNotAllowedException(String message) {
		super(message);
	}
	
	public static OperationNotAllowedException GameIsNotStarted() {
		return new OperationNotAllowedException("Game is not started");
	}

	public static OperationNotAllowedException GameIsNotActive() {
		return new OperationNotAllowedException("Game is not active");
	}
	
	public static OperationNotAllowedException CellStateChangeNotAllowed() {
		return new OperationNotAllowedException("Cell state change not allowed");
	}
}
