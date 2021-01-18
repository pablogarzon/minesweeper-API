package com.example.minesweeperAPI.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CellState {
	COVERED(1), FLAGGED(2), MARKED(3), UNCOVERED(4);
	
	private final int status;
}
