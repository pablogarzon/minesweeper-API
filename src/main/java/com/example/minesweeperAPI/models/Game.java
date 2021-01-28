package com.example.minesweeperAPI.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter 
@Setter
public class Game {
	
	@Transient
	public static final String SEQUENCE_NAME = "games_sequence";

	@Id
	private long id;	
	
	private int rows;
	
	private int columns;
	
	private int mines;
	
	private Cell[][] board;
	
	@Builder.Default
	private int uncoveredCells = 0;
	
	@Builder.Default
	private long time = 0L;
	
	@Builder.Default
	private GameState state = GameState.NOT_STARTED;
	
	public void startGame() {
		if (this.state.isNotStarted()) {
			this.state = GameState.ACTIVE;
		}
	}
	
	public void pauseGame() {
		if(this.state.isActive()) {
			this.state = GameState.PAUSED;
		}
	}
	
	public void resumeGame() {
		if(this.state.isPaused()) {
			this.state = GameState.ACTIVE;
		}
	}
	
	public void endGame(GameState state) {
		if(this.state.isActive() && (state.isFailed() || state.isVictory())) {
			this.state = state;
		}
	}
	
	public void incrementUncoveredCells(int uncovered) {
		this.uncoveredCells += uncovered;
	}
	
	public boolean isGameWon() {
		var cellCount = this.columns * this.rows;
		return this.uncoveredCells + this.mines == cellCount;
	}
	
	@Override
	public String toString() {
		var sb = new StringBuilder();
		sb.append("id=" + id + "\n");
		sb.append("time=" + this.time + "\n");
		sb.append("board=" + "\n");
		if (board != null) {
			for (int i = 0; i < this.rows; i++) {
				for (int j = 0; j < this.columns; j++) {
					if (this.board[i][j].getState().isCovered()) {
						sb.append("\tx\t");
						continue;
					}
					if (this.board[i][j].isHasMine()) {
						sb.append("\tb\t");
					} else {
						sb.append("\t" + this.board[i][j].getValue() + "\t");	
					}
				}
				sb.append("\n");
			}
		}	
		return sb.toString();
	}

}
