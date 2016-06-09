package dtu.grp13.drone.util;

import dtu.grp13.drone.vector.Vector2;

public class Wallmark {
	private String name;
	private Vector2 coordinate;
	private Wallmark left;
	private Wallmark right;
	
	public Wallmark(String name, Vector2 coordinate) {
		this.name = name;
		this.coordinate = coordinate;
	}
	
	@Override
	public boolean equals(Object a){
		if(a instanceof Wallmark)
			if(this.name.equals(a))
				return true;
			else
				return false;
		else
			return false;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Vector2 getCoordinate() {
		return coordinate;
	}
	public void setCoordinate(Vector2 coordinate) {
		this.coordinate = coordinate;
	}
	public Wallmark getLeft() {
		return left;
	}
	public void setLeft(Wallmark left) {
		this.left = left;
	}
	public Wallmark getRight() {
		return right;
	}
	public void setRight(Wallmark right) {
		this.right = right;
	}
	

}
