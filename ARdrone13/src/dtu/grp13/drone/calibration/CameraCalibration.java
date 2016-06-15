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
		MatOfPoint3f obj = new MatOfPoint3f();
		MatOfPoint2f imageCorners = new MatOfPoint2f();
		int numCornersHor = 9;
		int numCornersVer  = 6;
		int numSquares = numCornersHor * numCornersVer;
		
		Mat savedImage = new Mat();
		
		List<Mat> rvecs = new ArrayList<Mat>();
		List<Mat> tvecs = new ArrayList<Mat>();
		List<Mat> imagePoints = new ArrayList<>();
		List<Mat> objectPoints = new ArrayList<>();
		
		
		for(int j = 0; j < numSquares; j++) {
			obj.push_back(new MatOfPoint3f(new Point3(j / numCornersHor, j % numCornersVer, 0.0f)));
			objectPoints.add(obj);
		}
		
		for( int i = 1 ; i<=12; i++) {
			Mat image = Imgcodecs.imread("resources\\calibration\\cali"+i+".jpg");
			Mat grayImage = new Mat();
			Imgproc.cvtColor(image, grayImage, Imgproc.COLOR_BGRA2GRAY);
			
			Size boardsize = new Size(numCornersHor, numCornersVer );
			boolean found = Calib3d.findChessboardCorners(grayImage, boardsize, imageCorners);
			if(found) {
				
				
				TermCriteria term = new TermCriteria(TermCriteria.EPS | TermCriteria.MAX_ITER, 30, 0.1);
				Imgproc.cornerSubPix(grayImage, imageCorners, new Size(11,11), new Size(-1,-1), term);
				grayImage.copyTo(savedImage);
				
				imagePoints.add(imageCorners);
				
	
			}
			Calib3d.drawChessboardCorners(image, boardsize, imageCorners, found);
			Imgcodecs.imwrite("resources\\checkboard\\drawnimg"+i+".jpg", image);
			System.out.println(i + " done");
		}
		
		Mat cameraMatrix = Mat.eye(new Size(3,3), CvType.CV_64F);
		Mat distCoeffs =  new Mat();
		cameraMatrix.put(0, 0, 1);
		cameraMatrix.put(1, 1, 1);
		Calib3d.calibrateCamera(objectPoints, imagePoints, savedImage.size(), cameraMatrix, distCoeffs, rvecs, tvecs);
		Mat imgIn = Imgcodecs.imread("resources\\pic9.jpg");
		
		
		Imgproc.undistort(imgIn, imgIn, cameraMatrix, distCoeffs);
		Imgcodecs.imwrite("resources\\drawming.jpg",imgIn);
	}
}