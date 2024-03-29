package br.com.ecosensor.model;

public class ResultEvent {
	
	private final boolean itWon;
	
	public ResultEvent(boolean itWon) {
		this.itWon = itWon;
	}
	
	public boolean isItWon() {
		return itWon;
	}
}
