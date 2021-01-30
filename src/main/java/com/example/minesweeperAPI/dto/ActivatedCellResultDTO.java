package com.example.minesweeperAPI.dto;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ActivatedCellResultDTO {
	
	private String gameState;
	
	private Set<UncoveredCellDTO> uncoveredCells;
}
