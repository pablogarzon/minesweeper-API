package com.example.minesweeperAPI.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum GameState {
	ACTIVE(1), PAUSED(2), VICTORY(3), FAILED(4);
	
	private final int state;
	
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
