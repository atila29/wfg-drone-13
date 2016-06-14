package dtu.grp13.drone.core.matproc;

import org.opencv.core.Mat;

import dtu.grp13.drone.core.matproc.procs.IMatProcess;

public interface Processable {
	Mat processMat(Mat a);
	public void render(Mat image);
	void changeProcess(IMatProcess proc);
}
