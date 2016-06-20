package dtu.grp13.drone.core.matproc.procs;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import dtu.grp13.drone.core.ProgramManager;
import dtu.grp13.drone.cube.CubeDetector;
import dtu.grp13.drone.cube.Filterable;
import dtu.grp13.drone.vector.Vector2;

public class CubeProc implements IMatProcess {
	private List<Filterable> filtre = new ArrayList<>();
	private Mat currentFrame;
	private CubeDetector c;
	private ProgramManager pm;

	public CubeProc(ProgramManager pm) {
		c = new CubeDetector();
		this.pm = pm;

		filtre.add(new Filterable() {
			@Override
			public String getName() {
				return "green";
			}

			@Override
			public void processColor(Mat src, Mat dst) {
				Mat red1Img = new Mat();
				Mat red2Img = new Mat();
				src.copyTo(red1Img);
				src.copyTo(red2Img);
				Mat hsvImg = new Mat();
				Imgproc.cvtColor(src, hsvImg, Imgproc.COLOR_BGR2HSV);
				Core.inRange(hsvImg, new Scalar(40, 100, 10), new Scalar(80, 255, 255), dst);

			}
		});
		filtre.add(new Filterable() {
			@Override
			public String getName() {
				return "red";
			}

			@Override
			public void processColor(Mat src, Mat dst) {
				Mat red1Img = new Mat();
				Mat red2Img = new Mat();
				src.copyTo(red1Img);
				src.copyTo(red2Img);
				Mat hsvImg = new Mat();
				Imgproc.cvtColor(src, hsvImg, Imgproc.COLOR_BGR2HSV);
				Core.inRange(hsvImg, new Scalar(0, 100, 100), new Scalar(30, 255, 255), red1Img);
				Core.inRange(hsvImg, new Scalar(160, 100, 100), new Scalar(255, 255, 255), red2Img);
				Core.addWeighted(red1Img, 1.0, red2Img, 1.0, 0.0, dst);

			}
		});
	}

	@Override
	public Mat processMat(Mat a) {
		currentFrame = a;
		// LOGIK
		Mat img = currentFrame.clone();
		c.findSpecificCubeColor(currentFrame, img, filtre, new ICubeFoundAsync() {

			@Override
			public void retColor(String color) {
				pm.cubeFound(color);
			}
		});
		// c.searchForCubeColor(currentFrame, img, new ICubeFoundAsync() {
		//
		// @Override
		// public void retColor(String color) {
		// // TODO Auto-generated method stub
		//
		// }
		// });
		return img;
	}

}
