package dtu.grp13.drone.gui;

import java.awt.Button;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

import org.jfree.ui.tabbedui.VerticalLayout;

import de.yadrone.base.navdata.BatteryListener;
import de.yadrone.base.navdata.NavData;
import de.yadrone.base.navdata.NavDataManager;
import dtu.grp13.drone.core.CommandThread;
import dtu.grp13.drone.core.ProgramManager;
import dtu.grp13.drone.util.WFGUtilities;
import dtu.grp13.drone.vector.Vector2;



public class ControlFrame {
	private MyPanel panel;
	private JFrame frame;
	private ProgramManager pm;
	private NavDataManager navdata;
	private BatteryListener bl;
	private JTextArea logText;
	
	public ControlFrame(ProgramManager pm, NavDataManager navData){
		this.pm = pm;
		this.navdata = navData;
		logText = new JTextArea();
		logText.setEditable(false);
		panel = new MyPanel();
		frame = new JFrame();
		frame.getContentPane().setLayout(new VerticalLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Control System");
		frame.getContentPane().add(panel);
		JScrollPane scroll = new JScrollPane(logText);
		scroll.setPreferredSize(new Dimension(panel.getWidth(), 350));
		frame.getContentPane().add(scroll);
		frame.setResizable(false);
		frame.setLocation(1295, 565);
		frame.setVisible(true);
		frame.pack();
		WFGUtilities.LOGGER.addHandler(new WindowLogHandler());
	}
	
	public void writeToLog(String msg){
		logText.append(msg);
		frame.validate();
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
			this.batteryStatus.setText("<html><font color='red'>Battery: 00</font></html>");

			this.bStart.setPreferredSize(new Dimension(90,50));
			this.bStop.setPreferredSize(new Dimension(90,50));
			this.bEmergency.setPreferredSize(new Dimension(90,50));
			this.bTakeOff.setPreferredSize(new Dimension(90,50));
			this.bChangeCam.setPreferredSize(new Dimension(90,50));
			//this.batteryStatus.setPreferredSize(new Dimension(90,50));
			this.batteryStatus.setFont(new Font("Arial", Font.PLAIN, 11));
			bl = new BatteryListener() {
				
				@Override
				public void voltageChanged(int arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void batteryLevelChanged(int arg0) {
					
					
					if(79 < arg0){
						batteryStatus.setText("<html><font color='green'>"+ String.valueOf(arg0) + "</font></html>" );
					}else if(59 < arg0 && arg0 < 80){
						batteryStatus.setText("<html><font color='#ff0000'>"+ String.valueOf(arg0) + "</font></html>");
					}else if(30 < arg0 && arg0 < 60){
						batteryStatus.setText("<html><font color='orange'>"+ String.valueOf(arg0) + "</font></html>");
					}else if(0 < arg0 && arg0 < 31){
						batteryStatus.setText("<html><font color='red'>"+ String.valueOf(arg0) + "</font></html>");
					}
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
					//pm.rotateToWall(2);
					try {
						pm.initHeight();
						//pm.flyToPoint(new Vector2(200,200));
						pm.searchRoom();
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
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
	
	public class WindowLogHandler extends Handler {
		private Formatter formatter = null;
		
		public WindowLogHandler() {
			LogManager manager = LogManager.getLogManager();
		    formatter = WFGUtilities.getWFGFormatter();
		}
		
		@Override
		public void publish(LogRecord record) {
			String message = null;
		    if (!isLoggable(record))
		      return;
		    message = formatter.format(record);
		    writeToLog(message);
		}

		@Override
		public void flush() {
			
		}

		@Override
		public void close() throws SecurityException {
			
		}
		
	}
}
