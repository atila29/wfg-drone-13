package dtu.grp13.drone.test.gui;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.opencv.core.Mat;


public class MainFrame extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MenuPanel menuPanel;
	private BottomPanel bottomPanel;
	private CamPanel camPanel;
	
	public JFrame getFrame() {
		return frame;
	}

	private final JFrame frame;
	
	public MainFrame(String title){
		super(title);
		
		
		// Set layout manager
		setLayout(new BorderLayout());
		
		// Create Swing component
		final JTextArea camTA = new JTextArea();
		
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//final JTextArea consolTA = new JTextArea();
		//JButton button = new JButton("Emergency Landing");
		
		menuPanel = new MenuPanel();
		bottomPanel = new BottomPanel();
		camPanel = new CamPanel();
		
		// Add Swing components
		Container mainContainer = getContentPane();
		
		mainContainer.add(camPanel, BorderLayout.CENTER);
		mainContainer.add(menuPanel, BorderLayout.EAST);
		mainContainer.add(bottomPanel, BorderLayout.SOUTH);
		
		
		
		//c.add(button, BorderLayout.SOUTH);
		//c.add(consolTA, BorderLayout.SOUTH);

	}
	public void render(Mat image) {
		//Mat man = processMat(image);
		Image i = toBufferedImage(image);
		
		camPanel.setImage(i);
		camPanel.repaint();
		frame.pack();
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
