package dtu.grp13.drone.core;

import org.opencv.core.Core;

import de.yadrone.base.ARDrone;
import de.yadrone.base.command.VideoCodec;
import dtu.grp13.drone.core.matproc.FrameProcess;
import dtu.grp13.drone.core.matproc.IDroneSetup;
import dtu.grp13.drone.core.matproc.ModeController;

public class Main {

	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		ModeController mc = new ModeController(new FrameProcess());
		//mc.useWebcam();
		mc.useDrone(new IDroneSetup() {
			
			@Override
			public void setup(ARDrone drone) {
				drone.getCommandManager().setVideoBitrate(4000);
				drone.getCommandManager().setVideoCodecFps(15);
				drone.getCommandManager().setVideoCodec(VideoCodec.H264_720P);
			}
		});
	}

}
