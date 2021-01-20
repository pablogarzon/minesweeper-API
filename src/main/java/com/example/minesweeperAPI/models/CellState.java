package com.example.minesweeperAPI.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CellState {
	COVERED(1), MARKED_WITH_FLAG(2), MARKED_WITH_QUESTION(3), UNCOVERED(4);
	
	private final int state;
	
	public boolean isCovered() {
		return this.getState() == CellState.COVERED.getState();
	}
	
	public boolean isMarkedWithFlag() {
		return this.getState() == CellState.MARKED_WITH_FLAG.getState();
	}
	
	public boolean isMarkedWithQuestion() {
		return this.getState() == CellState.MARKED_WITH_QUESTION.getState();
	}
	
	public boolean isUnCovered() {
		return this.getState() == CellState.UNCOVERED.getState();
	}
}
