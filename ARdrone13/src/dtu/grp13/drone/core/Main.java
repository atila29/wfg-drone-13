package dtu.grp13.drone.core;

import org.opencv.core.Core;

import de.yadrone.base.ARDrone;
import de.yadrone.base.command.VideoBitRateMode;
import de.yadrone.base.command.VideoChannel;
import de.yadrone.base.command.VideoCodec;
import de.yadrone.base.command.WifiMode;
import dtu.grp13.drone.core.matproc.FrameProcess;
import dtu.grp13.drone.core.matproc.IDroneSetup;
import dtu.grp13.drone.core.matproc.ModeController;
import dtu.grp13.drone.core.matproc.TestProcess;

public class Main {

	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		ModeController mc = new ModeController(new FrameProcess());
		//mc.useWebcam();
		mc.useDrone(new IDroneSetup() {
			
			@Override
			public void setup(ARDrone drone) {
				drone.getCommandManager().setWifiMode(WifiMode.STATION);
				drone.getCommandManager().setVideoBitrateControl(VideoBitRateMode.DISABLED);
				drone.getCommandManager().setVideoBitrate(4000);
				drone.getCommandManager().setVideoCodecFps(15);
				drone.getCommandManager().setVideoCodec(VideoCodec.H264_720P);
				drone.getCommandManager().setVideoChannel(VideoChannel.NEXT);
			}
		});
	}

}
