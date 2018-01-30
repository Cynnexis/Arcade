package main.fr.polytech.arcade.game;

import java.awt.*;

public abstract class RotableObject implements Rotable {
	
	protected Point centre;
	
	/* GETTER & SETTER */
	
	public Point getCentre() {
		return centre;
	}
	
	public void setCentre(Point centre) {
		this.centre = centre;
	}
}
