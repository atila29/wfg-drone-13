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
import dtu.grp13.drone.util.WFGUtilities;
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
			List<Rect> rects = qa.findQrEdges(currentFrame, img);
			List<Rect> sortedCords = WFGUtilities.sortResults(rects, 0, rects.size() - 1);
			if (sortedCords.size() >= 2 && sortedCords.size() <= 3) {
				int qrIndex = qa.getMidIndex(sortedCords);
				Result qrResult = qa.scanQr(currentFrame, sortedCords.get(qrIndex));
				Vector2 position = ps.calcPosition(sortedCords, qrResult, qrIndex);
				WFGUtilities.LOGGER.info("Position (p3): " + position.toString());
				//double orientation = ps.findOrientation(position, qrResult);
				double orientation = ps.findOrientation(position, sortedCords.get(qrIndex), qrResult.getText());
				System.out.println("Pos: " + position);
				System.out.println("Orientation: " + Math.toDegrees(orientation));
				//System.out.println("Orientation: " + orientation.toString());
				pm.positionFound(position, orientation);
				
			}
		} 
		catch (NotFoundException e) {
			//e.printStackTrace();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return img;
	}

}