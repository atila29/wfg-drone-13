package dtu.grp13.drone.test.drone;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import de.yadrone.base.ARDrone;
import de.yadrone.base.command.CommandManager;
import de.yadrone.base.command.VideoChannel;
import de.yadrone.base.video.ImageListener;

/*
 * 
 * 
 * 
 */


public class Test {
	
	private MyFrame frame;
	private Mat image;
	private ScheduledExecutorService timer;
	
	private ARDrone drone;

	public Test(){
		
		drone = new ARDrone();
		drone.getCommandManager().setVideoChannel(VideoChannel.HORI);

		drone.getVideoManager().addImageListener(new ImageListener() {
			
			@Override
			public void imageUpdated(BufferedImage arg0) {
				// TODO Auto-generated method stub
				image = bufferedImageToMat(arg0);
				if (image != null) {
					frame.render(image);
				} else {
					System.out.println("Camera error!");
					System.exit(1);
				}
			}
		});
		
		frame = new MyFrame();
		image = new Mat();
		timer = Executors.newSingleThreadScheduledExecutor();
	}
	

	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		Test test = new Test();
		
		test.drone.start();
		
		//VideoCapture cap = new VideoCapture(0);
		

		test.frame.setVisible(true);
		
		//MyLoop loop = test.new MyLoop();
		//test.timer.scheduleAtFixedRate(loop, 0, 33, TimeUnit.MILLISECONDS);
		
	}
	
	public static Mat bufferedImageToMat(BufferedImage bi) {
		  Mat mat = new Mat(bi.getHeight(), bi.getWidth(), CvType.CV_8UC3);
		  byte[] data = ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();
		  mat.put(0, 0, data);
		  return mat;
		}
	
	private class MyLoop implements Runnable {

		@Override
		public void run() {

						//cap.read(image);
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
