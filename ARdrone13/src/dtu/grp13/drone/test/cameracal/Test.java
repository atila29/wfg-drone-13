package dtu.grp13.drone.test.cameracal;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.Video;
import org.opencv.videoio.VideoCapture;

//import com.sun.javafx.css.CalculatedValue;

import de.yadrone.base.ARDrone;
import de.yadrone.base.command.CommandManager;
import de.yadrone.base.command.VideoChannel;
import de.yadrone.base.video.ImageListener;
import dtu.grp13.drone.core.CommandThread;
import dtu.grp13.drone.core.ImageListenerThread;

/*
 * KUN TIL TEST
 * ------------
 * denne klasse er spaghetti kode
 * og skal kun benyttes til test.
 * 
 * 
 */

public class Test {

	private ImageListenerThread imgThread;
	private MyFrame frame;
	private Mat image;
	private ScheduledExecutorService timer;

	private ARDrone drone;

	// op_flow

	// System.loadLibrary("opencv_ffmpeg310_64");

	List<Point> cornersThis = new ArrayList<Point>();
	List<Point> cornersPrev = new ArrayList<Point>();
	Point pt = new Point(0, 0);
	Point pt1 = new Point(0, 0);
	Point pt2 = new Point(0, 0);
	List<Byte> byteStatus = new ArrayList<Byte>();
	int iGFFTMax = 50;
	int y;
	int x;
	Mat matOpFlowThis = new Mat();
	Mat matOpFlowPrev = new Mat();
	Mat mRgba = new Mat();
	MatOfPoint MOPcorners = new MatOfPoint();
	MatOfPoint2f mMOP2fptsPrev = new MatOfPoint2f();
	MatOfPoint2f mMOP2fptsSafe = new MatOfPoint2f();
	MatOfPoint2f mMOP2fptsThis = new MatOfPoint2f();
	MatOfFloat mMOFerr = new MatOfFloat();
	MatOfByte mMOBStatus = new MatOfByte();
	Scalar colorRed = new Scalar(255, 0, 0, 255);
	int blockSize = 8;
	boolean useHarrisDetector = false;
	double k = 0.0;
	int scale = 1;
	int delta = 0;
	int ddepth = CvType.CV_32FC1;
	int speed = 20;
	
	
	ImageListener listener = null;
	private volatile VectorAnalyzer va; // hvis der sker en fejl, er det evt. denne der ikke kan tilg�s fra andre tr�de!!!
	private CommandThread cmdThread;
	// op_flow END

	public Test() {

		//System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		va = new VectorAnalyzer("test");
		
		drone = new ARDrone();
		cmdThread = new CommandThread(drone);
		listener = new ImageListener() {

			@Override
			public void imageUpdated(BufferedImage arg0) {
				// TODO Auto-generated method stub
				
				image = bufferedImageToMat(arg0);
				frame.render(image);
			}
				
		};

		frame = new MyFrame();
		image = new Mat();
		timer = Executors.newSingleThreadScheduledExecutor();
		imgThread = new ImageListenerThread(listener);
	}

	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		Test test = new Test();
		test.drone.getCommandManager().setVideoChannel(VideoChannel.HORI);
		test.frame.setOnClickListenerForStart(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new Thread(() -> {
					
					test.drone.getVideoManager().addImageListener(new ImageListener() {
						
						@Override
						public void imageUpdated(BufferedImage arg0) {
							File outputfile = new File("cal_cam_drone" + System.currentTimeMillis() + ".jpg");
							try {
								ImageIO.write(arg0, "jpg", outputfile);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
						}
					});
				}) { 
				}.start();
				
				//test.cmdThread.hover(1000);
				
				//test.cmdThread.land();
			}
		});
		test.frame.setOnClickListenerForStop(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//test.cmdThread.land();
				test.drone.getCommandManager().landing();
				//test.imgThread.stop();
			}
		});

		test.drone.start();
		test.drone.getVideoManager().addImageListener(new ImageListener() {
			@Override
			public void imageUpdated(BufferedImage arg0) {
				test.imgThread.analyzeImage(arg0);
				
			}
		});

		// VideoCapture cap = new VideoCapture(0);

		test.frame.setVisible(true);
		
		
		
		//test.drone.getCommandManager().flatTrim().takeOff();
		//test.drone.getCommandManager().flatTrim().waitFor(12000);
		//test.drone.getCommandManager().hover().doFor(15000);
		//System.out.println("herfra");
		//test.va.setCollection("vigtig_1m");
		//test.drone.getCommandManager().forward(speed).doFor(990);
		//test.drone.getCommandManager().backward(speed).doFor(999);
		//test.va.stop();
		//test.va.writeVectors("vigtig_1m");
		//System.out.println("hertil");
		//test.va.start();
		//test.drone.getCommandManager().hover().doFor(500);
		//test.drone.getCommandManager().landing();
		
		// MyLoop loop = test.new MyLoop();
		// test.timer.scheduleAtFixedRate(loop, 0, 33, TimeUnit.MILLISECONDS);
		//test.imgThread.stop();
	}

	public static Mat bufferedImageToMat(BufferedImage bi) {
		Mat mat = new Mat(bi.getHeight(), bi.getWidth(), CvType.CV_8UC3);
		byte[] data = ((DataBufferByte) bi.getRaster().getDataBuffer())
				.getData();
		mat.put(0, 0, data);
		return mat;
	}


}
