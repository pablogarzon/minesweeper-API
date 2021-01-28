package com.example.minesweeperAPI.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter 
@Setter
public class CellStateDTO {
	
	private CoordinatesDTO coordinates;
	
	private int state;
}
