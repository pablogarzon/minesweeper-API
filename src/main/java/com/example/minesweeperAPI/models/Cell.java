package com.example.minesweeperAPI.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class Cell {
	
	private CellCoordinates coordinates;
	
	private int value;
	
	private boolean hasMine;
	
	@Builder.Default
	private CellState state = CellState.COVERED;
	
	public void addMinesAround() {
		this.value++;
	}
}
