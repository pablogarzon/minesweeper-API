package com.example.minesweeperAPI.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {

	@Transient
	public static final String SEQUENCE_NAME = "user_sequence";
	
	@Id
	private Long id;
	
	private String user;
}
