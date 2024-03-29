package br.com.ecosensor.start;

import org.junit.jupiter.api.Test;

import br.com.ecosensor.model.Gameboard;

class FieldboardTest {
	
	@Test
	void Start() {
		
		Gameboard gameboard = new Gameboard(3, 3, 9);
		
		gameboard.registerObserver(e -> {
			if (e.isItWon()) {
				System.out.println("Game winner!");
			} else {
				System.out.println("Game over!!!");
			}
		});
		
		gameboard.changeTag(0, 0);
		gameboard.changeTag(0, 1);
		gameboard.changeTag(0, 2);
		gameboard.changeTag(1, 0);
		gameboard.changeTag(1, 1);
		gameboard.changeTag(1, 2);
		gameboard.changeTag(2, 0);
		gameboard.changeTag(2, 1);
		gameboard.open(2, 2);
//		gameboard.changeTag(2, 2);
		
	}
}
