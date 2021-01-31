package com.example.minesweeperAPI.utils;

public class EndPointUrls {
	
	public final static String CREATE = "/game/create";
	
	public final static String START = "/game/start";
	
	public final static String UNCOVER_CELL = "/game/uncoverCell";
	
	public final static String PAUSE = "/game/{gameId}/pause";
	
	public final static String RESUME = "/game/{gameId}/resume";
	
	public final static String MARK_CELL = "/game/{gameId}/updateCell";
}
