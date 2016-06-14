package dtu.grp13.drone.core;

import java.io.IOException;

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
import dtu.grp13.drone.position.PositionFrame;
import dtu.grp13.drone.vector.Vector2;

public class Main {

	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		ModeController mc = new ModeController(new FrameProcess());
		PositionFrame xFrame = new PositionFrame();
		xFrame.setDronePosition(new Vector2(0,108));
//		mc.useWebcam();
//		mc.useDrone(new IDroneSetup() {
//			
//			@Override
//			public void setup(ARDrone drone) {
//				drone.reset();
//				drone.getCommandManager().setWifiMode(WifiMode.STATION);
//				drone.getCommandManager().setVideoBitrateControl(VideoBitRateMode.DISABLED);
//				drone.getCommandManager().setVideoBitrate(4000);
//				drone.getCommandManager().setVideoCodecFps(15);
//				drone.getCommandManager().setVideoCodec(VideoCodec.H264_720P);
//				//drone.getCommandManager().setVideoChannel(VideoChannel.NEXT);
//				drone.getCommandManager().setVideoChannel(VideoChannel.HORI);
//			}
//		});
		try {
			mc.useStaticImage("./resources/pic9.jpg");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
