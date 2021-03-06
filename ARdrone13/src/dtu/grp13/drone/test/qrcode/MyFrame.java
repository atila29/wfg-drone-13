package dtu.grp13.drone.test.qrcode;

import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;

import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

import dtu.grp13.drone.core.PositionSystem;
import dtu.grp13.drone.cube.CubeDetector;
import dtu.grp13.drone.vector.Vector2;

public class MyFrame {

	private final JFrame frame;
	private final MyPanel panel;
	private double horizontalRadians = 1.30899694;
	private double widthRes = 1280;
	private List<Double> betaList = new ArrayList<Double>();
	private double distvRadian;
	private double disthRadian;
	private double radius1;
	private double radius2;
	private Vector2 center1;
	private Vector2 center2;
	private Vector2 intersection;
	private PositionSystem positionSystem;
	private List<String> sortedResult = new ArrayList<String>();
	private CubeDetector cubeDetector;

	private double beta;
	private double b = (widthRes / 2) / (Math.tan(horizontalRadians / 2));
	private double t;

	public MyFrame() {
		// JFrame which holds JPanel
		cubeDetector = new CubeDetector();
		frame = new JFrame();
		frame.getContentPane().setLayout(new FlowLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("wc - 0.1 - 29");
		// JPanel which is used for drawing image
		panel = new MyPanel();
		frame.getContentPane().add(panel);
		try {
			positionSystem = new PositionSystem();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setVisible(boolean visible) {
		frame.setVisible(visible);
	}

	public void render(Mat image) {
		Mat man = editFrame(image);
		Image i = toBufferedImage(man);
		panel.setImage(i);
		panel.repaint();
		frame.pack();
	}

	public Mat editFrame(Mat src) {
		Mat dst = src.clone();
		//Imgproc.resize(a, dst, new Size(1480, 720));
		Image i = toBufferedImage(src);
		LuminanceSource source = new BufferedImageLuminanceSource((BufferedImage) i);
		BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
		QRCodeReader reader = new QRCodeReader();
		Mat grey = new Mat();
		Imgproc.cvtColor(src, grey, Imgproc.COLOR_BGR2GRAY);
		//dst = cubeDetector.findHoughLines(src);
//		List<Rect> rectList = cubeDetector.findRects(grey, dst);
//		cubeDetector.isolateinterestingRects(rectList, 1.41);

		
		try {
			Map<DecodeHintType,Object> hintsMap = new EnumMap<DecodeHintType, Object>(DecodeHintType.class);
            hintsMap.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
			Result scanResult = reader.decode(bitmap);
			
						System.out.println(scanResult.getText());
			
			if (scanResult.getText().equals("")) {
				reader.reset();
				return dst;
			}
//			sortResults(scanResult, 0, scanResult.length-1);
//
//			for (int index = 0; index < scanResult.length; index++) {
//
//				if (scanResult[index].getResultPoints()[0].getX() > 640) {
//					t = scanResult[index].getResultPoints()[0].getX() - 640;
//				} else {
//					t = 640 - scanResult[index].getResultPoints()[0].getX();
//				}
//
//				beta = Math.atan(t / b);
//				betaList.add(beta);
//
//			}
//			if (scanResult[1].getResultPoints()[0].getX() > 640) {
//				disthRadian = betaList.get(2) - betaList.get(1);
//				if (scanResult[0].getResultPoints()[0].getX() > 640) {
//					distvRadian = betaList.get(2) - betaList.get(0);
//				} else {
//					distvRadian = betaList.get(2) + betaList.get(0);
//				}
//
//			} else if (scanResult[1].getResultPoints()[0].getX() < 640) {
//				distvRadian = betaList.get(0) - betaList.get(1);
//				if (scanResult[2].getResultPoints()[0].getX() < 640) {
//					disthRadian = betaList.get(1) - betaList.get(2);
//				} else {
//					disthRadian = betaList.get(1) + betaList.get(2);
//				}
//
//				Vector2 p1 = positionSystem.getVec(scanResult[0].getText());
//				Vector2 p2 = positionSystem.getVec(scanResult[1].getText());
//				Vector2 p3 = positionSystem.getVec(scanResult[2].getText());
//
//				double afstand1 = Math.sqrt(Math.pow(p1.getX() - p2.getX(), 2) + Math.pow(p1.getY() - p2.getY(), 2));
//				double afstand2 = Math.sqrt(Math.pow(p2.getX() - p3.getX(), 2) + Math.pow(p2.getY() - p3.getY(), 2));
//
//				radius1 = calcRadius(afstand1, distvRadian);
//				radius2 = calcRadius(afstand2, disthRadian);
//
//				center1 = calcCenter(p1, p2, afstand1, distvRadian);
//				center2 = calcCenter(p2, p3, afstand2, disthRadian);
//				System.out.println("C1x: " + center1.getX() + " C1y: " + center1.getY());
//				System.out.println("C2x: " + center2.getX() + " C2y: " + center2.getY());
//				
//				intersection = calcIntersection(center1, center2, radius1, radius2);
//				System.out.println("--" + scanResult[0] + "--");
//				System.out.println("--" + scanResult[1] + "--");
//				System.out.println("--" + scanResult[2] + "--");
//				System.out.println("X cor: " + intersection.getX() + " Y Cor: " + intersection.getY());
//
//			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		reader.reset();
		betaList.clear();

		return dst;
	}

	public double calcRadius(double afstand, double vinkel) {
		double radius = (0.5 * afstand) / Math.sin(vinkel);
		return radius;
	}

	public Vector2 calcCenter(Vector2 p1, Vector2 p2, double afstand, double vinkel) {

		double var0 = Math.sqrt(Math.pow(Math.abs(-p2.getY()+p1.getY()), 2) + Math.pow(Math.abs(-p2.getX()+p1.getX()), 2));
		double var1 = Math.sqrt(Math.pow(afstand, 2)/Math.pow(Math.sin(vinkel), 2) - Math.pow(afstand, 2));
		double x = 0.5 * (p2.getY() - p1.getY())/var0 * var1 + (0.5 * p1.getX()) + (0.5 * p2.getX());
		double y = 0.5 * (-p2.getX() + p1.getX())/var0 * var1 + (0.5 * p1.getY()) + (0.5 * p2.getY());
		return new Vector2(x, y);
	}

	public Vector2 calcIntersection(Vector2 center1, Vector2 center2, double radius1, double radius2) {

		double d = Math
				.sqrt(Math.pow(center1.getX() - center2.getX(), 2) + Math.pow(center1.getY() - center2.getY(), 2));
		double t1 = Math.pow(radius1, 2) - Math.pow(radius2, 2) + Math.pow(d, 2);
		double d1 = t1 / (2 * d);
		double h = Math.sqrt(Math.pow(radius1, 2) - Math.pow(d1, 2));
		double x3 = center1.getX() + (d1 * (center2.getX() - center1.getX())) / d;
		double y3 = center1.getY() + (d1 * (center2.getY() - center1.getY())) / d;
		double x4 = x3 + (h * (center2.getY() - center1.getY())) / d;
		double y4 = y3 - (h * (center2.getX() - center1.getX())) / d;
		return new Vector2(x4, y4);
	}
	
	public static void sortResults(Result[] array, int fra, int til) {
		// code from
		// http://www.programcreek.com/2012/11/quicksort-array-in-java/
		if (fra >= til){
			return;
		}
		int start = fra + (til - fra) / 2;
		Result current = array[start];


		int i = fra, j = til;
		while (i <= j) {
			while (array[i].getResultPoints()[0].getX() < current.getResultPoints()[0].getX()) {
				i++;
			}
			while (array[j].getResultPoints()[0].getX() > current.getResultPoints()[0].getX()) {
				j--;
			}
			if (i <= j) {
				Result temp = array[i];
				array[i] = array[j];
				array[j] = temp;
				i++;
				j--;
			}
		}
		if (fra < j){
			sortResults(array, fra, j);
		}
		if (til > i){
			sortResults(array, i, til);
		}
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
		final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		System.arraycopy(b, 0, targetPixels, 0, b.length);
		return image;
	}
}
