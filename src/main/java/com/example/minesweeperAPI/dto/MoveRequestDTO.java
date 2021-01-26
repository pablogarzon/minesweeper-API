package com.example.minesweeperAPI.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
public class MoveRequestDTO {
	
	private CoordinatesDTO coordinates;
	
	private int gameId;
}
