package com.example.minesweeperAPI.controllers;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import com.example.minesweeperAPI.MinesweeperApiApplication;
import com.example.minesweeperAPI.dto.CellStateDTO;
import com.example.minesweeperAPI.dto.CreateGameDTO;
import com.example.minesweeperAPI.dto.UncoverCellDTO;
import com.example.minesweeperAPI.dto.PauseGameDTO;
import com.example.minesweeperAPI.models.CellCoordinates;
import com.example.minesweeperAPI.utils.EndPointUrls;
import com.fasterxml.jackson.databind.JsonNode;

@SpringBootTest(classes = MinesweeperApiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "test")
public class GameControllerTest {
	
	@LocalServerPort
	private int port;
	
	
    private static RestTemplate restTemplate;
    
    @BeforeAll
    public static void setUp(@Autowired RestTemplateBuilder builder) {
    	restTemplate = builder
    		.errorHandler(new ResponseErrorHandler() {
				@Override
				public boolean hasError(ClientHttpResponse response) throws IOException {
					return false;
				}
				
				@Override
				public void handleError(ClientHttpResponse response) throws IOException {
					
				}
			})
	        .build();
    }
    
	
	private String createURLWithPort(String uri) {
		return "http://localhost:" + port + uri;
	}
    
    // requests
    private ResponseEntity<JsonNode> callCreateGame(CreateGameDTO gameDTO) {
		ResponseEntity<JsonNode> response = restTemplate.exchange(
				createURLWithPort(EndPointUrls.CREATE),
				HttpMethod.POST, new HttpEntity<>(gameDTO), JsonNode.class);
		return response;
	}
    
    private ResponseEntity<JsonNode> callUncoverCell(UncoverCellDTO moveDTO) {
		return restTemplate.exchange(
				createURLWithPort(EndPointUrls.UNCOVER_CELL),
				HttpMethod.POST, new HttpEntity<>(moveDTO), JsonNode.class);
	}
    
    private ResponseEntity<JsonNode> callPauseGame(long gameId, PauseGameDTO dto) {
    	var uri = EndPointUrls.PAUSE.replace("{gameId}", Long.toString(gameId));
    	
    	return restTemplate.exchange(
				createURLWithPort(uri),
				HttpMethod.PATCH, new HttpEntity<>(dto), JsonNode.class);
	}
    
    private ResponseEntity<JsonNode> callResumeGame(long gameId) {
    	var uri = EndPointUrls.RESUME.replace("{gameId}", Long.toString(gameId));
    	
    	return restTemplate.exchange(
				createURLWithPort(uri),
				HttpMethod.PATCH, new HttpEntity<>(null), JsonNode.class);
	}
    
    private ResponseEntity<JsonNode> callCellUpdate(long gameId, CellStateDTO dto) {
    	var uri = EndPointUrls.MARK_CELL.replace("{gameId}", Long.toString(gameId));
    	
    	return restTemplate.exchange(
				createURLWithPort(uri),
				HttpMethod.PATCH, new HttpEntity<>(dto), JsonNode.class);
	}
    
    // tests
	@Test
	public void givenValidNewGameRequest_WhenNewGame_ThenReturnOk() {
		// given
		var gameDTO = new CreateGameDTO(3, 3, 2);
		// when
		ResponseEntity<JsonNode> response = callCreateGame(gameDTO);
		// then
		assertTrue(response.getStatusCode().equals(HttpStatus.OK));
	}
	
	@Test
	public void givenInvalidNewGameRequest_WhenNewGame_ThenReturnBadRequest() {
		// given
		var gameDTO = new CreateGameDTO(3, 3, 50);
		// when
		ResponseEntity<JsonNode> response = callCreateGame(gameDTO);
		// then
		assertTrue(response.getStatusCode().equals(HttpStatus.BAD_REQUEST));
	}
	
	@Test
	public void givenValidGameIdOnRequest_WhenStartGame_ThenReturnOk() {
		// given
		var gameDTO = new CreateGameDTO(3, 3, 2);
		long gameId = callCreateGame(gameDTO).getBody().get("gameId").asLong();
		var moveDTO = new UncoverCellDTO(new CellCoordinates(1, 0), gameId, 0L);
		// when
		ResponseEntity<JsonNode> response = callUncoverCell(moveDTO);
		// then
		assertTrue(response.getStatusCode().equals(HttpStatus.OK));
	}
	
