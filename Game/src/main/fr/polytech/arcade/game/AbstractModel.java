package main.fr.polytech.arcade.game;

import java.util.Observable;

public abstract class AbstractModel extends Observable {
	
	/**
	 * Trigger the modification status to <c>true</c> and notify all observers
	 */
	protected void snap() {
		setChanged();
		notifyObservers();
	}
}
