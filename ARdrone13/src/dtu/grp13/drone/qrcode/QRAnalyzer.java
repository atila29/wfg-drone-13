package dtu.grp13.drone.qrcode;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

import dtu.grp13.drone.util.WFGUtilities;

public class QRAnalyzer {

	public Result scanQr(Mat src) throws NotFoundException, ChecksumException, FormatException {
		Image img = WFGUtilities.toBufferedImage(src);
		LuminanceSource source = new BufferedImageLuminanceSource((BufferedImage) img);
		BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
		// QRCodeReader reader = new QRCodeReader();
		MultiFormatReader reader = new MultiFormatReader();
		Result scanResult = reader.decode(bitmap);

		//System.out.println(scanResult.getText());

		return scanResult;
	}

	public int getMidIndex(List<Rect> rects) throws IOException {
		List<Rect> sortedRects = WFGUtilities.sortResults(rects, 0, rects.size() - 1);
		if (sortedRects.size() == 3) {
			return 1;
		} else {
			double leftDif = Math.abs(sortedRects.get(0).x - 640);
			double rightDif = Math.abs(sortedRects.get(1).x - 640);
			if (leftDif < rightDif) {
				return 0;
			} else {
				return 1;
			}
		}
	}

	public Result scanQr(Mat src, Rect rect) throws NotFoundException, ChecksumException, FormatException {

		Mat qr = src.submat(rect);

		Image img = WFGUtilities.toBufferedImage(qr);
		LuminanceSource source = new BufferedImageLuminanceSource((BufferedImage) img);
		BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
		QRCodeReader reader = new QRCodeReader();
		//MultiFormatReader reader = new MultiFormatReader();
		Result scanResult = reader.decode(bitmap);

		//System.out.println(scanResult.getText());

		return scanResult;
	}

	public List<Rect> findQrEdges(Mat src, Mat dst) {
		List<MatOfPoint> edges = new ArrayList<MatOfPoint>();
		Mat s = src.clone();
		Imgproc.cvtColor(s, s, Imgproc.COLOR_BGR2GRAY);
		Imgproc.GaussianBlur(s, s, new Size(5, 5), 0);

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
				boolean keepRect = true;

				if (ratio > 1.3 && ratio < 2.5) {
					if (rectList.size() == 0) {
						rectList.add(rect);
					} else {
						for (int j = 0; j < rectList.size(); j++) {
							double dif = Math.abs(rect.x - rectList.get(j).x);
							if (dif < 40) {
								keepRect = false;
								if (rect.height < rectList.get(j).height) {
									rectList.set(j, rect);
								}
								
							}
						}
						if (keepRect) {
							rectList.add(rect);
						}
					}
				}
			}
		}

		for (int i = 0; i < rectList.size(); i++) {
			// System.out.println("rect x " + rectList.get(i).x + " y: " +
			// rectList.get(i).y);
			// System.out.println("height: " + rectList.get(i).height + " width:
			// " + rectList.get(i).width);
			Imgproc.rectangle(dst, rectList.get(i).tl(), rectList.get(i).br(), new Scalar(255, 0, 0), 1);
			Imgproc.drawMarker(dst, new Point(rectList.get(i).x, rectList.get(i).y), new Scalar(0, 255, 0));
		}
		return rectList;
	}

}
