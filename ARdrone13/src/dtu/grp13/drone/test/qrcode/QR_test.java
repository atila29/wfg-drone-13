package dtu.grp13.drone.test.qrcode;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

public class QR_test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		System.loadLibrary("opencv_ffmpeg310_64");

		
		
		VideoCapture cap = new VideoCapture("resources/IMG_0210.m4v");
		
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
				Thread.sleep(33);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// Render frame if the camera is still acquiring images
			;
			// Render frame if the camera is still acquiring images
			if (cap.read(image)) {
				frame.render(image);
			} else {
				System.out.println("Camera error!");
				cap.release();
				System.exit(1);
				
			}			
		}	
	}

}
