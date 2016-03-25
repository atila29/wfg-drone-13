package dtu.grp13.drone.vector;

public class Vector2 {

	private double x,y;
	
	public Vector2(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	// måske fejl?
	public Vector2(Matrix2 m) {
		this.x = m.getFromIndex(1, 1);
		this.y = m.getFromIndex(1, 2);
	}
	
	public Vector2 translate(Vector2 transVector) {
		return new Vector2(this.getX()+transVector.getX(), this.getY()+transVector.getY());
	}
	
	public Vector2 transform(Matrix2 T) {
		Matrix2 P = new Matrix2(1,3, this.getX(), this.getY(), 1);
		Matrix2 temp = P.dot(T);
		return new Vector2(temp.getFromIndex(1, 1), temp.getFromIndex(1, 2));
		
	}
	
	public Vector2 add(Vector2 v) {
		return new Vector2(this.x + v.getX(), this.y + v.getY());
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
	
	
}
