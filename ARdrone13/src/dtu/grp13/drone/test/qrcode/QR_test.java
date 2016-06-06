package dtu.grp13.drone.test.qrcode;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import de.yadrone.base.ARDrone;
import de.yadrone.base.command.VideoChannel;
import de.yadrone.base.command.VideoCodec;
import de.yadrone.base.video.ImageListener;

public class QR_test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		System.loadLibrary("opencv_ffmpeg310_64");

		ARDrone drone = new ARDrone();
		
		
		Mat image = new Mat();
		
		MyFrame frame = new MyFrame();
		frame.setVisible(true);
		drone.getCommandManager().setVideoCodec(VideoCodec.H264_720P);
		drone.getCommandManager().setVideoChannel(VideoChannel.HORI);
		
		
		drone.getVideoManager().addImageListener(new ImageListener() {
			
			@Override
			public void imageUpdated(BufferedImage arg0) {
				frame.render(bufferedImageToMat(arg0));
				
			}
		});
		drone.start();
		
	}
	
	public static Mat bufferedImageToMat(BufferedImage bi) {
		Mat mat = new Mat(bi.getHeight(), bi.getWidth(), CvType.CV_8UC3);
		byte[] data = ((DataBufferByte) bi.getRaster().getDataBuffer())
				.getData();
		mat.put(0, 0, data);
		return mat;
	}

}
