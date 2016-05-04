package dtu.grp13.drone.test.qrcode;

import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import javax.swing.JFrame;

import org.opencv.core.Mat;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

public class MyFrame {

	private final JFrame frame;
	private final MyPanel panel;
	private double Pxh;
	private double fl = 546.5;
	private double IRLh = 100;
	private double IMGh;
	private double Senh = 222;

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
		IMGh = image.height();
	}
	
	public Mat editFrame(Mat a) {
		Image i = toBufferedImage(a);
		LuminanceSource source = new BufferedImageLuminanceSource((BufferedImage) i);
		BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
		QRCodeReader reader = new QRCodeReader();
		try
		{
			Result scanResult = reader.decode(bitmap);
			System.out.println(scanResult.getText());
			Pxh = scanResult.getResultPoints()[2].getY()-scanResult.getResultPoints()[0].getY();
			System.out.println("Højden i px = " + Pxh);
		} catch (Exception ex) {
			System.out.println("-----");
		}
		
		reader.reset();
		
		
		return a;
	}
	
	public double calcDist(double focal, double objhøjde, double imghøjde, double objpix, double sensh){
		double dist = (focal*objhøjde*imghøjde)/(objpix*sensh);
		return dist;
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
