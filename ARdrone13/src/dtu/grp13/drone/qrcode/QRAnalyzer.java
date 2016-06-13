package dtu.grp13.drone.qrcode;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

import dtu.grp13.drone.core.PositionSystem;
import dtu.grp13.drone.util.WFGUtilities;

public class QRAnalyzer {

	public String scanQr(Mat src) throws NotFoundException, ChecksumException, FormatException {
		Image img = WFGUtilities.toBufferedImage(src);
		LuminanceSource source = new BufferedImageLuminanceSource((BufferedImage) img);
		BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
		//QRCodeReader reader = new QRCodeReader();
		MultiFormatReader reader = new MultiFormatReader();
		Result scanResult = reader.decode(bitmap);
		
		System.out.println(scanResult.getText());
		
		return scanResult.getText();

	}
	
	public Rect getMidRect(List<Rect> rects) throws IOException {
		return WFGUtilities.sortResults(rects, 0, rects.size()-1).get(1);
	}
	 
	public String scanQr(Mat src, Rect rect) throws NotFoundException, ChecksumException, FormatException {
		
		Mat qr =  src.submat(rect);
		//Imgproc.resize(qr, qr, new Size(500, 707));


		Image img = WFGUtilities.toBufferedImage(qr);
		LuminanceSource source = new BufferedImageLuminanceSource((BufferedImage) img);
		BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
		//QRCodeReader reader = new QRCodeReader();
		MultiFormatReader reader = new MultiFormatReader();
		Result scanResult = reader.decode(bitmap);
		
		System.out.println(scanResult.getText());
		
		return scanResult.getText();

	}
	
	public List<Rect> findQrEdges(Mat src, Mat dst) {
		List<MatOfPoint> edges = new ArrayList<MatOfPoint>();
		Mat s = src.clone();
		// lidt billedebahandling
		Imgproc.cvtColor(s, s, Imgproc.COLOR_BGR2GRAY);
		Imgproc.GaussianBlur(s, s, new Size(3, 3), 3);
		Imgproc.Canny(s, s, 100, 200); // læs op på, noget med lysforhold

		Imgproc.findContours(s, edges, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
		List<Rect> rectList = new ArrayList<>();
		for (int i = 0; i < edges.size(); i++) {
			MatOfPoint2f mop2 = new MatOfPoint2f();
			edges.get(i).convertTo(mop2, CvType.CV_32FC1);
			double marg = 0.01f;
			double ep = marg * Imgproc.arcLength(mop2, true);
			Imgproc.approxPolyDP(mop2, mop2, ep, true);
			mop2.convertTo(edges.get(i), CvType.CV_32S);

			if (edges.get(i).total() == 4) {

				Rect rect = Imgproc.boundingRect(edges.get(i));
				double ratio = (double) rect.height / (double) rect.width;

				if (ratio > 1.3 && ratio < 2.5 && rect.height > 80.0 && rect.height < 200) {
					rectList.add(rect);
				}
			}
		}
		double difHeight;
		double difWidth;
		for (int i = 0; i < rectList.size(); i++) {
			for (int j = 0; j < rectList.size(); j++) {
				if (i == j) {
					continue;
				}
				difWidth = Math.abs(rectList.get(i).x - rectList.get(j).x);
				difHeight = Math.abs(rectList.get(i).y - rectList.get(j).y);
				

				if (difWidth < 20) {
					rectList.remove(j);
				}
			}
		}
		
		for (int i = 0; i < rectList.size(); i++) {
			Imgproc.rectangle(dst, rectList.get(i).tl(), rectList.get(i).br(), new Scalar(255, 0, 0));
			Imgproc.drawMarker(dst, new Point(rectList.get(i).x, rectList.get(i).y), new Scalar(0, 255, 0));
		}
		System.out.println("Rect count = " + rectList.size());
		
		return rectList;
	}
	
	
}
