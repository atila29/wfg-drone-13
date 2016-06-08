package dtu.grp13.drone.core.matproc;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import dtu.grp13.drone.cube.CubeDetector;
import dtu.grp13.drone.cube.Filterable;

public class FrameProcess extends AbstractProcess {
	private Mat currentFrame;

	@Override
	public Mat processMat(Mat a) {
		currentFrame = a;
		// LOGIK
		Mat img = currentFrame.clone();
		Mat grey = new Mat();
		Imgproc.cvtColor(currentFrame, grey, Imgproc.COLOR_BGRA2GRAY); 
		Imgproc.GaussianBlur(grey, grey, new Size(3,3),0,0);
		Imgproc.Canny(grey, grey, 5, 50);
		CubeDetector c = new CubeDetector();

		Filterable filter = new Filterable() {
			@Override
			public void processGreen(Mat src, Mat dst) {
				Core.inRange(src, new Scalar(0, 50, 0), new Scalar(50, 255, 50), dst);
			}

			@Override
			public void processRed(Mat src, Mat dst) {
				Core.inRange(src, new Scalar(0, 0, 50), new Scalar(50, 50, 255), dst);
			}
		};
		
		c.findSpecificCubeColor(currentFrame, img, filter);

		// LOGIK END

		return img;
	}


}
