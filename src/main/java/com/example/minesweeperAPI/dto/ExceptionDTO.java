package com.example.minesweeperAPI.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ExceptionDTO {

	private String exceptionType;
	
	private String message;
}
