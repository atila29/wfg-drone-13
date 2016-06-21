package dtu.grp13.drone.core;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.opencv.core.Rect;

import com.google.zxing.Result;

import dtu.grp13.drone.util.WFGUtilities;
import dtu.grp13.drone.vector.Vector2;

public class PositionSystem {

	// private double horizontalRadians = 1.32899694; // 75 grader
	private double horizontalRadians = 1.39949138; // 80 grader
	private double focal = 1118.581907;
	private double paperHeight = 40.9;
	private double widthRes = 1280;
	private double b = (widthRes / 2) / (Math.tan(horizontalRadians / 2));
	private Map<String, Vector2> wallMarks;

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
	
	public List<Vector2> getWallmarks(){
		return new ArrayList<Vector2>(wallMarks.values());
	}

	public String getRight(String name) {
		String n = name.replace("W", "");
		int w = Integer.parseInt(n.split("\\.")[0]);
		int wn = Integer.parseInt(n.split("\\.")[1]);

		if (wn == 4) {
			if (w == 3) {
				return "W00.00";
			} else
				return "W0" + (w + 1) + ".00";
		} else {
			return "W0" + w + ".0" + (1 + wn);
		}
	}

	public String getLeft(String name) {
		String n = name.replace("W", "");
		int w = Integer.parseInt(n.split("\\.")[0]);
		int wn = Integer.parseInt(n.split("\\.")[1]);

		if (wn == 0) {
			if (w == 0) {
				return "W03.04";
			} else
				return "W0" + (w - 1) + ".00";
		} else {
			return "W0" + w + ".0" + (wn - 1);
		}
	}

	// Index 0 er venstre og index 1 er højre
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
	
	public double calcDistance(Vector2 p1, Vector2 p2) {
		double dist = Math
				.sqrt(Math.pow(p1.getX() - p2.getX(), 2) + Math.pow(p1.getY() - p2.getY(), 2));

		return dist;
	}

	// Index 0 er venstre beta og index 1 er højre beta
	public double[] calcBeta(List<Rect> cords) {

		List<Double> betaList = new ArrayList<Double>();
		double beta = 0.0;
		double t = 0;
		double[] betaArray = new double[2];
		List<Rect> sortedCords = WFGUtilities.sortResults(cords, 0, cords.size() - 1);

		for (int i = 0; i < sortedCords.size(); i++) {
			double x = sortedCords.get(i).x + sortedCords.get(i).width / 2;
			double y = sortedCords.get(i).y + sortedCords.get(i).height / 2;
			sortedCords.get(i).x = (int) x;
			sortedCords.get(i).y = (int) y;

			if (sortedCords.get(i).x > 640) {
				t = sortedCords.get(i).x - 640;
			} else {
				t = 640 - sortedCords.get(i).x;
			}
			beta = Math.atan(t / b);
			System.out.println("i = " + i + " beta = " + beta);
			betaList.add(beta);

		}
		
		if (sortedCords.get(1).x > 640) {
			betaArray[1] = betaList.get(2) - betaList.get(1);
			if (sortedCords.get(0).x > 640) {
				betaArray[0] = betaList.get(1) - betaList.get(0);
			} else {
				betaArray[0] = betaList.get(1) + betaList.get(0);
			}

		} else if (sortedCords.get(1).x < 640) {
			betaArray[0] = betaList.get(0) - betaList.get(1);
			if (sortedCords.get(2).x < 640) {
				betaArray[1] = betaList.get(1) - betaList.get(2);
			} else {
				betaArray[1] = betaList.get(1) + betaList.get(2);
			}
		}
		return betaArray;
	}

	public double calcRadius(double afstand, double vinkel) {
		double radius = (0.5 * afstand) / Math.sin(vinkel);
		return radius;
	}

	public Vector2 calcCenter(Vector2 v1, Vector2 v2, double afstand, double vinkel) {

		double var0 = Math
				.sqrt(Math.pow(Math.abs(-v2.getY() + v1.getY()), 2) + Math.pow(Math.abs(-v2.getX() + v1.getX()), 2));
		double var1 = Math.sqrt(Math.pow(afstand, 2) / Math.pow(Math.sin(vinkel), 2) - Math.pow(afstand, 2));
		double x = 0.5 * (v2.getY() - v1.getY()) / var0 * var1 + (0.5 * v1.getX()) + (0.5 * v2.getX());
		double y = 0.5 * (-v2.getX() + v1.getX()) / var0 * var1 + (0.5 * v1.getY()) + (0.5 * v2.getY());
		return new Vector2(x, y);
	}

