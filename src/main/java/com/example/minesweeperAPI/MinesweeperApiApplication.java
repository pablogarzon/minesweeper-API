package com.example.minesweeperAPI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "minesweeper-Api"))
public class MinesweeperApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MinesweeperApiApplication.class, args);
	}

}
