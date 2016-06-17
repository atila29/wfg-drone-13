package dtu.grp13.drone.gui;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

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
					//pm.firstCycle();
					pm.testCycleTwo();
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
			
			this.getInputMap().put(KeyStroke.getKeyStroke("H"), "speed_down");
			this.getInputMap().put(KeyStroke.getKeyStroke("J"), "speed_up");
			this.getInputMap().put(KeyStroke.getKeyStroke("Y"), "time_down");
			this.getInputMap().put(KeyStroke.getKeyStroke("U"), "time_up");
			
			this.getInputMap().put(KeyStroke.getKeyStroke("W"), "forward");
			this.getInputMap().put(KeyStroke.getKeyStroke("A"), "left");
			this.getInputMap().put(KeyStroke.getKeyStroke("S"), "backward");
			this.getInputMap().put(KeyStroke.getKeyStroke("D"), "right");
			
			this.getActionMap().put("speed_down", new AbstractAction() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					pm.testSpeedDown();
				}
			});
			this.getActionMap().put("speed_up", new AbstractAction() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					pm.testSpeedUp();
				}
			});
			this.getActionMap().put("time_down", new AbstractAction() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					pm.testTimeDown();
				}
			});
			this.getActionMap().put("time_up", new AbstractAction() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					pm.testTimeUp();
				}
			});
			
			this.getActionMap().put("forward", new AbstractAction() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					pm.testStepF();
				}
			});
			this.getActionMap().put("left", new AbstractAction() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					pm.testStepL();
				}
			});
			this.getActionMap().put("backward", new AbstractAction() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					pm.testStepB();
				}
			});
			this.getActionMap().put("right", new AbstractAction() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					pm.testStepR();
				}
			});
		}
	}
}
