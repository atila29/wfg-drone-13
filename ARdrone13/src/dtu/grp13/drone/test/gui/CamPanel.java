package dtu.grp13.drone.test.gui;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.opencv.core.Mat;

import dtu.grp13.drone.test.drone.MyFrame;


public class CamPanel extends JPanel {

	JTextArea camTA;
	private Image img;
	
	public CamPanel(){
		Dimension size = getPreferredSize();
		//size.height = 100;
		//size.width = 250;
		setPreferredSize(size);
		
		setBorder(BorderFactory.createTitledBorder("Camera Feed"));
		
		// Set layout manager
		setLayout(new GridBagLayout());
		
		// Create Swing component		
		camTA = new JTextArea();
		
		// Add Swing components
		GridBagConstraints gridConst = new GridBagConstraints();
		gridConst.gridx = 1;
		gridConst.gridy = 1;
		gridConst.gridwidth = 1;
		gridConst.gridheight = 1;
		gridConst.weightx = 50;
		gridConst.weighty = 100;
		gridConst.insets = new Insets(5,5,5,5);
		gridConst.anchor = GridBagConstraints.CENTER;
		gridConst.fill = GridBagConstraints.BOTH;
		
		
		
		
		//add(camTA, gridConst);
		
		// Add behaviour
		
		/*
		btnEmergLand.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent actEv) {
				consolTA.append("Hello\n");
			}
			
			
					
		}); */
		
		//this.add(bottomContainer);
		
		//setBorder(BorderFactory.createTitledBorder(""));
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
