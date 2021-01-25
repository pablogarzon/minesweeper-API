package com.example.minesweeperAPI.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter 
@Setter
@ToString
public class CellCoordinates {	
	
	private final int x;
	
	private final int y;
}
