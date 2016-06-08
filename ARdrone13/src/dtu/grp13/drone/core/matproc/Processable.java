package dtu.grp13.drone.core.matproc;

import org.opencv.core.Mat;

public interface Processable {
	Mat processMat(Mat a);
	public void render(Mat image);
}
