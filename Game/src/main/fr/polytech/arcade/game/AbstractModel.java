package main.fr.polytech.arcade.game;

import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Observable;

/**
 * An observable abstract class. Add the "snap" method.
 */
public abstract class AbstractModel extends Observable {
	
	/**
	 * Trigger the modification status to <c>true</c> and notify all observers
	 */
	protected void snap(@Nullable Object argument) {
		setChanged();
		notifyObservers(argument);
	}
	/**
	 * Trigger the modification status to <c>true</c> and notify all observers
	 */
	protected void snap() {
		setChanged();
		notifyObservers();
	}
	/**
	 * Trigger the modification status to <c>true</c> and notify all observers
	 */
	protected void snap(@Nullable Object... list) {
		setChanged();
		notifyObservers(list);
	}
}
