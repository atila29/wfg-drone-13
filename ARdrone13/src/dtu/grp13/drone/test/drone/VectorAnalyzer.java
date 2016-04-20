package dtu.grp13.drone.test.drone;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;






import java.util.Map;

import org.opencv.core.Mat;
import org.opencv.core.Point;

import dtu.grp13.drone.vector.CoorSystem2;
import dtu.grp13.drone.vector.Matrix2;
import dtu.grp13.drone.vector.Vector2;

public class VectorAnalyzer {
	private Map<String, List<Vector2>> vektorSet;
	private String currentKey;
	private boolean run;
	private Map<String, List<Vector2>> sumVectorSet;
	private CoorSystem2 s = new CoorSystem2(10,10,150,150);
	
	public CoorSystem2 getCoordinateSystem() {
		return s;
	}

	public VectorAnalyzer(String name) {
		this.currentKey = name;
		
		vektorSet = new HashMap<>();
		sumVectorSet = new HashMap<>();
		
		vektorSet.put(name, new ArrayList<Vector2>());
		sumVectorSet.put(name, new ArrayList<Vector2>());
		run = true;
	}
	
	public void addVector(Mat m, Point pt1, Point pt2) {
		if(!run)
			return;
		Vector2 v1 = new Vector2(m.height() - pt1.x, m.width() - pt1.y);
		Vector2 v2 = new Vector2(m.height() - pt2.x, m.width() - pt2.y);
		
		Vector2 v = new Vector2(v1.getX() - v2.getX(), v1.getY() - v2.getY()); // DETTE SKAL FIXES!
		//System.out.println(v.getX() + " : " + v.getY() + " = " + pt1 + pt2);
		v = v.transform(new Matrix2(3, 3,-1, 0, 0,
										  0, 1, 0,
										  0, 0, 1));
		vektorSet.get(currentKey).add(v);
	}
	
	public void drawAllVectors(Mat mat) {
		List<Vector2> list = vektorSet.get(currentKey);
		for(Vector2 v : list) {
			s.drawLine(mat, new Vector2(0,0), v);
		}
	}
	
	public void drawSumVector(Mat mat) {
		List<Vector2> list = new ArrayList<>(vektorSet.get(currentKey));
		
		double x = 0;
		double y = 0;
		
		for(Vector2 v : list) {
			x += v.getX();
			y += v.getY();
		}
		Vector2 v = new Vector2(x/list.size(), y/list.size());
		s.drawLine(mat, new Vector2(0,0), v);
		this.reset();
		sumVectorSet.get(currentKey).add(v);
	}
	
	public void setCollection(String name) {
		if(vektorSet.containsKey(name)){
			this.currentKey = name;
		}
		else {
			this.currentKey = name;
			vektorSet.put(name, new ArrayList<Vector2>());
			sumVectorSet.put(name, new ArrayList<Vector2>());
		}
			
	}
	
	public void writeVectors(String name) {
		double x = 0;
		double y = 0;
		for(Vector2 v : sumVectorSet.get(name)) {
			System.out.println(v.getX() + " : " + v.getY());
			x += v.getX();
			y += v.getY();
		}
		x = x;
		y = y;
		Vector2 sum = new Vector2(x,y);
		System.out.println("Samlet v(" + name + ") : " + sum);
	}
	
	public void reset(){
		vektorSet.get(currentKey).clear();
	}
	
	public void stop() {
		run = false;
	}
	
	public void start() {
		run = true;
	}

}
