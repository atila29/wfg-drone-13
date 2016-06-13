package dtu.grp13.drone.core;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.opencv.core.Rect;

import com.google.zxing.Result;

import dtu.grp13.drone.util.WFGUtilities;
import dtu.grp13.drone.util.Wallmark;
import dtu.grp13.drone.util.WallmarkLinkedList;
import dtu.grp13.drone.vector.Vector2;

public class PositionSystem {

	//private double horizontalRadians = 1.32899694; // 75 grader
	private double horizontalRadians = 1.39949138; // 80 grader
	private double widthRes = 1280;
	private Map<String, Vector2> wallMarks;
	private double b = (widthRes / 2) / (Math.tan(horizontalRadians / 2));

	public Vector2 getVec(String name) {
		return wallMarks.get(name);
	}

	public PositionSystem() throws IOException {
		wallMarks = new HashMap<>();

		String csvMarks = "./resources/WallCoordinates.csv";
		BufferedReader reader = null;
		String csvSplit = ";";
		String line = "";

		try {
			reader = new BufferedReader(new FileReader(csvMarks));
			reader.readLine();
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
				String[] splitedLine = line.split(csvSplit);
				double x = Double.parseDouble(splitedLine[1].equals("-") ? "-1" : splitedLine[1]);
				double y = Double.parseDouble(splitedLine[2].equals("-") ? "-1" : splitedLine[2]);
				wallMarks.put(splitedLine[0], new Vector2(x, y));
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			reader.close();
		}

	}
	
	public String getRight(String name) {
		String n = name.replace("W", "");
		int w = Integer.parseInt(n.split("\\.")[0]);
		int wn = Integer.parseInt(n.split("\\.")[1]);
		
		if(wn == 4){
			if(w == 3){
				return "W00.00";
			}
			else 
				return "W0"+(w+1) + ".00";
		}
		else {
			return "W0"+w+".0"+(1+wn);
		}
	}
	
	public String getLeft(String name) {
		String n = name.replace("W", "");
		int w = Integer.parseInt(n.split("\\.")[0]);
		int wn = Integer.parseInt(n.split("\\.")[1]);
		
		if(wn == 0){
			if(w == 0){
				return "W03.04";
			}
			else 
				return "W0"+(w-1) + ".00";
		}
		else {
			return "W0"+w+".0"+(wn-1);
		}
	}
	


	// Index 0 er venstre og index 1 er h�jre
	public double[] calcDistance(String qr) {
		Vector2 qrVec = getVec(qr);
		Vector2 leftVec = getVec(getLeft(qr));
		Vector2 rightVec = getVec(getRight(qr));

		double[] distArray = new double[2];

		distArray[0] = Math
				.sqrt(Math.pow(qrVec.getX() - leftVec.getX(), 2) + Math.pow(qrVec.getY() - leftVec.getY(), 2));
		distArray[1] = Math
				.sqrt(Math.pow(qrVec.getX() - rightVec.getX(), 2) + Math.pow(qrVec.getY() - rightVec.getY(), 2));

		
		return distArray;

	}

	// Index 0 er venstre beta og index 1 er h�jre beta
	public double[] calcBeta(List<Rect> cords) {

		List<Double> betaList = new ArrayList<Double>();
		double beta = 0.0;
		double t = 0;
		double[] betaArray = new double[2];
		List<Rect> qrCordList = WFGUtilities.sortResults(cords, 0, cords.size()-1);

		for (int i = 0; i < qrCordList.size(); i++) {
			double x = qrCordList.get(i).x + qrCordList.get(i).width/2;
			double y = qrCordList.get(i).y + qrCordList.get(i).height/2;
			qrCordList.get(i).x = (int) x;
			qrCordList.get(i).y = (int) y;
			//System.out.println("index = " + i + " value = " + qrCordList.get(i).x);

			if (qrCordList.get(i).x > 640) {
				t = qrCordList.get(i).x - 640;
			} else {
				t = 640 - qrCordList.get(i).x;
			}
			beta = Math.atan(t / b);
			System.out.println("i = " + i + " beta = " + beta);
			betaList.add(beta);

		}
		if (qrCordList.get(1).x > 640) {
			betaArray[1] = betaList.get(2) - betaList.get(1);
			if (qrCordList.get(0).x > 640) {
				betaArray[0] = betaList.get(1) - betaList.get(0);
			} else {
				betaArray[0] = betaList.get(1) + betaList.get(0);
			}

		} else if (qrCordList.get(1).x < 640) {
			betaArray[0] = betaList.get(0) - betaList.get(1);
			if (qrCordList.get(2).x < 640) {
				betaArray[1] = betaList.get(1) - betaList.get(2);
			} else {
				betaArray[1] = betaList.get(1) + betaList.get(2);
			}
		}
		//System.out.println(betaArray[0] + " --- " + betaArray[1]);

		return betaArray;
	}

