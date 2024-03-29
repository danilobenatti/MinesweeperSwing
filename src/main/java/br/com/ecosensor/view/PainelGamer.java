package br.com.ecosensor.view;

import java.awt.GridLayout;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import br.com.ecosensor.model.Gameboard;

@SuppressWarnings("serial")
public class PainelGamer extends JPanel {
	
	public PainelGamer(Gameboard gameboard) {
		
		setLayout(new GridLayout(gameboard.getLines(), gameboard.getColumns()));
		
		gameboard.forEachField(f -> add(new FieldButton(f)));
		
		gameboard.registerObserver(e -> {
			SwingUtilities.invokeLater(() -> {
				if (e.isItWon()) {
					JOptionPane.showMessageDialog(this, "You Won :)");
				} else {
					JOptionPane.showMessageDialog(this, "It lost :(");
				}
				gameboard.restart();
			});
		});
	}
	
}
