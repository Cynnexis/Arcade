package main.fr.polytech.arcade.game.piece;

import fr.berger.enhancedlist.Point;

public abstract class RotableObject implements Rotable {
	
	protected Point centre;
	
	public RotableObject() {
		setCentre(new Point(0, 0));
	}
	
	/* GETTER & SETTER */
	
	public Point getCentre() {
		return centre;
	}
	
	public void setCentre(Point centre) {
		this.centre = centre;
	}
}
