package dtu.grp13.drone.core.matproc;

import org.opencv.core.Mat;

import dtu.grp13.drone.core.matproc.procs.IMatProcess;

public class TestProcess extends AbstractProcess {

	@Override
	public Mat processMat(Mat a) {
		return a;
	}

	@Override
	public void changeProcess(IMatProcess proc) {
		// TODO Auto-generated method stub
		
	}

}
