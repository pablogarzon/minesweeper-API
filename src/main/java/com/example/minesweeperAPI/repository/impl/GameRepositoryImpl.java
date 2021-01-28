package com.example.minesweeperAPI.repository.impl;

import java.util.List;

import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.example.minesweeperAPI.exceptions.GameNotFoundException;
import com.example.minesweeperAPI.exceptions.InvalidCoordinatesException;
import com.example.minesweeperAPI.models.Cell;
import com.example.minesweeperAPI.models.CellCoordinates;
import com.example.minesweeperAPI.models.CellState;
import com.example.minesweeperAPI.models.Game;
import com.example.minesweeperAPI.models.GameState;
import com.example.minesweeperAPI.repository.GameRepositoryCustom;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GameRepositoryImpl implements GameRepositoryCustom {

	private final MongoTemplate mongoTemplate;

	@Override
	public void updateGameToPaused(int gameId, long time) {		
		var update = new Update();
		update.set("time", time);
		update.set("state", GameState.PAUSED.name());
		
		mongoTemplate.updateFirst(findGame(gameId), update, Game.class);
	}

	@Override
	public void updateGameToActive(int gameId) {		
		var update = Update.update("state", GameState.ACTIVE.name());		
		mongoTemplate.updateFirst(findGame(gameId), update, Game.class);
	}

	@Override
	public CellState findCellPreviousState(int gameId, CellCoordinates coordinates) throws GameNotFoundException, InvalidCoordinatesException {
		var exists = mongoTemplate.exists(findGame(gameId), Game.class);
		if (!exists) {
			throw new GameNotFoundException();
		}
		// needs improvement
		Aggregation aggregation = Aggregation.newAggregation(
				Aggregation.match(Criteria.where("_id").is(gameId)),
				Aggregation.unwind("$board"),
				Aggregation.unwind("$board"),
				Aggregation.match(Criteria.where("board.coordinates.x").is(coordinates.getX())
						.andOperator(Criteria.where("board.coordinates.y").is(coordinates.getY()))),
				Aggregation.project("$board").andExclude("_id") //$board.state does not work for some reason
			);
		@SuppressWarnings("unchecked")
		var result = (List<Document>) mongoTemplate.aggregate(aggregation, Game.class, Cell.class)
				.getRawResults().get("results");
		if (result == null || result.size() == 0) {
			throw new InvalidCoordinatesException();
		}
		Document document = (Document) result.get(0).get("board");
		return CellState.valueOf(document.get("state").toString());
	}

	@Override
	public void updateCellState(int gameId, CellCoordinates coordinates, CellState cellState) {		
		var affectedCell = String.format("board.%d.%d.state", coordinates.getY(), coordinates.getX());
		var update = new Update();
		update.set(affectedCell, cellState.name());
		
		mongoTemplate.updateFirst(findGame(gameId), update, Game.class);
	}
	
	private Query findGame(int gameId) {
		return Query.query(Criteria.where("_id").is(gameId));
	}
}
