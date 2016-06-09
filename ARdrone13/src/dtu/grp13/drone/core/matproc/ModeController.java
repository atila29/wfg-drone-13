package dtu.grp13.drone.core.matproc;

import java.awt.image.BufferedImage;

import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import de.yadrone.base.ARDrone;
import de.yadrone.base.video.ImageListener;
import dtu.grp13.drone.core.ImageListenerThread;
import dtu.grp13.drone.util.WFGUtilities;

public class ModeController {
	private ARDrone drone;
	private ImageListenerThread imgThread;
	private ImageListener listener;
	private Mat image;
	private Processable frame;
	
	public ModeController(Processable process) {
		frame = process;
	}
	
	
	
	public ARDrone useDrone(IDroneSetup setup){
		initDrone();
		setup.setup(drone);
		return drone;
	}
	
	private void initDrone(){
		drone = new ARDrone();
		listener = new ImageListener() {
			
			@Override
			public void imageUpdated(BufferedImage arg0) {
				image = WFGUtilities.bufferedImageToMat(arg0);
				frame.render(image);
			}
		};
		imgThread = new ImageListenerThread(listener);
		drone.getVideoManager().addImageListener(new ImageListener() {
			@Override
			public void imageUpdated(BufferedImage arg0) {
				imgThread.analyzeImage(arg0);
			}
		});
	}
	
	public void useWebcam(){
		VideoCapture cap = new VideoCapture(0);
		if(!cap.isOpened()) {
			System.exit(1);
		}
		
		Mat image = new Mat();
		
		//frame.setVisible(true);
		

		// Main loop
		while (true) {
			// Read current camera frame into matrix
			try {
				Thread.sleep(33);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			cap.read(image);
			// Render frame if the camera is still acquiring images
			if (image != null) {
				frame.render(image);
			} else {
				System.out.println("Camera error!");
				System.exit(1);
			}
		}
	}
}