	public double calcRadius(double afstand, double vinkel) {
		double radius = (0.5 * afstand) / Math.sin(vinkel);
		//System.out.println("Radius: " + radius);
		return radius;
	}

//	public Vector2 calcCenter(Rect p1, Rect p2, double afstand, double vinkel) {
//		
//
//		double var0 = Math.sqrt(Math.pow(Math.abs(-p2.y + p1.y), 2) + Math.pow(Math.abs(-p2.x + p1.x), 2));
//		double var1 = Math.sqrt(Math.pow(afstand, 2) / Math.pow(Math.sin(vinkel), 2) - Math.pow(afstand, 2));
//		double x = 0.5 * (p2.y - p1.y) / var0 * var1 + (0.5 * p1.x) + (0.5 * p2.x);
//		double y = 0.5 * (-p2.x + p1.x) / var0 * var1 + (0.5 * p1.y) + (0.5 * p2.y);
//		System.out.println("x: " + x + " --- y: " + y);
//		return new Vector2(x, y);
//	}
	
	public Vector2 calcCenter(Vector2 v1, Vector2 v2, double afstand, double vinkel) {
		
		double var0 = Math.sqrt(Math.pow(Math.abs(-v2.getY() + v1.getY()), 2) + Math.pow(Math.abs(-v2.getX() + v1.getX()), 2));
		double var1 = Math.sqrt(Math.pow(afstand, 2) / Math.pow(Math.sin(vinkel), 2) - Math.pow(afstand, 2));
		double x = 0.5 * (v2.getY() - v1.getY()) / var0 * var1 + (0.5 * v1.getX()) + (0.5 * v2.getX());
		double y = 0.5 * (-v2.getX() + v1.getX()) / var0 * var1 + (0.5 * v1.getY()) + (0.5 * v2.getY());
		//System.out.println("x: " + x + " --- y: " + y);
		return new Vector2(x, y);
	}

	public Vector2 calcIntersection(String qr, List<Rect> qrCordList) {

		if (qrCordList.size() == 3) {
			
			double[] distArray = calcDistance(qr);
			double[] betaArray = calcBeta(qrCordList);
//			betaArray[0] = 0.519409985;
//			betaArray[1] = 0.416261027;
			
			Vector2 qrVec = getVec(qr);
			Vector2 leftVec = getVec(getLeft(qr));
			Vector2 rightVec = getVec(getRight(qr));
//			System.out.println("Left = " + leftVec.getX());
//			System.out.println("Mid = " + qrVec.getX());
//			System.out.println("Right = " + rightVec.getX());

			//Vector2 cent1 = calcCenter(qrCordList.get(0), qrCordList.get(1), distArray[0], betaArray[0]);
			//Vector2 cent2 = calcCenter(qrCordList.get(1), qrCordList.get(2), distArray[1], betaArray[1]);
			
			Vector2 cent1 = calcCenter(leftVec, qrVec, distArray[0], betaArray[0]);
			Vector2 cent2 = calcCenter(qrVec, rightVec, distArray[1], betaArray[1]);
			double radius1 = calcRadius(distArray[0], betaArray[0]);
			double radius2 = calcRadius(distArray[1], betaArray[1]);
			System.out.println("beta1 = " + betaArray[0]);
			System.out.println("beta2 = " + betaArray[1]);
			
//			cent1 = new Vector2(5.995, -1.748);
//			cent2 = new Vector2(5.433, 5.467);
//			radius1 = 6.245;
//			radius2 = 5.628;
			
			double d = Math.sqrt(Math.pow(cent1.getX() - cent2.getX(), 2) + Math.pow(cent1.getY() - cent2.getY(), 2));
			double t1 = Math.pow(radius1, 2) - Math.pow(radius2, 2) + Math.pow(d, 2);
			double d1 = t1 / (2 * d);
			double h = Math.sqrt(Math.abs(Math.pow(radius1, 2) - Math.pow(d1, 2)));
			double x3 = cent1.getX() + (d1 * (cent2.getX() - cent1.getX())) / d;
			double y3 = cent1.getY() + (d1 * (cent2.getY() - cent1.getY())) / d;
			double x4 = x3 + (h * (cent2.getY() - cent1.getY())) / d;
			double y4 = y3 - (h * (cent2.getX() - cent1.getX())) / d;
			return new Vector2(x4, y4);

		}
		return null;
	}

}
