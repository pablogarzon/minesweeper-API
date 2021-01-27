package com.example.minesweeperAPI.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum GameState {
	NOT_STARTED(1), ACTIVE(2), PAUSED(3), VICTORY(4), FAILED(5);
	
	private final int state;
	
	public boolean isNotStarted() {
		return this.state == GameState.NOT_STARTED.state;
	}
	
	public boolean isActive() {
		return this.state == GameState.ACTIVE.state;
	}
	
	public boolean isPaused() {
		return this.state == GameState.PAUSED.state;
	}
	
	public boolean isVictory() {
		return this.state == GameState.VICTORY.state;
	}
	
	public boolean isFailed() {
		return this.state == GameState.FAILED.state;
	}
}
