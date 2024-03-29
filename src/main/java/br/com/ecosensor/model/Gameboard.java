package br.com.ecosensor.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import br.com.ecosensor.model.enums.FieldEvent;

public class Gameboard implements FieldObserver {
	
	private final int lines;
	private final int columns;
	private final int mines;
	
	private final List<Fieldboard> fields = new ArrayList<>();
	private final List<Consumer<ResultEvent>> observers = new ArrayList<>();
	
	public int getLines() {
		return lines;
	}
	
	public int getColumns() {
		return columns;
	}
	
	public Gameboard(int lines, int columns, int mines) {
		this.lines = lines;
		this.columns = columns;
		this.mines = mines;
		
		generateFields();
		associateNeighbors();
		raffleMines();
		
	}
	
	public void forEachField(Consumer<Fieldboard> function) {
		this.fields.forEach(function);
	}
	
	public void registerObserver(Consumer<ResultEvent> observer) {
		this.observers.add(observer);
	}
	
	private void notifyObservers(boolean result) {
		this.observers.stream().forEach(o -> o.accept(new ResultEvent(result)));
	}
	
	public void open(int line, int column) {
		this.fields.parallelStream()
				.filter(c -> c.getLine() == line && c.getColumn() == column)
				.findFirst().ifPresent(c -> c.open());
	}
	
	public void changeTag(int line, int column) {
		this.fields.parallelStream()
				.filter(c -> c.getLine() == line && c.getColumn() == column)
				.findFirst().ifPresent(c -> c.changeTag());
	}
	
	private void generateFields() {
		for (int i = 0; i < lines; i++) {
			for (int j = 0; j < columns; j++) {
				Fieldboard field = new Fieldboard(i, j);
				field.registerObserver(this);
				this.fields.add(field);
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
	
	@Override
	public void occurredEvent(Fieldboard fieldboard, FieldEvent event) {
		if (event == FieldEvent.EXPLODE) {
			showMines();
			notifyObservers(false);
		} else if (goalComplete()) {
			notifyObservers(true);
		}
		
	}
	
	private void showMines() {
		this.fields.stream().filter(f -> f.isMineded())
				.filter(f -> !f.isMarkned()).forEach(f -> f.setOpenned(true));
	}
	
}
