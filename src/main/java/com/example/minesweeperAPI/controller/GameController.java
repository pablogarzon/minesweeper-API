package com.example.minesweeperAPI.controller;

import java.util.Collections;
import java.util.Map;

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
import com.example.minesweeperAPI.models.CellState;
import com.example.minesweeperAPI.services.GameService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class GameController {
	
	private final GameService service;
	
	@PostMapping(value = "/game/create")
	public Map<String, Long> create(@RequestBody CreateGameDTO dto) {
		var game = service.create(dto.getRows(), dto.getColumns(), dto.getMines());
		return Collections.singletonMap("gameId", game.getId());
	}
	
	@PostMapping(value = "/game/start")
	public MoveResultDTO start(@RequestBody MoveRequestDTO dto) {
		int x = dto.getCoordinates().getX();
		int y = dto.getCoordinates().getY();
		
		final var result = service.start(dto.getGameId(), x, y);;
		
		return result;
	}
	
	@PostMapping(value = "/game/move")
	public MoveResultDTO move(@RequestBody MoveRequestDTO dto) {
		int x = dto.getCoordinates().getX();
		int y = dto.getCoordinates().getY();
		
		final var result = service.move(dto.getGameId(), x, y);
		
		return result;
	}
	
	@PatchMapping(value = "/game/{gameId}/pause")
	public Map<String, String> pause(@PathVariable int gameId, @RequestBody PauseGameDTO dto) {
		service.pause(gameId, dto.getTime());
		return Collections.singletonMap("status", "PAUSED");
	}
	
	@PatchMapping(value = "/game/{gameId}/resume")
	public Map<String, String> resume(@PathVariable int gameId) {
		service.resume(gameId);
		return Collections.singletonMap("status", "ACTIVE");
	}
	
	@PatchMapping(value = "/game/{gameId}/updateCell")
	public Map<String, String> updateCell(@PathVariable int gameId, @RequestBody CellStateDTO dto) {
		var state = CellState.getByValue(dto.getState());
		service.updateCellState(gameId, dto.getCoordinates().getX(), dto.getCoordinates().getY(), state);
		return Collections.singletonMap("status", state.name());
	}
}