	@Test
	public void givenInvalidGameIdOnRequest_WhenStartGame_ThenReturnNotFound() {
		// given
		var moveDTO = new UncoverCellDTO(new CellCoordinates(1, 0), 99, 0L);
		// when
		ResponseEntity<JsonNode> response = callUncoverCell(moveDTO);
		// then
		assertTrue(response.getStatusCode().equals(HttpStatus.NOT_FOUND));
	}
	
//	@Test
//	public void givenGameIdOfStartedGameOnRequest_WhenStartGame_ThenReturnConflict() {
//		// given
//		var gameDTO = new CreateGameDTO(3, 3, 2);
//		var json = callCreateGame(gameDTO);
//		long gameId = json.getBody().get("gameId").asLong();
//		var moveDTO = new ActivatedCellDTO(new CellCoordinates(1, 0), gameId);
//		callStartGame(moveDTO);
//		// when
//		ResponseEntity<JsonNode> response = callStartGame(moveDTO);
//		// then
//		assertTrue(response.getStatusCode().equals(HttpStatus.CONFLICT));
//	}
	
	@Test
	public void givenInvalidCoordinatesOnRequest_WhenStartGame_ThenReturnBadRequest() {
		// given
		var gameDTO = new CreateGameDTO(3, 3, 2);
		long gameId = callCreateGame(gameDTO).getBody().get("gameId").asLong();
		var moveDTO = new UncoverCellDTO(new CellCoordinates(10, 0), gameId, 0L);
		// when
		ResponseEntity<JsonNode> response = callUncoverCell(moveDTO);
		// then
		assertTrue(response.getStatusCode().equals(HttpStatus.BAD_REQUEST));
	}
	
	@Test
	public void givenGameIdOfStartedGameOnRequest_WhenCallUncoverCellPerSecondTime_ThenReturnOk() {
		// given
		var gameDTO = new CreateGameDTO(3, 3, 8); //the first cell will by the only non-mined cell
		var json = callCreateGame(gameDTO);
		long gameId = json.getBody().get("gameId").asLong();
		var startDTO = new UncoverCellDTO(new CellCoordinates(1, 0), gameId, 0L);
		callUncoverCell(startDTO);
		// when
		var moveDTO = new UncoverCellDTO(new CellCoordinates(1, 1), gameId, 1L);
		ResponseEntity<JsonNode> response = callUncoverCell(moveDTO);
		// then
		assertTrue(response.getStatusCode().equals(HttpStatus.OK));
	}
	
//	@Test
//	public void givenGameIdOfUnstartedGameOnRequest_WhenUncoverCell_ThenReturnOk() {
//		// given
//		var gameDTO = new CreateGameDTO(3, 3, 2);
//		var json = callCreateGame(gameDTO);
//		long gameId = json.getBody().get("gameId").asLong();
//		// when
//		var moveDTO = new ActivatedCellDTO(new CellCoordinates(1, 1), gameId);
//		ResponseEntity<JsonNode> response = callUncoverCell(moveDTO);
//		// then
//		assertTrue(response.getStatusCode().equals(HttpStatus.BAD_REQUEST));
//	}
	
	@Test
	public void givenUncoverRequestForUncoveredCell_WhenUncoverCell_ThenReturnConflict() {
		// given
		var gameDTO = new CreateGameDTO(5, 5, 24);
		var json = callCreateGame(gameDTO);
		long gameId = json.getBody().get("gameId").asLong();
		var startDTO = new UncoverCellDTO(new CellCoordinates(1, 0), gameId, 0L);
		callUncoverCell(startDTO);
		// when
		var moveDTO = new UncoverCellDTO(new CellCoordinates(1, 0), gameId, 1L);
		ResponseEntity<JsonNode> response = callUncoverCell(moveDTO);
		// then
		assertTrue(response.getStatusCode().equals(HttpStatus.CONFLICT));
	}
	
