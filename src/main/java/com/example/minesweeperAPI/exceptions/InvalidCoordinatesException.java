package com.example.minesweeperAPI.exceptions;

public class InvalidCoordinatesException extends MineSweeperException {

	private static final long serialVersionUID = 1L;
	
	public InvalidCoordinatesException() {
		super("Invalid coordinates");
	}

}
