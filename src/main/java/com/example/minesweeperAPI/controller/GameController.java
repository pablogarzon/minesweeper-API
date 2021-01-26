package com.example.minesweeperAPI.controller;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.minesweeperAPI.dto.CreateGameDTO;
import com.example.minesweeperAPI.dto.MoveRequestDTO;
import com.example.minesweeperAPI.dto.MoveResponseDTO;
import com.example.minesweeperAPI.dto.MoveResultDTO;
import com.example.minesweeperAPI.models.Game;
import com.example.minesweeperAPI.models.GameState;
import com.example.minesweeperAPI.services.GameService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class GameController {
	
	private final GameService service;
	
	private final ModelMapper mapper;
	
	@PostMapping(value = "/game/create")
	public Map<String, String> create(@RequestBody CreateGameDTO dto) {
		service.create(dto.getRows(), dto.getColumns(), dto.getMines());
		return Collections.singletonMap("result", "ok");
	}
	
	@PostMapping(value = "/game/start")
	public MoveResultDTO start(@RequestBody MoveRequestDTO dto) {
		int x = dto.getCoordinates().getX();
		int y = dto.getCoordinates().getY();
		
		var cells = new HashSet<>(); //service.start(dto.getGameId(), x, y);
		
		Set<MoveResponseDTO> uncoverdcells = new HashSet<>();		
		mapper.map(cells, uncoverdcells);
		
		return new MoveResultDTO(GameState.ACTIVE.getState(), uncoverdcells);
	}
	
	@PostMapping(value = "/game/move")
	public MoveResultDTO move(@RequestBody MoveRequestDTO dto) {
		int x = dto.getCoordinates().getX();
		int y = dto.getCoordinates().getY();
		
		var cells = new HashSet<>(); //service.move(dto.getGameId(), x, y);
		
		Set<MoveResponseDTO> uncoverdcells = new HashSet<>();		
		mapper.map(cells, uncoverdcells);
		
		return new MoveResultDTO(GameState.ACTIVE.getState(), uncoverdcells);
	}
	
	@PatchMapping(value = "/game/pause")
	public void pause() {
		
	}
	
	@PatchMapping(value = "/game/resume")
	public void resume() {
		
	}
}
