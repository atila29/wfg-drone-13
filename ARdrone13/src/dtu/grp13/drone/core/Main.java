package dtu.grp13.drone.core;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.opencv.core.Core;
import org.opencv.core.Mat;

import de.yadrone.base.ARDrone;
import de.yadrone.base.command.VideoChannel;
import de.yadrone.base.command.VideoCodec;
import dtu.grp13.drone.core.matproc.FrameProcess;
import dtu.grp13.drone.core.matproc.IDroneSetup;
import dtu.grp13.drone.core.matproc.ModeController;
import dtu.grp13.drone.core.matproc.TestProcess;
import dtu.grp13.drone.core.matproc.procs.CircleProc;
import dtu.grp13.drone.core.matproc.procs.CubeProc;
import dtu.grp13.drone.core.matproc.procs.IMatProcess;
import dtu.grp13.drone.core.matproc.procs.QrProc;
import dtu.grp13.drone.gui.ControlFrame;
import dtu.grp13.drone.gui.PositionFrame;
import dtu.grp13.drone.util.WFGUtilities;
import dtu.grp13.drone.vector.Vector2;

public class Main {
	

	public static void main(String[] args) throws IOException {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		WFGUtilities.setupLog();
		
		ProgramManager programManager = new ProgramManager();
		PositionSystem positionSystem = new PositionSystem();
		FrameProcess proc = new FrameProcess(new QrProc(programManager, positionSystem));
		ModeController mc = new ModeController(proc);
		PositionFrame xFrame = new PositionFrame();
		xFrame.drawWallMarks(positionSystem.getWallmarks());
//		xFrame.setDronePosition(new Vector2(926,324));
//		mc.useWebcam();
		ARDrone drone = mc.useDrone(new IDroneSetup() {
			@Override
			public void setup(ARDrone drone) {
				//drone.reset();
				drone.getCommandManager().setVideoBitrate(4000);
				//drone.getCommandManager().setVideoCodecFps(15);
				drone.getCommandManager().setVideoCodec(VideoCodec.H264_720P);
				//drone.getCommandManager().setVideoChannel(VideoChannel.NEXT);
				drone.getCommandManager().setVideoChannel(VideoChannel.HORI);
			}
		});
		
		ICommandThread cmd = new CommandThread(drone);
		programManager.setProc(proc);
		programManager.setCmd(cmd);
		programManager.setxFrame(xFrame);
		programManager.setPosSystem(positionSystem);
		ControlFrame cf = new ControlFrame(programManager, drone.getNavDataManager());
//		try {
//			mc.useStaticImage("./resources/squares/cap1.jpg");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		

	}

}
