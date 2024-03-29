package br.com.ecosensor.view;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.SwingUtilities;

import br.com.ecosensor.model.FieldObserver;
import br.com.ecosensor.model.Fieldboard;
import br.com.ecosensor.model.enums.FieldEvent;

@SuppressWarnings("serial")
public class FieldButton extends JButton
		implements FieldObserver, MouseListener {
	
	private static final Color BG_STANDARD = new Color(184, 184, 184);
	private static final Color BG_MARKED = new Color(8, 179, 247);
	private static final Color BG_EXPLODE = new Color(189, 66, 69);
	private static final Color TEXT_GREEN = new Color(0, 100, 0);
	
	private Fieldboard fieldboard;
	
	public FieldButton(Fieldboard fieldboard) {
		this.fieldboard = fieldboard;
		setBackground(BG_STANDARD);
		setOpaque(true);
		setBorder(BorderFactory.createBevelBorder(0));
		addMouseListener(this);
		fieldboard.registerObserver(this);
	}
	
	@Override
	public void occurredEvent(Fieldboard fieldboard, FieldEvent event) {
		switch (event) {
			case OPEN:
				applyOpenStyle();
				break;
			case TOMARK:
				applyToMarkStyle();
				break;
			case EXPLODE:
				applyExplodeStyle();
				break;
			default:
				applyDefaultStyle();
				break;
		}
		SwingUtilities.invokeLater(() -> {
			repaint();
			validate();
		});
	}
	
	private void applyDefaultStyle() {
		setBackground(BG_STANDARD);
		setBorder(BorderFactory.createBevelBorder(0));
		setText("");
	}
	
	private void applyExplodeStyle() {
		setBackground(BG_EXPLODE);
		setForeground(Color.WHITE);
		setText("X");
	}
	
	private void applyToMarkStyle() {
		setBackground(BG_MARKED);
		setForeground(Color.BLACK);
		setText("M");
	}
	
	private void applyOpenStyle() {
		
		setBorder(BorderFactory.createLineBorder(Color.GRAY));
		if (this.fieldboard.isMineded()) {
			setBackground(BG_EXPLODE);
			return;
		}
		setBackground(BG_STANDARD);
		switch (this.fieldboard.minesIntoNeighbor()) {
			case 1:
				setForeground(TEXT_GREEN);
				break;
			case 2:
				setForeground(Color.BLUE);
				break;
			case 3:
				setForeground(Color.YELLOW);
				break;
			case 4:
				setForeground(Color.ORANGE);
				break;
			case 5, 6:
				setForeground(Color.RED);
				break;
			default:
				setForeground(Color.PINK);
				break;
		}
		String value = !this.fieldboard.safeNeighbor()
				? this.fieldboard.minesIntoNeighbor() + ""
				: "";
		setText(value);
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == 1) {
			this.fieldboard.open();
		} else {
			this.fieldboard.changeTag();
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
	}
}
