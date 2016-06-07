package dtu.grp13.drone.cube;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

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
				for(int x = 0 ; x < c.cols();x+=5) {
					for (int y = 0; y < c.rows();y+=5) {
						double[] point = c.get(y, x);
						// RGB[] 0 = red, 1 = green, 2 = blue 
						if(point[0] > 60 && point[1] < 50 && point[2] <50)
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
	
	public Rect bindRects(MatOfPoint points) {
		MatOfPoint2f o = new MatOfPoint2f();
		Imgproc.approxPolyDP(new MatOfPoint2f(points.toArray()), o, 5, true);
		MatOfPoint m = new MatOfPoint(o.toArray());
		return Imgproc.boundingRect(m);
	}
	
}