	public Vector2 calcIntersection(String qr, List<Rect> qrCordList) {

		if (qrCordList.size() == 3) {
			Vector2 qrVec = getVec(qr);
			Vector2 leftVec = getVec(getLeft(qr));
			Vector2 rightVec = getVec(getRight(qr));

			double[] distArray = calcDistance(qr);
			double[] angleArray = calcBeta(qrCordList);

			Vector2 cent1 = calcCenter(leftVec, qrVec, distArray[0], angleArray[0]);
			Vector2 cent2 = calcCenter(qrVec, rightVec, distArray[1], angleArray[1]);
			double radius1 = calcRadius(distArray[0], angleArray[0]);
			double radius2 = calcRadius(distArray[1], angleArray[1]);
			System.out.println("beta1 = " + angleArray[0]);
			System.out.println("beta2 = " + angleArray[1]);

			// Test code
			// cent1 = new Vector2(5.995, -1.748);
			// cent2 = new Vector2(5.433, 5.467);
			// radius1 = 6.245;
			// radius2 = 5.628;

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
	
	public Vector2 calcPosition(List<Rect> qrCordList, Result qrResult, int p1Index) {
		Vector2 p1 = new Vector2(0, 0);
		Vector2 p2 = new Vector2(0, 0);
		int p2Index = 0;
		
		if (qrCordList.size() == 3) {
			double leftDif1 = Math.abs(qrCordList.get(1).x - qrCordList.get(0).x);
			double rightDif2 = Math.abs(qrCordList.get(1).x - qrCordList.get(2).x);
			
			if (leftDif1 < rightDif2) {
				p1 = getVec(qrResult.getText());
				p2 = getVec(getLeft(qrResult.getText()));
				p2Index = 0;
			} else {
				p1 = getVec(qrResult.getText());
				p2 = getVec(getRight(qrResult.getText()));
				p2Index = 2;
			}
			
		} else {
			if (p1Index == 0) {
				p1 = getVec(qrResult.getText());
				p2 = getVec(getRight(qrResult.getText()));
				p2Index = 1;
			} else {
				p1 = getVec(qrResult.getText());
				p2 = getVec(getLeft(qrResult.getText()));
				p2Index = 0;
			}
		}

//		double distp3p1 = 0.0;
//		double distp3p2 = 0.0;
		
		double distp1p2 = calcDistance(p1, p2);
		double distp3p1 = (paperHeight*focal)/qrCordList.get(p1Index).height;
		double distp3p2 = (paperHeight*focal)/qrCordList.get(p2Index).height;
		System.out.println("dist to p1: " + distp3p1);
		System.out.println("dist to p2: " + distp3p2);
		
		double a = (distp3p1*distp3p1 - distp3p2*distp3p2 + distp1p2*distp1p2) / (2*distp1p2);
        double h = Math.sqrt(distp3p1*distp3p1 - a*a);

        Vector2 temp = new Vector2(p1.getX() + a*(p2.getX() - p1.getX()) / distp1p2, p1.getY() + a*(p2.getY() - p1.getY()) / distp1p2);
        Vector2 v3 = new Vector2(temp.getX() - h * (p2.getY() - p1.getY()) / distp1p2, temp.getY() + h * (p2.getX() - p1.getX()) / distp1p2);
        Vector2 v4 = new Vector2(temp.getX() + h * (p2.getY() - p1.getY()) / distp1p2, temp.getY() - h * (p2.getX() - p1.getX()) / distp1p2);
//        System.out.println("V3 x: " + v3.getX() + " y: " + v3.getY());
//        System.out.println("V4 x: " + v4.getX() + " y: " + v4.getY());
        WFGUtilities.LOGGER.info("qr: " + qrResult.getText());
        WFGUtilities.LOGGER.info("p1: " + p1.toString());
        WFGUtilities.LOGGER.info("p2: " + p2.toString());
        WFGUtilities.LOGGER.info("Distance p3 til p1: " + distp3p1);
        WFGUtilities.LOGGER.info("Distance p3 til p2: " + distp3p2);
        
		if (v3.getX() < 0 || v3.getY() < 0 || v3.getX() > 926 || v3.getY() > 1060) {
			return v4;
		} else {
			return v3;
		}
		
	}
	
	public double findOrientation(Vector2 position, Rect qrRect, String qr) {
		Vector2 qrVec = getVec(qr);
		Vector2 tempOrient = qrVec.subtract(position);
		
		double t = 0.0;
		double beta = 0.0;
		double qrMid = qrRect.x + qrRect.width/2;
		if (qrRect.x > 640) {
			t = qrMid - 640;
			beta = - Math.atan(t / b);
		} else {
			t = 640 - qrMid;
			beta = Math.atan(t / b);
			
		}
//		System.out.println("rect x: " + qrMid);
//		System.out.println("t = " + t);
//		System.out.println("beta = " + Math.toDegrees(beta));
		
//		tempOrient = tempOrient.rotate(beta);
		
		return (tempOrient.getAngle(new Vector2(0,1)) + beta);
	}
	
	

}
