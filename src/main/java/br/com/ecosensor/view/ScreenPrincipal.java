package br.com.ecosensor.view;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import br.com.ecosensor.model.Gameboard;

@SuppressWarnings("serial")
public class ScreenPrincipal extends JFrame {
	
	public ScreenPrincipal() {
		Gameboard gameboard = new Gameboard(16, 30, 50);
		add(new PainelGamer(gameboard));
		
		setTitle("Minesweeper Game");
		setSize(690, 438);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	public static void main(String[] args) {
		new ScreenPrincipal();
	}
	
}
