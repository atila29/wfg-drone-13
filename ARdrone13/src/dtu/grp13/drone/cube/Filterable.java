package dtu.grp13.drone.cube;

import org.opencv.core.Mat;
import org.opencv.core.Scalar;

public interface Filterable {
	void processGreen(Mat src, Mat dst);
	void processRed(Mat src, Mat dst);
}
