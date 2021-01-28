package com.example.minesweeperAPI.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "database_sequences")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DatabaseSequence {
	
	@Id
	private String id;

	private long seq;
}
