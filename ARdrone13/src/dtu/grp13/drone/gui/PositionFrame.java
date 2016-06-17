package dtu.grp13.drone.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import dtu.grp13.drone.vector.Matrix2;
import dtu.grp13.drone.vector.Vector2;

public class PositionFrame {
	private MyPanel panel;
	private JFrame frame;
	private Vector2 origo;
	private Matrix2 flip, scale, transformValue;
	
	
	public PositionFrame() {
		panel = new MyPanel();
		frame = new JFrame();
		frame.getContentPane().setLayout(new FlowLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("c_system");
		frame.getContentPane().add(panel);
		frame.setResizable(false);
		frame.setLocation(1295, 0);
		frame.setVisible(true);
		origo = new Vector2(0, 523);
		flip = new Matrix2(2,2, 1,0,
								0,-1);
		scale = new Matrix2(2,2, 0.5, 0,
								 0,	 0.5);
		transformValue = flip.dot(scale);
		frame.pack();
	}
	
	public Vector2 transform(Vector2 v) {
		return transformValue.mul(v).add(origo);
	}
	
	public void drawWallMarks(List<Vector2> wallmarks) {
		List<Vector2> transformedMarks = new ArrayList<Vector2>();
		for(Vector2 v : wallmarks){
			transformedMarks.add(transform(v));
		}
		panel.setWallmarks(transformedMarks);
		frame.pack();
	}
	
	public void setDronePosition(Vector2 c) {
		panel.setCoor(transform(c));
		frame.pack();
	}
	
	private class MyPanel extends JPanel {
		private Vector2 coor;
		private Image img;
		private List<Vector2> wallmarks;
		
		
		public MyPanel(){
			try {
				img = ImageIO.read(new File("./resources/koordinate_system.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
			setPreferredSize(size);
			setMinimumSize(size);
			setMaximumSize(size);
			setSize(size);
			setLayout(null);
		}
		
		public void setWallmarks(List<Vector2> wallmarks) {
			this.wallmarks = wallmarks;
			this.repaint();
		}
		
		public void setCoor(Vector2 coor){
			this.coor = coor;
			this.repaint();
		}
		
	    @Override
		public void paintComponent(Graphics g) {
			g.drawImage(img, 0, 0, null);
			if(coor != null) {
				int x = (int)coor.getX()+6;
				int y = (int)coor.getY()+6;
				g.setColor(new Color(255, 0, 0));
				g.fillOval(x, y, 12, 12);
			}
			if(wallmarks != null){
				for(Vector2 v : wallmarks){
					int x = (int)v.getX()-6;
					int y = (int)v.getY();
					g.setColor(new Color(0, 0, 0));
					g.fillOval(x, y, 12, 12);
				}
			}
		}
	}
	
}
