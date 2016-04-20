package dtu.grp13.drone.test.gui;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import javax.swing.JFrame;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

import de.yadrone.base.ARDrone;
import de.yadrone.base.command.VideoChannel;
import de.yadrone.base.video.ImageListener;

public class Main {
	
	private ARDrone drone;
	ImageListener listener = null;
	
	public Main(){
	
		
	}
	
	public static Mat bufferedImageToMat(BufferedImage bi) {
		Mat mat = new Mat(bi.getHeight(), bi.getWidth(), CvType.CV_8UC3);
		byte[] data = ((DataBufferByte) bi.getRaster().getDataBuffer())
				.getData();
		mat.put(0, 0, data);
		return mat;
	}
	
	public static void main(String[] args) {
		
		

		MainFrame mainFrame = new MainFrame("Gruppe 13 - Drone Control");
		mainFrame.setSize(1000, 600);
		mainFrame.setResizable(false);
		
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setVisible(true);
		mainFrame.render(new Mat());
		
		ARDrone drone = new ARDrone();
		drone.getCommandManager().setVideoChannel(VideoChannel.VERT);
		
		ImageListener listener = new ImageListener() {

			@Override
			public void imageUpdated(BufferedImage arg0) {
				mainFrame.render(Main.bufferedImageToMat(arg0));
			} 
				
		};
		drone.start();

	}

}
