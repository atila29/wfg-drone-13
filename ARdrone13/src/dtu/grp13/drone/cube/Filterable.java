package dtu.grp13.drone.cube;

import org.opencv.core.Mat;
import org.opencv.core.Scalar;

public interface Filterable {
	String getName();
	void processColor(Mat src, Mat dst);
}
