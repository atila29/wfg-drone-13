package dtu.grp13.drone.core.matproc.procs;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;

import dtu.grp13.drone.cube.CubeDetector;
import dtu.grp13.drone.cube.Filterable;
import dtu.grp13.drone.vector.Vector2;

public class CubeProc implements IMatProcess{
	private List<Filterable> filtre = new ArrayList<>();
	private Mat currentFrame;
	CubeDetector c;
	
	public CubeProc(){
		c = new CubeDetector();
		
		filtre.add(new Filterable(){
			@Override
			public String getName() {
				return "green";
			}

			@Override
			public void processColor(Mat src, Mat dst) {
				Core.inRange(src, new Scalar(0, 50, 0), new Scalar(50, 255, 50), dst);
			}
		});
		filtre.add(new Filterable(){
			@Override
			public String getName() {
				return "red";
			}

			@Override
			public void processColor(Mat src, Mat dst) {
				Core.inRange(src, new Scalar(0, 0, 50), new Scalar(40, 40, 255), dst);
			}
		});
	}

	@Override
	public Mat processMat(Mat a) {
		currentFrame = a;
		// LOGIK
		Mat img = currentFrame.clone();
		c.findSpecificCubeColor(currentFrame, img, filtre);
		return img;
	}

	
}
