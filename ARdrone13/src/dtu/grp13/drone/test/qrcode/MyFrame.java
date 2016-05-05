package dtu.grp13.drone.test.qrcode;

import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import org.opencv.core.Mat;
import org.opencv.core.Point;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.multi.qrcode.QRCodeMultiReader;
import com.google.zxing.qrcode.QRCodeReader;

public class MyFrame {

	private final JFrame frame;
	private final MyPanel panel;
	private double horizontalRadians =  1.30899694;
	private double widthRes = 1280;
	private double heightRes = 720;
	private double realDistQR;
	private List<Double> betaList = new ArrayList<Double>();
	private double distvRadian;
	private double disthRadian;
	private double beta;
	private double b = (widthRes/2)/(Math.tan(horizontalRadians/2));
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
		try
		{
			Result[] scanResult = reader.decodeMultiple(bitmap);
			if(scanResult.length < 3){
				reader.reset();
				return a;
			}
			for(int index = 0; index < scanResult.length; index++){
				System.out.println(scanResult[index].getText());
				if(scanResult[index].getResultPoints()[0].getX() > 640)
					t = scanResult[index].getResultPoints()[0].getX() - 640;
				else 
					t = 640 - scanResult[index].getResultPoints()[0].getX();
				beta = Math.atan(t/b);
				betaList.add(beta);
//				System.out.println("Vinklen mellem midten og qr kode " + (index+1) + " er: " + beta);
			}
			if(scanResult[1].getResultPoints()[0].getX() > 640){
				disthRadian = betaList.get(2)-betaList.get(1);
				if(scanResult[0].getResultPoints()[0].getX() > 640)
					distvRadian = betaList.get(2)-betaList.get(0);
				else 
					distvRadian = betaList.get(2)+betaList.get(0);
				

			} else if(scanResult[1].getResultPoints()[0].getX() < 640){
				distvRadian = betaList.get(0)-betaList.get(1);
				if(scanResult[2].getResultPoints()[0].getX() < 640)
					disthRadian = betaList.get(1)-betaList.get(2);
				else
					disthRadian = betaList.get(1)+betaList.get(2);
				
			}
		} catch (Exception ex) {
			System.out.println("-----");
		}
		
		reader.reset();
		betaList.clear();
		
		
		return a;
	}
	
	public double calcRadius(double afstand, double vinkel){
		double radius = (0.5*afstand)/Math.sin(vinkel);
		return radius;
	}

	public Point calcCenter(Point p1, Point p2, double afstand, double vinkel){
		Point center = new Point();
		center.x = 0.5*(((p2.y-p1.y)/Math.sqrt(Math.pow(2, -p2.y+p1.y)+Math.pow(2, -p2.x+p1.x))))*(Math.sqrt((Math.pow(2, afstand))/Math.pow(2, Math.sin(vinkel)))-Math.pow(2, afstand))+(0.5*p1.x)+(0.5*p2.x);
		center.y = 0.5*(((p2.x-p1.x)/Math.sqrt(Math.pow(2, -p2.y+p1.y)+Math.pow(2, -p2.x+p1.x))))*(Math.sqrt((Math.pow(2, afstand))/Math.pow(2, Math.sin(vinkel)))-Math.pow(2, afstand))+(0.5*p1.y)+(0.5*p2.y);
		return center;
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
