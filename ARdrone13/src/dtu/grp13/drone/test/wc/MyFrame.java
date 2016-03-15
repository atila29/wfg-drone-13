package dtu.grp13.drone.test.wc;

import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.beans.FeatureDescriptor;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.opencv.core.Core;
import org.opencv.core.KeyPoint;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.BackgroundSubtractorMOG2;

import com.sun.prism.Surface;

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
		
		Mat prosFrame = new Mat();

		if(lastFrame == null){
			lastFrame = a;
			return lastFrame;
		}
		
		currentFrame = a;
		Imgproc.GaussianBlur(currentFrame, currentFrame, new Size(5, 5), 0);
		Imgproc.GaussianBlur(lastFrame, lastFrame, new Size(5, 5), 0);
		
		// Greyscale
		
		Imgproc.cvtColor(currentFrame, currentFrame, Imgproc.COLOR_RGB2GRAY);
		
		
		// simpel point detection - evt. med extraction og opticalflow
		
		FeatureDetector fd = FeatureDetector.create(FeatureDetector.GRID_HARRIS);
		
		MatOfKeyPoint currentKeyPoints = new MatOfKeyPoint();
		MatOfKeyPoint lastKeyPoints = new MatOfKeyPoint();
		
		fd.detect(currentFrame, currentKeyPoints);
		fd.detect(lastFrame, lastKeyPoints);
		
		Features2d.drawKeypoints(currentFrame, currentKeyPoints, prosFrame);
		
		//for(KeyPoint kp : currentKeyPoints.)
		
		//simpleMotionDetection(prosFrame);
		
		
		lastFrame = currentFrame.clone();
		return prosFrame;
	}

	private void simpleMotionDetection(Mat prosFrame) {
		Core.subtract(currentFrame, lastFrame, prosFrame);
		
		Imgproc.cvtColor(prosFrame, prosFrame, Imgproc.COLOR_RGB2GRAY);
		
		Imgproc.threshold(prosFrame, prosFrame, treshold, 255,Imgproc.THRESH_BINARY);
		
		ArrayList<Rect> rects = detect_contours(currentFrame, prosFrame);
		
		for(Rect r : rects) {
			Imgproc.rectangle(currentFrame, r.br(), r.tl(), new Scalar(0, 255, 0));
		}
		
		lastFrame = currentFrame.clone();
	}
	
	public ArrayList<Rect> detect_contours(Mat frame, Mat out) {
		Mat v = new Mat();
		Mat y = out.clone();
		
		List<MatOfPoint> c = new ArrayList<>();
		Imgproc.findContours(y, c, v, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
		
		double maxArea = 100;
		int maxArea_idx;
		Rect r;
		ArrayList<Rect> r_array = new ArrayList<Rect>();
		
		for(int i = 0; i < c.size(); i++){
			Mat con = c.get(i);
			double con_area = Imgproc.contourArea(con);
			
			if(con_area > maxArea) {
				maxArea_idx = i;
				r = Imgproc.boundingRect(c.get(maxArea_idx));
				r_array.add(r);
				Imgproc.drawContours(frame, c, maxArea_idx, new Scalar(0,0,255));
			}
		}
		v.release();
		return r_array;
		
		
	}

	public static Image toBufferedImage(Mat m){
              // Code from http://stackoverflow.com/questions/15670933/opencv-java-load-image-to-gui

              // Check if image is grayscale or color
	      int type = BufferedImage.TYPE_BYTE_GRAY;
	      if ( m.channels() > 1 ) {
	          type = BufferedImage.TYPE_3BYTE_BGR;
	      }

              // Transfer bytes from Mat to BufferedImage
	      int bufferSize = m.channels()*m.cols()*m.rows();
	      byte [] b = new byte[bufferSize];
	      m.get(0,0,b); // get all the pixels
	      BufferedImage image = new BufferedImage(m.cols(), m.rows(), type);
	      final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
	      System.arraycopy(b, 0, targetPixels, 0, b.length);
	      return image;
	  }
}
