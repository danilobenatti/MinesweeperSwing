package br.com.ecosensor.model;

import br.com.ecosensor.model.enums.FieldEvent;

@FunctionalInterface
public interface FieldObserver {
	
	public void occurredEvent(Fieldboard fieldboard, FieldEvent event);
	
}
