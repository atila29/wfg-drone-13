package dtu.grp13.drone.core.matproc.procs;

import java.io.IOException;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.Rect;

import com.google.zxing.NotFoundException;
import com.google.zxing.Result;

import dtu.grp13.drone.core.PositionSystem;
import dtu.grp13.drone.core.ProgramManager;
import dtu.grp13.drone.qrcode.QRAnalyzer;
import dtu.grp13.drone.vector.Vector2;

public class QrProc implements IMatProcess{
	private QRAnalyzer qa;
	private PositionSystem ps;
	private Mat currentFrame;
	private ProgramManager pm;
	
	public QrProc(ProgramManager pm, PositionSystem posSys) {
		this.pm = pm;
		qa = new QRAnalyzer();
		ps = posSys;
	}
	
	@Override
	public Mat processMat(Mat a) {
		currentFrame = a;
		// LOGIK
		Mat img = currentFrame.clone();
		try {
			List<Rect> rects = qa.findQrEdges(a, img);
			if (rects.size() >= 2 && rects.size() <= 3) {
				Rect qrRect = qa.getMidRect(rects);
				Result qrResult = qa.scanQr(currentFrame, qrRect);
				Vector2 position = ps.calcPosition(rects, qrResult);
				//double orientation = ps.findOrientation(position, qrResult);
				double orientation = ps.findOrientation(position, qrRect, qrResult.getText());
				System.out.println("Pos: " + position);
				System.out.println("Orientation: " + (orientation/(2*Math.PI))*360);
				//System.out.println("Orientation: " + orientation.toString());
				pm.positionFound(position, orientation);
				
			}
		} 
		catch (NotFoundException e) {
			e.printStackTrace();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return img;
	}

}
