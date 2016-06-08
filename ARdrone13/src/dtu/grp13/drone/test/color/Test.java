package dtu.grp13.drone.test.color;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import de.yadrone.base.ARDrone;
import de.yadrone.base.command.VideoChannel;
import de.yadrone.base.video.ImageListener;
import dtu.grp13.drone.core.ImageListenerThread;

public class Test {
	private ARDrone drone;
	private ImageListenerThread imgThread;
	private ImageListener listener;
	private Mat image;
	private MyFrame frame;
	
	public Test(){
		drone = new ARDrone();
		listener = new ImageListener() {
			
			@Override
			public void imageUpdated(BufferedImage arg0) {
				image = bufferedImageToMat(arg0);
				frame.render(image);
			}
		};
		imgThread = new ImageListenerThread(listener);
	}

	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Test test = new Test();
		
//		VideoCapture cap = new VideoCapture(0);
//		if(!cap.isOpened()) {
//			System.exit(1);
//		}
		test.drone.getCommandManager().setVideoChannel(VideoChannel.HORI);
		test.frame = new MyFrame();
		test.frame.setVisible(true);
		test.drone.getVideoManager().addImageListener(new ImageListener() {
			@Override
			public void imageUpdated(BufferedImage arg0) {
				test.imgThread.analyzeImage(arg0);
			}
		});
		
		test.drone.start();
		
	}
	

	public static Mat bufferedImageToMat(BufferedImage bi) {
		Mat mat = new Mat(bi.getHeight(), bi.getWidth(), CvType.CV_8UC3);
		byte[] data = ((DataBufferByte) bi.getRaster().getDataBuffer())
				.getData();
		mat.put(0, 0, data);
		return mat;
	}

}
