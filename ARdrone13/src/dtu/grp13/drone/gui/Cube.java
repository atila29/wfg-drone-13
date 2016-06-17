package dtu.grp13.drone.gui;

import java.util.List;

import dtu.grp13.drone.vector.Vector2;

public class Cube {
	
	private String color;
	private Vector2 coordinate;
	
	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Vector2 getCoordinate() {
		return coordinate;
	}

	public void setCoordinate(Vector2 coordinate) {
		this.coordinate = coordinate;
	}

	public Cube(String color, Vector2 coordinate) {
		this.color = color;
		this.coordinate = coordinate;
	}
	
	
}