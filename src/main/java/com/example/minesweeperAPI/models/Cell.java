package com.example.minesweeperAPI.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Builder(toBuilder = true)
@RequiredArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class Cell {
	
	private final int row;
	
	private final int column;
	
	private int value;
	
	private final boolean hasMine;
	
	@Builder.Default
	private CellState state = CellState.COVERED;
	
	public void addMinesAround() {
		this.value++;
	}
}