	@Test
	public void givenValidGameIdOnRequest_WhenPauseGame_ThenReturnOk() {
		// given
		var gameDTO = new CreateGameDTO(3, 3, 2);
		long gameId = callCreateGame(gameDTO).getBody().get("gameId").asLong();
		var startDTO = new UncoverCellDTO(new CellCoordinates(1, 0), gameId, 0L);
		callUncoverCell(startDTO);
		PauseGameDTO dto = new PauseGameDTO(2L);
		// when
		var response = callPauseGame(gameId, dto);
		// then
		assertTrue(response.getStatusCode().equals(HttpStatus.OK));
	}
	
	@Test
	public void givenGameIdOfGameNotStartedOnRequest_WhenPauseGame_ThenReturnBadRequest() {
		// given
		var gameDTO = new CreateGameDTO(3, 3, 2);
		long gameId = callCreateGame(gameDTO).getBody().get("gameId").asLong();
		PauseGameDTO dto = new PauseGameDTO(2L);
		// when
		var response = callPauseGame(gameId, dto);
		// then
		assertTrue(response.getStatusCode().equals(HttpStatus.BAD_REQUEST));
	}
	
	@Test
	public void givenInvalidGameIdOnRequest_WhenPauseGame_ThenReturnNotFound() {
		// given
		PauseGameDTO dto = new PauseGameDTO(2L);
		// when
		var response = callPauseGame(99, dto);
		// then
		assertTrue(response.getStatusCode().equals(HttpStatus.NOT_FOUND));
	}
	
	@Test
	public void givenValidPausedGameIdOnRequest_WhenResumeGame_ThenReturnOk() {
		// given
		var gameDTO = new CreateGameDTO(3, 3, 2);
		long gameId = callCreateGame(gameDTO).getBody().get("gameId").asLong();
		var startDTO = new UncoverCellDTO(new CellCoordinates(1, 0), gameId, 0L);
		callUncoverCell(startDTO);
		PauseGameDTO dto = new PauseGameDTO(2L);
		callPauseGame(gameId, dto);
		// when
		var response = callResumeGame(gameId);
		// then
		assertTrue(response.getStatusCode().equals(HttpStatus.OK));
	}
	
	@Test
	public void givenActiveGameIdOnRequest_WhenResumeGame_ThenReturnBadRequest() {
		// given
		var gameDTO = new CreateGameDTO(3, 3, 2);
		long gameId = callCreateGame(gameDTO).getBody().get("gameId").asLong();
		var startDTO = new UncoverCellDTO(new CellCoordinates(1, 0), gameId, 0L);
		callUncoverCell(startDTO);
		// when
		var response = callResumeGame(gameId);
		// then
		assertTrue(response.getStatusCode().equals(HttpStatus.BAD_REQUEST));
	}
	
	@Test
	public void givenValidGameOnRequest_WhenCellUpdate_ThenReturnOk() {
		// given
		var gameDTO = new CreateGameDTO(3, 3, 8);
		long gameId = callCreateGame(gameDTO).getBody().get("gameId").asLong();
		var startDTO = new UncoverCellDTO(new CellCoordinates(1, 0), gameId, 0L);
		callUncoverCell(startDTO);
		var cellStateDto = new CellStateDTO(new CellCoordinates(1, 2), "MARKED_WITH_FLAG");
		// when
		var response = callCellUpdate(gameId, cellStateDto);
		// then
		assertTrue(response.getStatusCode().equals(HttpStatus.OK));
	}
	
	@Test
	public void givenGameIdOfGameNotStartedOnRequest_WhenCellUpdate_ThenReturnBadRequest() {
		// given
		var gameDTO = new CreateGameDTO(3, 3, 2);
		long gameId = callCreateGame(gameDTO).getBody().get("gameId").asLong();
		var cellStateDto = new CellStateDTO(new CellCoordinates(1, 2), "MARKED_WITH_FLAG");
		// when
		var response = callCellUpdate(gameId, cellStateDto);
		// then
		assertTrue(response.getStatusCode().equals(HttpStatus.BAD_REQUEST));
	}
}
