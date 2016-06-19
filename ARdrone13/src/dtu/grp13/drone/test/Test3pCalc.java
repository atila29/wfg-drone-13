package dtu.grp13.drone.test;

import org.opencv.core.Point;

public class Test3pCalc {

	public static void main(String[] args) {
		Point p1 = new Point(0, 0);
		Point p2 = new Point(4, 0);
		double d1 = 5;
		Point[] p3 = getP3(p1, 5, p2, 5);
		System.out.println("x: " + p3[1].x + " y: " + p3[1].y);
	}
	
    public static Point[] getP3(Point p1, double distanceFromP1, Point p2, double distanceFromP2) {
        double d = 4;

        if(d > (distanceFromP1 + distanceFromP2) || p1.equals(p2) || d < Math.abs(distanceFromP1 - distanceFromP2)) {
            // there does not exist a 3rd point, or there are an infinite amount of them
            return new Point[]{};
        }

        double a = (distanceFromP1*distanceFromP1 - distanceFromP2*distanceFromP2 + d*d) / (2*d);
        double h = Math.sqrt(distanceFromP1*distanceFromP1 - a*a);

        Point temp = new Point(p1.x + a*(p2.x - p1.x) / d, p1.y + a*(p2.y - p1.y) / d);

        return new Point[]{
                new Point(temp.x + h * (p2.y - p1.y) / d, temp.y - h * (p2.x - p1.x) / d),
                new Point(temp.x - h * (p2.y - p1.y) / d, temp.y + h * (p2.x - p1.x) / d)
        };
    }

}
