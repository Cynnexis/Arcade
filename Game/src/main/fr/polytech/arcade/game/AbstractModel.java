package main.fr.polytech.arcade.game;

import java.util.Observable;

public abstract class AbstractModel extends Observable {
	
	protected void update() {
		setChanged();
		notifyObservers();
	}
}
