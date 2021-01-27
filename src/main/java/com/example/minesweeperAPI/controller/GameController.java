package com.example.minesweeperAPI.controller;

import java.util.Collections;
import java.util.Map;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.minesweeperAPI.dto.CreateGameDTO;
import com.example.minesweeperAPI.dto.MoveRequestDTO;
import com.example.minesweeperAPI.dto.MoveResultDTO;
import com.example.minesweeperAPI.services.GameService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class GameController {
	
	private final GameService service;
	
	@PostMapping(value = "/game/create")
	public Map<String, String> create(@RequestBody CreateGameDTO dto) {
		service.create(dto.getRows(), dto.getColumns(), dto.getMines());
		return Collections.singletonMap("result", "ok");
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
	
	@PatchMapping(value = "/game/pause")
	public void pause() {
		
	}
	
	@PatchMapping(value = "/game/resume")
	public void resume() {
		
	}
}
