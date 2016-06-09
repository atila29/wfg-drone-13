package dtu.grp13.drone.cube;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.imgproc.Imgproc;

import dtu.grp13.drone.vector.Vector2;

public class CubeDetector {
	private final int minSizeOfRects = 40;
	private final Scalar colorOfRects = new Scalar(255,0,0);
	private final Scalar textColor = new Scalar(0, 255, 0);
	
	public List<Rect> findRects(Mat src, Mat dst) {
		List<MatOfPoint> edges = new ArrayList<MatOfPoint>();
		Imgproc.findContours(src, edges, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
		List<Rect> ret = new ArrayList<Rect>();
		for(int i = 0; i < edges.size(); i++) {
			Rect rect = bindRects(edges.get(i));
			if(Point2D.distance(rect.tl().x, rect.tl().y, rect.br().x, rect.br().y) > minSizeOfRects) {
				ret.add(rect);
				Imgproc.rectangle(dst, rect.tl(), rect.br(), colorOfRects);
			}
		}
		return ret;
	}
	
	public List<Rect> isolateinterestingRects(List<Rect> rects, double aRatio){
		List<Rect> QR = new ArrayList<Rect>();
		int currentRatio = 0;
		for(int i = 0; i < rects.size(); i++) {
			currentRatio = rects.get(i).height/rects.get(i).width;
			if(aRatio-0.1 < currentRatio && currentRatio < aRatio+0.1){
				QR.add(rects.get(i));
				System.out.println(QR.size());
			}
		}
		return QR;
	}
	
	public void getQr(Mat src, Mat dst) {
		List<MatOfPoint> edges = new ArrayList<MatOfPoint>();
		Mat s = src.clone();
		// lidt billedebahandling
		Imgproc.cvtColor(s, s, Imgproc.COLOR_BGR2GRAY);
		Imgproc.GaussianBlur(s, s, new Size(3,3), 3);		
		Imgproc.Canny(s, s, 100, 200); // læs op på, noget med lysforhold
		
		Imgproc.findContours(s, edges, new Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
		for(int i = 0; i < edges.size(); i++){
			MatOfPoint2f mop2 = new MatOfPoint2f();
			edges.get(i).convertTo(mop2, CvType.CV_32FC1);
			double marg = 0.01f;
			double ep = marg * Imgproc.arcLength(mop2, true);
			Imgproc.approxPolyDP(mop2, mop2, ep, true);
			mop2.convertTo(edges.get(i), CvType.CV_32S);
			
			if(edges.get(i).total() == 4){
				Imgproc.drawContours(dst, edges, i, new Scalar(255,0,0), 4);
				
			}
		}
	}
	
	public List<Rect> isolateInterestingCubes(List<Rect> rects, int dif) {
		List<Rect> ret = new ArrayList<Rect>();
		for(int i = 0; i < rects.size(); i++) {
			if(rects.get(i).height > rects.get(i).width-dif && rects.get(i).height < rects.get(i).width+dif){
				ret.add(rects.get(i));
			}
		}
		return ret;
	}
	
	public void findCubes(Mat src, Mat dst, List<Rect> rects) {
		for(int i = 0; i < rects.size(); i++) {
			if(rects.get(i).height > rects.get(i).width-3 && rects.get(i).height < rects.get(i).width+3) {
				Mat c = src.submat(rects.get(i));
				int redCount = 0;
				int greenCount = 0;
				
				int totalCount = c.cols()/5 + c.rows() / 5;
				for(int x = 0 ; x < c.cols(); x+=5) {
					for (int y = 0; y < c.rows(); y+=5) {
						double[] point = c.get(y, x);
						// BGR[] 0 : blue, 1: green, 2: red
						if(point[0] < 50 && point[1] < 50 && point[2] > 80)
							redCount ++;
						if(point[0] < 50 && point[1] > 80 && point[2] < 50) {
							greenCount ++;
						}
					}
				}
				String text = "NaN";
				if(totalCount*0.6 < redCount) {
					text = "red";
				} else if (totalCount*0.6 < greenCount) {
					text = "green";
				} 
				Imgproc.putText(dst, text, rects.get(i).tl(), Core.FONT_HERSHEY_PLAIN, 1, textColor,2);	
			}
		}
	}
	

	//	xtra params evt: Scalar col, int margin
	public Scalar findCubeColor(Mat src, Mat dst) {
		FeatureDetector fd = FeatureDetector.create(FeatureDetector.PYRAMID_SIMPLEBLOB);
		MatOfKeyPoint keypoints = new MatOfKeyPoint();
		fd.detect(src, keypoints);
		List<Scalar> colOfPoints = new ArrayList<Scalar>();
		double blue = 0, green = 0, red = 0;
		int n = 0;
		for(int x = 0 ; x < src.cols(); x+=5) {
			for (int y = 0; y < src.rows(); y+=5) {
				double[] point = src.get(y, x);
				// BGR[] 0 : blue, 1: green, 2: red
				colOfPoints.add(new Scalar(point));
				blue += point[0];
				green += point[1];
				red += point[2];
				n++;
			}
		}
		
		double[] val = {(blue/n) , (green/n), (red/n)};
		
		return new Scalar(val);
	}
	
	public void findSpecificCubeColor(Mat src, Mat dst, Filterable filter) {
//		FeatureDetector fd = FeatureDetector.create(FeatureDetector.PYRAMID_SIMPLEBLOB);
//		MatOfKeyPoint keypoints = new MatOfKeyPoint();
//		fd.detect(src, keypoints);
		List<Scalar> colOfPoints = new ArrayList<Scalar>();
		
/*		double blue = 0, green = 0, red = 0;
		for(int x = 0 ; x < src.cols(); x+=5) {
			for (int y = 0; y < src.rows(); y+=5) {
				double[] point = src.get(y, x);
				// BGR[] 0 : blue, 1: green, 2: red
				Scalar currentPixColor = new Scalar(point);
				colOfPoints.add(new Scalar(point));
			}
		}*/
		// dette er en dyr methode da en gør brug af inRange()
		// ville kunnne spare tid ved at skifte til egen inRange, der checker 
		// flere parametre samtidig, eller bruge bedre fra openCV 
		Mat fGreen	= src.clone(); 
		Mat fRed 	= src.clone();
		
		filter.processGreen(fGreen, fGreen);
		filter.processGreen(fRed, fRed);
		List<Rect> rectsGreen = findRects(fGreen, new Mat());
		List<Rect> rectsRed = findRects(fRed, new Mat());
		
		for(int i = 0; i < rectsGreen.size(); i++) {
			if(rectsGreen.get(i).height > rectsGreen.get(i).width-5 && rectsGreen.get(i).height < rectsGreen.get(i).width+5) {
				Imgproc.putText(dst, "GREEN", rectsGreen.get(i).tl(), Core.FONT_HERSHEY_PLAIN, 1, textColor,2);
				Imgproc.rectangle(dst, rectsGreen.get(i).tl(), rectsGreen.get(i).br(), new Scalar(255, 0, 0));
			}
		}
		for(int i = 0; i < rectsRed.size(); i++) {
			if(rectsRed.get(i).height > rectsRed.get(i).width-5 && rectsRed.get(i).height < rectsRed.get(i).width+5) {
				Imgproc.putText(dst, "RED", rectsRed.get(i).tl(), Core.FONT_HERSHEY_PLAIN, 1, textColor,2);
				Imgproc.rectangle(dst, rectsRed.get(i).tl(), rectsRed.get(i).br(), new Scalar(255, 0, 0));
			}
		}
	}
	
	public Rect bindRects(MatOfKeyPoint points) {
		MatOfPoint2f o = new MatOfPoint2f();
		MatOfPoint2f p = new MatOfPoint2f(points);
		Imgproc.approxPolyDP(new MatOfPoint2f(p.toArray()), o, 5, true);
		MatOfPoint m = new MatOfPoint(o.toArray());
		return Imgproc.boundingRect(m);
	}
	
	public Rect bindRects(MatOfPoint points) {
		MatOfPoint2f o = new MatOfPoint2f();
		Imgproc.approxPolyDP(new MatOfPoint2f(points.toArray()), o, 5, true);
		MatOfPoint m = new MatOfPoint(o.toArray());
		return Imgproc.boundingRect(m);
	}
	
	public Mat findHoughLines(Mat src) {
	    Mat dst = src.clone();
		Mat mYuv = new Mat();
	    Mat mRgba = new Mat();
	    //Mat thresholdImage = new Mat(getFrameHeight() + getFrameHeight() / 2, getFrameWidth(), CvType.CV_8UC1);
	    //mYuv.put(0, 0, data);
	    //Imgproc.cvtColor(mYuv, mRgba, Imgproc.COLOR_YUV420sp2RGB, 4);
	    //Imgproc.cvtColor(mRgba, dst, Imgproc.COLOR_RGB2GRAY, 4);
	    Imgproc.Canny(src, dst, 80, 100, 3, true);
	    Mat lines = dst.clone();
	    int threshold = 100;
	    int minLineSize = 40;
	    int lineGap = 10;

	    Imgproc.HoughLinesP(dst, lines, 1, Math.PI/180, threshold, minLineSize, lineGap);
	    
	    for (int x = 0; x < lines.cols(); x++) 
	    {
	          double[] vec = lines.get(0, x);
	          double x1 = vec[0], 
	                 y1 = vec[1],
	                 x2 = vec[2],
	                 y2 = vec[3];
	          Point start = new Point(x1, y1);
	          Point end = new Point(x2, y2);

	          Imgproc.line(src, start, end, new Scalar(0,255,0), 5);
	    }
	    return dst;
	}
	
}
