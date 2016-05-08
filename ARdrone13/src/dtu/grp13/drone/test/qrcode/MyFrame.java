package dtu.grp13.drone.test.qrcode;

import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.print.attribute.standard.RequestingUserName;
import javax.swing.JFrame;

import org.omg.CORBA.SystemException;
import org.opencv.core.Mat;
import org.opencv.core.Point;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.multi.qrcode.QRCodeMultiReader;


public class MyFrame {

	private final JFrame frame;
	private final MyPanel panel;
	private double horizontalRadians = 1.30899694;
	private double widthRes = 1280;
	private double heightRes = 720;
	private double realDistQR;
	private List<Double> betaList = new ArrayList<Double>();
	private double distvRadian;
	private double disthRadian;
	private double radius1;
	private double radius2;
	private Point center1;
	private Point center2;
	private Point p1 = new Point(0.0, 0.0);
	private Point p2 = new Point(1.0, 2.0);
	private Point p3 = new Point(0.0, 4.0);

	private double beta;
	private double b = (widthRes / 2) / (Math.tan(horizontalRadians / 2));
	private double t;

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
		Mat man = editFrame(image);
		Image i = toBufferedImage(man);
		panel.setImage(i);
		panel.repaint();
		frame.pack();
	}

	public Mat editFrame(Mat a) {
		Image i = toBufferedImage(a);
		LuminanceSource source = new BufferedImageLuminanceSource((BufferedImage) i);
		BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
		QRCodeMultiReader reader = new QRCodeMultiReader();
		try {
			Result[] scanResult = reader.decodeMultiple(bitmap);
			if (scanResult.length < 3) {
				reader.reset();
				return a;
			}
			for (int index = 0; index < scanResult.length; index++) {
				System.out.println(scanResult[index].getText());
				if (scanResult[index].getResultPoints()[0].getX() > 640)
					t = scanResult[index].getResultPoints()[0].getX() - 640;
				else
					t = 640 - scanResult[index].getResultPoints()[0].getX();
				beta = Math.atan(t / b);
				betaList.add(beta);
				// System.out.println("Vinklen mellem midten og qr kode " +
				// (index+1) + " er: " + beta);
			}
			if (scanResult[1].getResultPoints()[0].getX() > 640) {
				disthRadian = betaList.get(2) - betaList.get(1);
				if (scanResult[0].getResultPoints()[0].getX() > 640)
					distvRadian = betaList.get(2) - betaList.get(0);
				else
					distvRadian = betaList.get(2) + betaList.get(0);

			} else if (scanResult[1].getResultPoints()[0].getX() < 640) {
				distvRadian = betaList.get(0) - betaList.get(1);
				if (scanResult[2].getResultPoints()[0].getX() < 640)
					disthRadian = betaList.get(1) - betaList.get(2);
				else
					disthRadian = betaList.get(1) + betaList.get(2);

				radius1 = calcRadius(Math.sqrt(5), distvRadian);
				radius2 = calcRadius(Math.sqrt(5), disthRadian);
				center1 = calcCenter(p1, p2, Math.sqrt(5), distvRadian);
				center2 = calcCenter(p3, p2, Math.sqrt(5), distvRadian);
				
//				Point centertest1 = new Point(5.995, -1.748);
//				Point centertest2 = new Point(5.443, 5.467);
//				calcIntersection(centertest1, centertest2, 6.245 ,5.628);

				
				
				
			}
		} catch (Exception ex) {
			//System.out.println("-----");
		}
		
		Point centertest1 = new Point(5.995, -1.748);
		Point centertest2 = new Point(5.443, 5.467);
		calcIntersection(centertest1, centertest2, 6.245 , 5.628);

		reader.reset();
		betaList.clear();

		return a;
	}

	public double calcRadius(double afstand, double vinkel) {
		double radius = (0.5 * afstand) / Math.sin(vinkel);
		return radius;
	}

	public Point calcCenter(Point p1, Point p2, double afstand, double vinkel) {
		Point center = new Point();
		center.x = 0.5 * (((p2.y - p1.y) / Math.sqrt(Math.pow(2, -p2.y + p1.y) + Math.pow(2, -p2.x + p1.x))))
				* (Math.sqrt((Math.pow(2, afstand)) / Math.pow(2, Math.sin(vinkel))) - Math.pow(2, afstand))
				+ (0.5 * p1.x) + (0.5 * p2.x);
		center.y = 0.5 * (((p2.x - p1.x) / Math.sqrt(Math.pow(2, -p2.y + p1.y) + Math.pow(2, -p2.x + p1.x))))
				* (Math.sqrt((Math.pow(2, afstand)) / Math.pow(2, Math.sin(vinkel))) - Math.pow(2, afstand))
				+ (0.5 * p1.y) + (0.5 * p2.y);
		return center;
	}
	
	public Point calcIntersection(Point center1, Point center2, double radius1, double radius2) {
		
		double d = Math.sqrt(Math.pow(center1.x-center2.x, 2)+Math.pow(center1.y-center2.y, 2));
		System.out.println("d = " + d);
		double t1 = Math.pow(radius1, 2) - Math.pow(radius2, 2) + Math.pow(d, 2);
		System.out.println("t1 = " + t1);
		double d1 = t1/(2*d);
		System.out.println("d1 = " + d1);
		double h = Math.sqrt(Math.pow(radius1, 2) - Math.pow(d1, 2));
		System.out.println("h = " + h);
		double x3 = center1.x + (d1 * (center2.x-center1.x))/d;
		System.out.println("x3 = " + x3);
		double y3 = center1.y + (d1 * (center2.y-center1.y))/d;
		System.out.println("y3 = " + y3);
		double x4 = x3 + (h * (center2.y - center1.y))/d;
		double y4 = y3 - (h * (center2.x - center1.x))/d;
		System.out.println("X cor: " + x4 + " Y cor: " + y4);
		return new Point(x4, y4);
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
