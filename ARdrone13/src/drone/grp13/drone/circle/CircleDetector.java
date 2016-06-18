package drone.grp13.drone.circle;

import java.util.ArrayList;
import java.util.List;

import javax.swing.plaf.basic.BasicBorders.MarginBorder;

import org.bytedeco.javacpp.opencv_ximgproc.Edge;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class CircleDetector {
	private final Scalar colorOfRects = new Scalar(255, 0, 0);
	private final Scalar textColor = new Scalar(0, 255, 0);
	
	public void findCicles(Mat src, Mat dst) {
		
		Mat hsvImg = new Mat();
		Imgproc.cvtColor(src, hsvImg, Imgproc.COLOR_BGR2HSV);
		Mat blueImg = new Mat();
		src.copyTo(blueImg);
		Core.inRange(hsvImg, new Scalar(100, 0, 0), new Scalar(145, 255, 255), blueImg);
	    
//		Mat gray = new Mat();
//	    Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
//	    Imgproc.blur(gray, gray, new Size(3, 3));
//
//	    Mat edges = new Mat();
//	    int lowThreshold = 40;
//	    int ratio = 3;
//		Imgproc.Canny(blueImg, dst, lowThreshold, lowThreshold * ratio);
//
	    Mat circles = new Mat();
		Imgproc.GaussianBlur(blueImg, blueImg, new Size(9, 9), 3, 3);
	    Imgproc.HoughCircles(blueImg, circles, Imgproc.CV_HOUGH_GRADIENT, 1, blueImg.rows()/8, 200, 100, 0, 0);
	    
	    double x = 0.0;
	    double y = 0.0;
	    int r = 0;

	    for( int i = 0; i < circles.cols(); i++ )
	    {
	      double[] data = circles.get(0, i);
	      
	      for(int j = 0 ; j < data.length ; j++){
	           x = Math.round(data[0]); 
	           y = Math.round(data[1]);
	           r = (int) Math.round(data[2]);
	      }
	      Point center = new Point(x,y);
	      
	      Imgproc.circle(dst, center, r, new Scalar(0, 0, 255), 2);
	    }	
	}

}
