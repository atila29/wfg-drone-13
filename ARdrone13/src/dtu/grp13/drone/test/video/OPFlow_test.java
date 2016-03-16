package dtu.grp13.drone.test.video;

import java.util.List;

import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.Video;
import org.opencv.videoio.VideoCapture;


public class OPFlow_test {

	public static void main(String[] args) {
		
	    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		System.loadLibrary("opencv_ffmpeg310_64");
		
		List<Point> cornersThis = new ArrayList<Point>();
		List<Point> cornersPrev = new ArrayList<Point>();
		Point pt = new Point (0, 0);
		Point pt1 = new Point (0, 0);
		Point pt2 = new Point (0, 0);
		List<Byte> byteStatus = new ArrayList<Byte>();
		int iGFFTMax = 400;
		int y;
		int x;
		Mat matOpFlowThis = new Mat();
		Mat matOpFlowPrev = new Mat();
		Mat mRgba = new Mat();
		MatOfPoint MOPcorners = new MatOfPoint();
		MatOfPoint2f mMOP2fptsPrev = new MatOfPoint2f();
		MatOfPoint2f mMOP2fptsSafe = new MatOfPoint2f();
		MatOfPoint2f mMOP2fptsThis = new MatOfPoint2f();
	    MatOfFloat mMOFerr = new MatOfFloat();
	    MatOfByte mMOBStatus = new MatOfByte();
	    Scalar colorRed = new Scalar(255, 0, 0, 255);
	    int blockSize = 8;
	    boolean useHarrisDetector = false;
	    double k = 0.0;
	    int scale = 1;
	    int delta = 0;
	    int ddepth = CvType.CV_32FC1;
		
		
		VideoCapture cap = new VideoCapture("resources/IMG_0208.m4v");
		
		if(!cap.isOpened()) {
			System.exit(1);
		}
		
		MyFrame frame = new MyFrame();
		frame.setVisible(true);

		// Main loop
		while (true) {
			// Read current camera frame into matrix
			try {
				Thread.sleep(33);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// Render frame if the camera is still acquiring images
			
			if (cap.grab()) {
				
				if (mMOP2fptsPrev.rows() == 0) {
					cap.read(mRgba);
					//mRgba = new Mat(matOpFlowThis.height(), matOpFlowThis.height(), CvType.CV_8UC4);
					//mRgba = new Mat(img1.height(), img1.width(), CvType.CV_8UC4);
					Imgproc.cvtColor(mRgba, matOpFlowThis, Imgproc.COLOR_RGBA2GRAY);
					Imgproc.GaussianBlur(matOpFlowThis, matOpFlowThis, new Size(3,3), 0);
					//Imgproc.Canny(matOpFlowThis, matOpFlowThis, 1, 50, 3, true);
					matOpFlowThis.copyTo(matOpFlowPrev);
					Imgproc.goodFeaturesToTrack(matOpFlowPrev, MOPcorners, iGFFTMax, 0.50, 5);
					mMOP2fptsPrev.fromArray(MOPcorners.toArray());
					mMOP2fptsPrev.copyTo(mMOP2fptsSafe);
					
				}
					
					matOpFlowThis.copyTo(matOpFlowPrev);
					cap.read(mRgba);
					Imgproc.cvtColor(mRgba, matOpFlowThis, Imgproc.COLOR_RGBA2GRAY);
					Imgproc.GaussianBlur(matOpFlowThis, matOpFlowThis, new Size(3,3), 0);
					//Imgproc.Canny(matOpFlowThis, matOpFlowThis, 10, 50, 3, true);
					Imgproc.goodFeaturesToTrack(matOpFlowThis, MOPcorners, iGFFTMax, 0.50, 5);
					mMOP2fptsThis.fromArray(MOPcorners.toArray());
					mMOP2fptsSafe.copyTo(mMOP2fptsPrev);
					mMOP2fptsThis.copyTo(mMOP2fptsSafe);
				
				Video.calcOpticalFlowPyrLK(matOpFlowPrev, matOpFlowThis, mMOP2fptsPrev, mMOP2fptsThis, mMOBStatus, mMOFerr);
				//Video.calcOpticalFlowPyrLK(img1, img2, mMOP2fptsPrev, mMOP2fptsThis, mMOBStatus, mMOFerr);
		        cornersPrev = mMOP2fptsPrev.toList();
		        cornersThis = mMOP2fptsThis.toList();
		        byteStatus = mMOBStatus.toList();
		        
		        y = byteStatus.size() - 1;
		        for (x = 0; x < y; x++) {
		           	if (byteStatus.get(x) == 1) {
		                pt = cornersThis.get(x);
		                pt2 = cornersPrev.get(x);
		                System.out.println(pt);
		                System.out.println(pt2);

		                Imgproc.arrowedLine(matOpFlowThis, pt, pt2, colorRed);
		                
		            }
		        }
		        frame.render(matOpFlowThis);

			} else {
				System.out.println("Camera error!");
				System.exit(1);
				cap.release();
			}			
		}	
	}

}


