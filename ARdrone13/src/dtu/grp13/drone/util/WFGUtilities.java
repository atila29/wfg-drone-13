package dtu.grp13.drone.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.List;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Rect;

public class WFGUtilities {
	public static Mat bufferedImageToMat(BufferedImage bi) {
		Mat mat = new Mat(bi.getHeight(), bi.getWidth(), CvType.CV_8UC3);
		byte[] data = ((DataBufferByte) bi.getRaster().getDataBuffer())
				.getData();
		mat.put(0, 0, data);
		return mat;
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
	
	public static List<Rect> sortResults(List<Rect> qrCordList, int fra, int til) {
		// code from
		// http://www.programcreek.com/2012/11/quicksort-array-in-java/
		if (fra >= til){
			return qrCordList;
		}
		int start = fra + (til - fra) / 2;
		Rect current = qrCordList.get(start);


		int i = fra, j = til;
		while (i <= j) {
			while (qrCordList.get(i).x < current.x) {
				i++;
			}
			while (qrCordList.get(j).x > current.x) {
				j--;
			}
			if (i <= j) {
				Rect temp = qrCordList.get(i);
				qrCordList.set(i, qrCordList.get(j));
				qrCordList.set(j, temp);
				i++;
				j--;
			}
		}
		if (fra < j){
			sortResults(qrCordList, fra, j);
		}
		if (til > i){
			sortResults(qrCordList, i, til);
		} 
		
		return qrCordList;
	}
	
}
