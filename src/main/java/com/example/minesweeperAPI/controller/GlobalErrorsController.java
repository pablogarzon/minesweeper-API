package com.example.minesweeperAPI.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.minesweeperAPI.dto.ExceptionDTO;
import com.example.minesweeperAPI.exceptions.BoardDimensionException;
import com.example.minesweeperAPI.exceptions.GameNotFoundException;
import com.example.minesweeperAPI.exceptions.InvalidCoordinatesException;
import com.example.minesweeperAPI.exceptions.InvalidOperationException;
import com.example.minesweeperAPI.exceptions.MineSweeperException;
import com.example.minesweeperAPI.exceptions.OperationNotAllowedException;

@RestControllerAdvice
public class GlobalErrorsController {

	@ExceptionHandler({GameNotFoundException.class})
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<ExceptionDTO> handleNotFoundException(MineSweeperException ex) {
		final var res = new ExceptionDTO(ex.getClass().getSimpleName(), ex.getMessage());
		return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler({InvalidOperationException.class})
	@ResponseStatus(HttpStatus.CONFLICT)
	public ResponseEntity<ExceptionDTO> handleConflictsException(MineSweeperException ex) {
		final var res = new ExceptionDTO(ex.getClass().getSimpleName(), ex.getMessage());
		return new ResponseEntity<>(res, HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler({BoardDimensionException.class, OperationNotAllowedException.class, InvalidCoordinatesException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ExceptionDTO> handleBadRequestException(MineSweeperException ex) {
		final var res = new ExceptionDTO(ex.getClass().getSimpleName(), ex.getMessage());
		return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
	}
}