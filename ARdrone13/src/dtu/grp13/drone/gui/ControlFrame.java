package dtu.grp13.drone.gui;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import de.yadrone.base.navdata.BatteryListener;
import de.yadrone.base.navdata.NavData;
import de.yadrone.base.navdata.NavDataManager;
import dtu.grp13.drone.core.CommandThread;
import dtu.grp13.drone.core.ProgramManager;



public class ControlFrame {
	private MyPanel panel;
	private JFrame frame;
	private ProgramManager pm;
	private NavDataManager navdata;
	private BatteryListener bl;
	
	
	public ControlFrame(ProgramManager pm, NavDataManager navData){
		this.pm = pm;
		this.navdata = navData;
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
		JLabel batteryStatus;
		
		public MyPanel(){
			this.bStart = new Button();
			this.bEmergency = new Button();
			this.bTakeOff = new Button();
			this.bStop = new Button();
			this.bChangeCam = new Button();
			this.batteryStatus = new JLabel();
			
			this.bStart.setLabel("START");
			this.bEmergency.setLabel("EMERGENCY");
			this.bTakeOff.setLabel("TAKEOFF");
			this.bStop.setLabel("STOP");
			this.bChangeCam.setLabel("CCAM");
			this.batteryStatus.setText("Battery: ");

			this.bStart.setPreferredSize(new Dimension(100,50));
			this.bStop.setPreferredSize(new Dimension(100,50));
			this.bEmergency.setPreferredSize(new Dimension(100,50));
			this.bTakeOff.setPreferredSize(new Dimension(100,50));
			this.bChangeCam.setPreferredSize(new Dimension(100,50));
			this.batteryStatus.setPreferredSize(new Dimension(100,50));
			bl = new BatteryListener() {
				
				@Override
				public void voltageChanged(int arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void batteryLevelChanged(int arg0) {
					batteryStatus.setText("Battery: " + String.valueOf(arg0));
				}
			};
			navdata.addBatteryListener(bl);

			this.bEmergency.setBackground(Color.RED);
			this.bStart.setBackground(Color.GREEN);
			
			this.add(bTakeOff);
			this.add(bStart);
			this.add(bStop);
			this.add(bEmergency);
			this.add(bChangeCam);
			this.add(batteryStatus);
			
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
					//pm.testCycleTwo();
					pm.rotateToWall(2);
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
			
			this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_H, 0, true), "speed_down");
			this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_J, 0, true), "speed_up");
			this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, 0, true), "time_down");
			this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_U, 0, true), "time_up");
			
			this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, true), "forward");
			this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, true), "left");
			this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, true), "backward");
			this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, true), "right");
			
			this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_Q, 0, true), "rot_l");
			this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_E, 0, true), "rot_r");
			
			this.getActionMap().put("rot_r", new AbstractAction() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					pm.testRotR();
				}
			});
			this.getActionMap().put("rot_l", new AbstractAction() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					pm.testRotL();
				}
			});
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
