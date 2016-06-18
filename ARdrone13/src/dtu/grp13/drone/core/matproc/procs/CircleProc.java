package dtu.grp13.drone.core.matproc.procs;

import org.opencv.core.Mat;

import drone.grp13.drone.circle.CircleDetector;
import dtu.grp13.drone.core.ProgramManager;

public class CircleProc implements IMatProcess {
	private Mat currentFrame;
	private CircleDetector c;
	private ProgramManager pm;
	
	public CircleProc(ProgramManager pm) {
		c = new CircleDetector();
		this.pm = pm;
	}

	@Override
	public Mat processMat(Mat a) {
		currentFrame = a;
		// LOGIK
		Mat img = currentFrame.clone();
		c.findCicles(currentFrame, img);
		
		return img;
	}

}
