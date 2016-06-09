package dtu.grp13.drone.core.matproc;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import dtu.grp13.drone.cube.CubeDetector;
import dtu.grp13.drone.cube.Filterable;

public class FrameProcess extends AbstractProcess {
	private Mat currentFrame;
	List<Filterable> filtre = new ArrayList<>();
	
	public FrameProcess(){
		super();
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
//		filtre.add(new Filterable(){
//			@Override
//			public String getName() {
//				return "green";
//			}
//
//			@Override
//			public void processColor(Mat src, Mat dst) {
//				Core.inRange(src, new Scalar(0, 255, 0), new Scalar(120, 255, 120), dst);
//			}
//		});

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
		/*Mat grey = new Mat();
		Imgproc.cvtColor(currentFrame, grey, Imgproc.COLOR_BGRA2GRAY); 
		Imgproc.GaussianBlur(grey, grey, new Size(3,3),0,0);
		Imgproc.Canny(grey, grey, 5, 50);*/
		CubeDetector c = new CubeDetector();

		c.getQr(currentFrame, img);

		// LOGIK END

		return img;
	}


}
