package dtu.grp13.drone.core.matproc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;

import dtu.grp13.drone.core.PositionSystem;
import dtu.grp13.drone.core.matproc.procs.IMatProcess;
import dtu.grp13.drone.cube.CubeDetector;
import dtu.grp13.drone.cube.Filterable;
import dtu.grp13.drone.qrcode.QRAnalyzer;
import dtu.grp13.drone.vector.Vector2;

public class FrameProcess extends AbstractProcess {
	CubeDetector c;
	QRAnalyzer qa;
	PositionSystem ps;
	List<Filterable> filtre = new ArrayList<>();
	IMatProcess proc;
	
	public FrameProcess(IMatProcess proc){
		super();
		qa = new QRAnalyzer();
		this.proc = proc;
		try {
			ps = new PositionSystem();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Mat processMat(Mat a) {
		return proc.processMat(a);
	}

	@Override
	public void changeProcess(IMatProcess proc) {
		this.proc = proc;
	}


}
