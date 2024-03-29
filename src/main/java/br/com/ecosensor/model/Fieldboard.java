package br.com.ecosensor.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.com.ecosensor.model.enums.FieldEvent;

public class Fieldboard {
	
	private final int line;
	private final int column;
	
	private boolean openned = false;
	private boolean mineded = false;
	private boolean markned = false;
	
	private List<Fieldboard> neighbors = new ArrayList<>();
	private Set<FieldObserver> observers = new HashSet<>();
	
	/**
	 * private List<BiConsumer<Fieldboard, FieldEvent>> observers = new
	 * ArrayList<>();
	 */
	
	Fieldboard(int line, int column) {
		this.line = line;
		this.column = column;
	}
	
	public void registerObserver(FieldObserver observer) {
		this.observers.add(observer);
	}
	
	private void notifyObservers(FieldEvent event) {
		this.observers.stream().forEach(o -> o.occurredEvent(this, event));
	}
	
	boolean addNeighbor(Fieldboard neighbor) {
		boolean lineDifferent = this.line != neighbor.line;
		boolean columnDifferent = this.column != neighbor.column;
		boolean diagonal = lineDifferent && columnDifferent;
		
		int deltaLine = Math.abs(this.line - neighbor.line);
		int deltaColumn = Math.abs(this.column - neighbor.column);
		int deltaResult = deltaLine + deltaColumn;
		
		if ((deltaResult == 1 && !diagonal) ^ (deltaResult == 2 && diagonal)) {
			this.neighbors.add(neighbor);
			return true;
		} else {
			return false;
		}
	}
	
	void changeTag() {
		if (!this.openned) {
			this.markned = !this.isMarkned();
			if (markned) {
				notifyObservers(FieldEvent.TOMARK);
			} else {
				notifyObservers(FieldEvent.UNMARK);
			}
		}
	}
	
	boolean open() {
		if (!this.openned && !this.isMarkned()) {
			this.openned = true;
			if (this.mineded) {
				notifyObservers(FieldEvent.EXPLODE);
				return true;
			}
			
			setOpenned(true);
			
			if (safeNeighbor()) {
				this.neighbors.forEach(n -> n.open());
			}
			return true;
		} else {
			return false;
		}
	}
	
	boolean safeNeighbor() {
		return this.neighbors.stream().noneMatch(n -> n.mineded);
	}
	
	public boolean isMarkned() {
		return this.markned;
	}
	
	void undermine() {
		this.mineded = true;
	}
	
	public boolean isMineded() {
		return this.mineded;
	}
	
	void setOpenned(boolean openned) {
		this.openned = openned;
		if (openned) {
			notifyObservers(FieldEvent.OPEN);
		}
	}
	
	public boolean isOpen() {
		return this.openned;
	}
	
	public boolean isClose() {
		return !isOpen();
	}
	
	public int getLine() {
		return this.line;
	}
	
	public int getColumn() {
		return this.column;
	}
	
	boolean goalComplete() {
		boolean uncovereField = !this.mineded && this.openned;
		boolean protectedField = this.mineded && this.markned;
		return uncovereField || protectedField;
	}
	
	long minesIntoNeighbor() {
		return this.neighbors.stream().filter(n -> n.mineded).count();
	}
	
	void restart() {
		this.openned = false;
		this.mineded = false;
		this.markned = false;
	}
	
}
