package dtu.grp13.drone.core.matproc.procs;

import java.io.IOException;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.Rect;

import com.google.zxing.NotFoundException;

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
			if (rects.size() == 3) {
			String qrResult = qa.scanQr(currentFrame, qa.getMidRect(rects));
			Vector2 position = ps.calcIntersection(qrResult, rects);
			System.out.println("Pos: " + position);
			pm.positionFound(position);
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
