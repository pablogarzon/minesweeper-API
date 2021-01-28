package com.example.minesweeperAPI.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.minesweeperAPI.dto.CellStateDTO;
import com.example.minesweeperAPI.dto.CreateGameDTO;
import com.example.minesweeperAPI.dto.MoveRequestDTO;
import com.example.minesweeperAPI.dto.MoveResultDTO;
import com.example.minesweeperAPI.dto.PauseGameDTO;
import com.example.minesweeperAPI.exceptions.MineSweeperException;
import com.example.minesweeperAPI.models.CellState;
import com.example.minesweeperAPI.services.GameService;
import com.example.minesweeperAPI.utils.EndPointUrls;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class GameController {
	
	private final GameService service;
	
	@PostMapping(value = EndPointUrls.CREATE)
	public Map<String, Long> create(@RequestBody CreateGameDTO dto) throws MineSweeperException {
		var game = service.create(dto.getRows(), dto.getColumns(), dto.getMines());
		return Collections.singletonMap("gameId", game.getId());
	}
	
	@PostMapping(value = EndPointUrls.START)
	public MoveResultDTO start(@RequestBody MoveRequestDTO dto) throws MineSweeperException {
		int x = dto.getCoordinates().getX();
		int y = dto.getCoordinates().getY();
		
		final var result = service.start(dto.getGameId(), x, y);;
		
		return result;
	}
	
	@PostMapping(value = EndPointUrls.UNCOVER_CELL)
	public MoveResultDTO uncoverCell(@RequestBody MoveRequestDTO dto) throws MineSweeperException {
		int x = dto.getCoordinates().getX();
		int y = dto.getCoordinates().getY();
		
		final var result = service.move(dto.getGameId(), x, y);
		
		return result;
	}
	
	@PatchMapping(value = EndPointUrls.PAUSE)
	public Map<String, String> pause(@PathVariable int gameId, @RequestBody PauseGameDTO dto) throws MineSweeperException {
		service.pause(gameId, dto.getTime());
		return Collections.singletonMap("status", "PAUSED");
	}
	
	@PatchMapping(value = EndPointUrls.RESUME)
	public Map<String, String> resume(@PathVariable int gameId) throws MineSweeperException {
		service.resume(gameId);
		return Collections.singletonMap("status", "ACTIVE");
	}
	
	@PatchMapping(value = EndPointUrls.MARK_CELL)
	public Map<String, String> updateCell(@PathVariable int gameId, @RequestBody CellStateDTO dto) throws MineSweeperException {
		var state = CellState.getByValue(dto.getState());
		service.updateCellState(gameId, dto.getCoordinates().getX(), dto.getCoordinates().getY(), state);
		return Collections.singletonMap("status", state.name());
	}
	
	@ExceptionHandler({MineSweeperException.class})
	public ResponseEntity<?> handleException(MineSweeperException ex) {
		Map<String, String> res = new HashMap<>();
		res.put("message", ex.getMessage());
		return new ResponseEntity<Map<String, String>>(res, ex.getStatus());
	}
}
