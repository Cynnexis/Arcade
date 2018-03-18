package main.fr.polytech.arcade.game;

import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Observable;

public abstract class AbstractModel extends Observable {
	
	/**
	 * Trigger the modification status to <c>true</c> and notify all observers
	 */
	protected void snap(@Nullable Object argument) {
		setChanged();
		notifyObservers(argument);
	}
	protected void snap() {
		setChanged();
		notifyObservers();
	}
	protected void snap(@Nullable Object... list) {
		setChanged();
		notifyObservers(list);
	}
}
