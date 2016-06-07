package dtu.grp13.drone.test.color;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

import dtu.grp13.drone.test.drone.VectorAnalyzer;

public class MyPanel extends JPanel {
	private Image img;

	public MyPanel() {
	}

	public void setImage(Image img) {
                // Image which we will render later
		this.img = img;

                // Set JPanel size to image size
		Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		setSize(size);
		setLayout(null);
	}

        @Override
	public void paintComponent(Graphics g) {
		g.drawImage(img, 0, 0, null);
	}
}