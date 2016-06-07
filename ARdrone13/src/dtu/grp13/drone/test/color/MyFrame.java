package dtu.grp13.drone.test.color;

import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.List;

import javax.swing.JFrame;

import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.BackgroundSubtractorMOG2;

import dtu.grp13.drone.cube.CubeDetector;

public class MyFrame {

	private final JFrame frame;
	private final MyPanel panel;

	// attributes needed for processeing images
	private Mat currentFrame;
	private Mat lastFrame;
	private BackgroundSubtractorMOG2 bsMog;
	int treshold = 60; // vigtig kommentar

	public MyFrame() {
		// JFrame which holds JPanel
		frame = new JFrame();
		frame.getContentPane().setLayout(new FlowLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("wc - 0.1 - 29");
		// JPanel which is used for drawing image
		panel = new MyPanel();
		frame.getContentPane().add(panel);
	}

	public void setVisible(boolean visible) {
		frame.setVisible(visible);
	}

	public void render(Mat image) {
		Mat man = processMat(image);
		Image i = toBufferedImage(man);
		panel.setImage(i);
		panel.repaint();
		frame.pack();
	}

	public Mat processMat(Mat a) {
		// lav billede ændring og analyse
		currentFrame = a;
		// LOGIK
		Mat img = currentFrame.clone();
		Mat grey = new Mat();
		Imgproc.cvtColor(currentFrame, grey, Imgproc.COLOR_BGRA2GRAY); 
		Imgproc.GaussianBlur(grey, grey, new Size(3,3),0,0);
		Imgproc.Canny(grey, grey, 5, 50);
		CubeDetector c = new CubeDetector();
//		List<Rect> rects = c.findRects(grey, img);
//		c.findCubes(currentFrame, img, rects);
//		c.isolateinterestingRects(rects, 1.41);
		//c.findCubeColor(currentFrame, img);
		c.findSpecificCubeColor(currentFrame, img, new Scalar(20, 20, 20), new Scalar(50, 255, 50));
//		Mat hsv_cf = new Mat();
//		Mat threshold = new Mat();
//		Imgproc.GaussianBlur(currentFrame, currentFrame, new Size(3, 3), 0);
//		Imgproc.cvtColor(currentFrame, hsv_cf, Imgproc.COLOR_BGR2HSV);
//		Core.inRange(hsv_cf, new Scalar(160, 100, 100),
//				new Scalar(179, 255, 255), threshold);

		// LOGIK END

		return img;
	}

	public static Image toBufferedImage(Mat m) {
		// Code from
		// http://stackoverflow.com/questions/15670933/opencv-java-load-image-to-gui

		// Check if image is grayscale or color
		int type = BufferedImage.TYPE_BYTE_GRAY;
		if (m.channels() > 1) {
			type = BufferedImage.TYPE_3BYTE_BGR;
		}

		// Transfer bytes from Mat to BufferedImage
		int bufferSize = m.channels() * m.cols() * m.rows();
		byte[] b = new byte[bufferSize];
		m.get(0, 0, b); // get all the pixels
		BufferedImage image = new BufferedImage(m.cols(), m.rows(), type);
		final byte[] targetPixels = ((DataBufferByte) image.getRaster()
				.getDataBuffer()).getData();
		System.arraycopy(b, 0, targetPixels, 0, b.length);
		return image;
	}
}
