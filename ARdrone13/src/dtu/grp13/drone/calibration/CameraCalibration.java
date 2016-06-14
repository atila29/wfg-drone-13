package dtu.grp13.drone.calibration;

import java.util.ArrayList;
import java.util.List;

import org.opencv.calib3d.Calib3d;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.MatOfPoint3f;
import org.opencv.core.Point3;
import org.opencv.core.Size;
import org.opencv.core.TermCriteria;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class CameraCalibration {

	public CameraCalibration() {

	}
	
	public static void main(String[] args) {
		System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
		int xsize = 6, ysize = 9;
		Mat savedImage = new Mat();
		
		List<Mat> imagePoint = new ArrayList<Mat>(), rvecs = new ArrayList<Mat>(), tvecs = new ArrayList<Mat>();
		for(int x = 0; x<xsize; x++) {
			for(int y = 0; y<ysize; y++) {
				MatOfPoint3f points = new MatOfPoint3f(new Point3(x,y,0));
				imagePoint.add(points);
			}
		}
		
		List<Mat> cornerList = new ArrayList<Mat>();
		for( int i = 1 ; i<=12; i++) {
			Mat image = Imgcodecs.imread("resources\\calibration\\cali"+i+".jpg");
			Mat grayImg = new Mat();
			Imgproc.cvtColor(image, grayImg, Imgproc.COLOR_BGRA2GRAY);
			
			MatOfPoint2f corners = new MatOfPoint2f();
			Size size = new Size(xsize, ysize);
			boolean found = Calib3d.findChessboardCorners(image, size, corners);
			if(found) {
		
				grayImg.copyTo(savedImage);
				Imgproc.cornerSubPix(grayImg, corners, new Size(11,11), new Size(-1,-1), new TermCriteria(TermCriteria.EPS|TermCriteria.MAX_ITER, 30, 0.1));
				cornerList.add(corners);
	
			}
			Calib3d.drawChessboardCorners(image, size, corners, found);
			Imgcodecs.imwrite("resources\\checkboard\\drawnimg"+i+".jpg", image);
			System.out.println(i + " done");
		}
		
		Mat cameraMatrix = Mat.eye(new Size(3,3), CvType.CV_64F);
		Mat distCoeffs =  new Mat();
		cameraMatrix.put(0, 0, 1);
		cameraMatrix.put(1, 1, 1);
		Calib3d.calibrateCamera(cornerList, imagePoint, savedImage.size(), cameraMatrix, distCoeffs, rvecs, tvecs);
		Mat imgIn = Imgcodecs.imread("resources\\pic9.jpg");
		
		
		Imgproc.undistort(imgIn, imgIn, cameraMatrix, distCoeffs);
		Imgcodecs.imwrite("resources\\drawming.jpg",imgIn);
	}
}