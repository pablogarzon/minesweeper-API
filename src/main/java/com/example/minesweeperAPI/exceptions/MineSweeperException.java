package com.example.minesweeperAPI.exceptions;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class MineSweeperException extends Exception {

	private static final long serialVersionUID = -8222738346419071776L;

	private final HttpStatus status;
	private final String message;
	
	public static MineSweeperException OperationNotAllowedException() {
		return new MineSweeperException(HttpStatus.BAD_REQUEST, "Operation not allowed");
	}
	
	public static MineSweeperException GameNotFoundException() {
		return new MineSweeperException(HttpStatus.NOT_FOUND, "Game not found");
	}
	
	public static MineSweeperException InvalidOperationException() {
		return new MineSweeperException(HttpStatus.CONFLICT, "Invalid operation");
	}
	
	public static MineSweeperException BoardDimensionException() {
		return new MineSweeperException(HttpStatus.BAD_REQUEST, "Invalid board dimension");
	}
	
	public static MineSweeperException InvalidCoordinatesException() {
		return new MineSweeperException(HttpStatus.BAD_REQUEST, "Invalid coordinates");
	}
}
