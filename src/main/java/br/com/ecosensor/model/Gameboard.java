package br.com.ecosensor.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Gameboard {
	
	private int lines;
	private int columns;
	private int mines;
	
	private List<Fieldboard> fields = new ArrayList<>();
	
	public Gameboard(int lines, int columns, int mines) {
		this.lines = lines;
		this.columns = columns;
		this.mines = mines;
		
		generateFields();
		associateNeighbors();
		raffleMines();
		
	}
	
	public void open(int line, int column) {
		try {
			this.fields.parallelStream()
					.filter(c -> c.getLine() == line && c.getColumn() == column)
					.findFirst().ifPresent(c -> c.open());
		} catch (Exception e) {
			// FIXME Fix implementation for method 'open'
			this.fields.forEach(f -> f.setOpenned(true));
			throw e;
		}
	}
	
	public void changeTag(int line, int column) {
		this.fields.parallelStream()
				.filter(c -> c.getLine() == line && c.getColumn() == column)
				.findFirst().ifPresent(c -> c.changeTag());
	}
	
	private void generateFields() {
		for (int i = 0; i < lines; i++) {
			for (int j = 0; j < columns; j++) {
				this.fields.add(new Fieldboard(i, j));
			}
		}
	}
	
	private void associateNeighbors() {
		for (Fieldboard field1 : this.fields) {
			for (Fieldboard field2 : this.fields) {
				field1.addNeighbor(field2);
			}
		}
	}
	
	private void raffleMines() {
		long armedMines = 0;
		Predicate<Fieldboard> mineded = f -> f.isMineded();
		do {
			int rand = (int) (Math.random() * this.fields.size());
			this.fields.get(rand).undermine();
			armedMines = this.fields.stream().filter(mineded).count();
		} while (armedMines < this.mines);
	}
	
	public boolean goalComplete() {
		return this.fields.stream().allMatch(f -> f.goalComplete());
	}
	
	public void restart() {
		this.fields.stream().forEach(c -> c.restart());
		raffleMines();
	}
	
}
