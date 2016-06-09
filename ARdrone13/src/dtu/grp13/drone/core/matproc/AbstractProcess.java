package dtu.grp13.drone.core.matproc;

import java.awt.FlowLayout;
import java.awt.Image;

import javax.swing.JFrame;

import org.opencv.core.Mat;

import dtu.grp13.drone.test.color.MyPanel;
import dtu.grp13.drone.util.WFGUtilities;

public abstract class AbstractProcess implements Processable{
	private final MyPanel panel;
	private final JFrame frame;
	
	public AbstractProcess() {
		panel = new MyPanel();
		frame = new JFrame();
		frame.getContentPane().setLayout(new FlowLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("wc - 0.1 - 29");
		// JPanel which is used for drawing image
		
		frame.getContentPane().add(panel);
		frame.setVisible(true);
	}
	@Override
	public void render(Mat image) {
		Mat man = processMat(image);
		Image i = WFGUtilities.toBufferedImage(man);
		panel.setImage(i);
		i = null;
		panel.repaint();
		frame.pack();
	}
}
