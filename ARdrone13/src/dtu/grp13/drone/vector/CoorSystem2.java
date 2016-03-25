package dtu.grp13.drone.vector;

import java.awt.Graphics;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class CoorSystem2 {
	private Vector2 origo;
	private Matrix2 flip, scale, transformValue;
	
	public CoorSystem2(double sx, double sy, double ox, double oy){
		origo = new Vector2(ox, oy);
		flip = new Matrix2(2,2, 1,0,
								0,-1);
		scale = new Matrix2(2,2, sx, 0,
								 0,	 sy);
		transformValue = flip.dot(scale);
	}
	
	public Vector2 transform(Vector2 v) {
		return transformValue.mul(v).add(origo);
	}
	
	public void drawLine(Mat mat, Vector2 p1, Vector2 p2) {
		Vector2 p1w = transform(p1);
		Vector2 p2w = transform(p2);
		
		Point v1 = new Point(p1w.getX(), p1w.getY());
		Point v2 = new Point(p2w.getX(), p2w.getY());
		//System.out.println(v1 +  " : " + v2);
		//System.out.println(p2.getX() + " : " + p2.getY());
		Imgproc.arrowedLine(mat, v1, v2, new Scalar(255, 0, 0));
	}
	
	public void drawPoint(Mat mat, Vector2 p) {
		Vector2 pw = transform(p);
		
		Imgproc.circle(mat, new Point(pw.getX(), pw.getY()), 2, new Scalar(0, 0, 255));
		
	}
	
	public void drawAxes(Mat mat){
		drawLine(mat, new Vector2(0,0), new Vector2(1,0));	
		drawLine(mat, new Vector2(0,0), new Vector2(0,1));
	}
}
