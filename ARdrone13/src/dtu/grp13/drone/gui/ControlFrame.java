package dtu.grp13.drone.gui;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import dtu.grp13.drone.core.CommandThread;
import dtu.grp13.drone.core.ProgramManager;



public class ControlFrame {
	private MyPanel panel;
	private JFrame frame;
	private ProgramManager pm;
	
	
	public ControlFrame(ProgramManager pm){
		this.pm = pm;
		panel = new MyPanel();
		frame = new JFrame();
		frame.getContentPane().setLayout(new FlowLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Control System");
		frame.getContentPane().add(panel);
		frame.setResizable(false);
		frame.setLocation(1295, 565);
		frame.setVisible(true);
		frame.pack();
	}
	
	
	private class MyPanel extends JPanel{
		Button bTakeOff;
		Button bEmergency;
		Button bStart;
		Button bStop;
		Button bChangeCam;
		
		public MyPanel(){
			this.bStart = new Button();
			this.bEmergency = new Button();
			this.bTakeOff = new Button();
			this.bStop = new Button();
			this.bChangeCam = new Button();
			
			this.bStart.setLabel("START");
			this.bEmergency.setLabel("EMERGENCY");
			this.bTakeOff.setLabel("TAKEOFF");
			this.bStop.setLabel("STOP");
			this.bChangeCam.setLabel("CCAM");
			
			this.bStart.setPreferredSize(new Dimension(100,50));
			this.bStop.setPreferredSize(new Dimension(100,50));
			this.bEmergency.setPreferredSize(new Dimension(100,50));
			this.bTakeOff.setPreferredSize(new Dimension(100,50));
			this.bChangeCam.setPreferredSize(new Dimension(100,50));
			
			this.bEmergency.setBackground(Color.RED);
			this.bStart.setBackground(Color.GREEN);
			
			this.add(bTakeOff);
			this.add(bStart);
			this.add(bStop);
			this.add(bEmergency);
			this.add(bChangeCam);
			
			this.bTakeOff.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						pm.takeOffDrone();
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			});
			
			this.bStart.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					pm.firstCycle();
				}
			});
			
			this.bStop.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						pm.landDrone();
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			});
			
			this.bEmergency.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					pm.emergencyDrone();
				}
			});
			
			this.bChangeCam.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					pm.changeCam();
				}
			});
		}
	}
}
