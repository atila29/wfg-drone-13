package dtu.grp13.drone.test.video;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;


public class Test {

	/*
	 * Denne test er lavet efter:
	 * http://michaelscarlett.blog.com/2015/01/25/opencv-java-webcam-example/
	 * 
	 * 
	 */

	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		System.loadLibrary("opencv_ffmpeg310_64");
		
		// "resources/video1.avi"
		VideoCapture cap = new VideoCapture(0);
		
		if(!cap.isOpened()) {
			System.exit(1);
		}
		
		Mat image = new Mat();
		
		MyFrame frame = new MyFrame();
		frame.setVisible(true);
		

		// Main loop
		while (true) {
			// Read current camera frame into matrix
			try {
				Thread.sleep(25);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// Render frame if the camera is still acquiring images
			if (cap.read(image)) {
				frame.render(image);
			} else {
				System.out.println("Camera error!");
				System.exit(1);
				cap.release();
			}			
		}	
	}
}
