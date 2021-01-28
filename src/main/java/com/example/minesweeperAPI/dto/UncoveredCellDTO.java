package com.example.minesweeperAPI.dto;

import com.example.minesweeperAPI.models.CellCoordinates;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UncoveredCellDTO {

	private CellCoordinates coordinates;
	
	private int value;
}
