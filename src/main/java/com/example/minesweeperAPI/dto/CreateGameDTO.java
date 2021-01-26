package com.example.minesweeperAPI.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CreateGameDTO {

	private int rows;
	
	private int columns;
	
	private int mines;
}
