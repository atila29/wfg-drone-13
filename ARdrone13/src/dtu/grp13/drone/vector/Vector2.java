package dtu.grp13.drone.vector;

public class Vector2 {

	private double x,y;
	
	public Vector2(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public String toString() {
		return "{"+ this.x + " : "+ this.y+"}";
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
	
	public Vector2 subtract(Vector2 v) {
		return new Vector2(this.x - v.getX(), this.y - v.getY());
	}
	
	public Vector2 rotate(double radians) {
		double ca = Math.cos(radians);
		double sa = Math.sin(radians);
		double x = ca*this.getX()-sa*this.getY();
		double y = sa * this.getX() + ca* this.getY();
		return new Vector2(x, y);
	}
	
	public double getAngle(Vector2 v) {
		double dot = v.getX() * this.getX() + v.getY() * this.getY();
		double mag1 = Math.sqrt(Math.pow(this.getX(), 2) + Math.pow(this.getY(), 2));
		double mag2 = Math.sqrt(Math.pow(v.getX(), 2) + Math.pow(v.getY(), 2));
		double cosa = (dot/(mag1*mag2));
		double angle = Math.acos(cosa);
		return angle;
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
