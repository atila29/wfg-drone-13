package dtu.grp13.drone.test.drone;

import java.util.ArrayList;
import java.util.List;






import org.opencv.core.Mat;
import org.opencv.core.Point;

import dtu.grp13.drone.vector.CoorSystem2;
import dtu.grp13.drone.vector.Matrix2;
import dtu.grp13.drone.vector.Vector2;

public class VectorAnalyzer {
	private List<Vector2> list;
	
	private CoorSystem2 s = new CoorSystem2(10,10,200,400);
	
	public CoorSystem2 getCoordinateSystem() {
		return s;
	}

	public VectorAnalyzer() {
		list = new ArrayList<Vector2>();
	}
	
	public void addVector(Mat m, Point pt1, Point pt2) {
		Vector2 v1 = new Vector2(m.height() - pt1.x, m.width() - pt1.y);
		Vector2 v2 = new Vector2(m.height() - pt2.x, m.width() - pt2.y);
		
		Vector2 v = new Vector2(v1.getX() - v2.getX(), v1.getY() - v2.getY());
		System.out.println(v.getX() + " : " + v.getY() + " = " + pt1 + pt2);
		list.add(v);
	}
	
	public void drawAllVectors(Mat mat) {
		
		for(Vector2 v : list) {
			s.drawLine(mat, new Vector2(0,0), v);
		}
	}
	
	public void drawSumVector(Mat mat) {
		double x = 0;
		double y = 0;
		
		for(Vector2 v : list) {
			x += v.getX();
			y += v.getY();
		}
		Vector2 v = new Vector2(x/list.size(), y/list.size());
		s.drawLine(mat, new Vector2(0,0), v);
	}
	


}
