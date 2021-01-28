package com.example.minesweeperAPI.repository.impl;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

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
		var query = Query.query(Criteria.where("_id").is(gameId));
		var update = new Update();
		update.set("time", time);
		update.set("state", GameState.PAUSED.name());
		mongoTemplate.updateFirst(query, update, Game.class);
	}

	@Override
	public void updateGameToActive(int gameId) {
		var query = Query.query(Criteria.where("_id").is(gameId));
		var update = Update.update("state", GameState.ACTIVE.name());
		mongoTemplate.updateFirst(query, update, Game.class);
	}

	@Override
	public void updateCellState(int gameId, CellCoordinates coordinates, CellState cellState) {
		var affectedCell = String.format("board.%d.%d.state", coordinates.getY(), coordinates.getX());
		var query = Query.query(Criteria.where("_id").is(gameId));
		var update = new Update();
		update.set(affectedCell, cellState.name());
		mongoTemplate.updateFirst(query, update, Game.class);
	}
}
