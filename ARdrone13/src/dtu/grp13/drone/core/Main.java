package dtu.grp13.drone.core;

import org.opencv.core.Core;

import dtu.grp13.drone.core.matproc.FrameProcess;
import dtu.grp13.drone.core.matproc.ModeController;

public class Main {

	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		ModeController mc = new ModeController(new FrameProcess());
		mc.useWebcam();
	}

}
