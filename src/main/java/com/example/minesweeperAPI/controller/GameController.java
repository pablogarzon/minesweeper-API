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
import com.example.minesweeperAPI.dto.PauseGameDTO;
import com.example.minesweeperAPI.dto.UncoverCellDTO;
import com.example.minesweeperAPI.dto.UncoverCellResultDTO;
import com.example.minesweeperAPI.exceptions.BoardDimensionException;
import com.example.minesweeperAPI.exceptions.GameNotFoundException;
import com.example.minesweeperAPI.exceptions.InvalidCoordinatesException;
import com.example.minesweeperAPI.exceptions.InvalidOperationException;
import com.example.minesweeperAPI.exceptions.OperationNotAllowedException;
import com.example.minesweeperAPI.services.GameService;
import com.example.minesweeperAPI.utils.EndPointUrls;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class GameController {
	
	private final GameService service;
	
	@Operation(summary = "Create a new minesweeper game")
	@PostMapping(value = EndPointUrls.CREATE)
	public Map<String, Long> create(@RequestBody CreateGameDTO dto) throws BoardDimensionException {
		var game = service.create(dto);
		return Collections.singletonMap("gameId", game.getId());
	}
	
	@Operation(summary = "Uncover the selected cell and all adjacent non-mined cells")
	@PostMapping(value = EndPointUrls.UNCOVER_CELL)
	public UncoverCellResultDTO uncoverCell(@RequestBody UncoverCellDTO dto)  throws GameNotFoundException, InvalidCoordinatesException, OperationNotAllowedException, InvalidOperationException {
		final var result = service.uncoverCell(dto);
		return result;
	}
	
	@Operation(summary = "Pause the current game")
	@PatchMapping(value = EndPointUrls.PAUSE)
	public Map<String, String> pause(@PathVariable long gameId, @RequestBody PauseGameDTO dto) throws GameNotFoundException, OperationNotAllowedException {
		service.pause(gameId, dto);
		return Collections.singletonMap("status", "PAUSED");
	}
	
	@Operation(summary = "Resume the current game")
	@PatchMapping(value = EndPointUrls.RESUME)
	public Map<String, String> resume(@PathVariable long gameId) throws GameNotFoundException, InvalidOperationException, OperationNotAllowedException {
		service.resume(gameId);
		return Collections.singletonMap("status", "ACTIVE");
	}
	
	@Operation(summary = "Change state of one cell")
	@PatchMapping(value = EndPointUrls.MARK_CELL)
	public Map<String, String> updateCell(@PathVariable long gameId, @RequestBody CellStateDTO dto) throws OperationNotAllowedException, GameNotFoundException, InvalidCoordinatesException {
		service.updateCellState(gameId, dto);
		return Collections.singletonMap("status", dto.getState());
	}
}
