package dtu.grp13.drone.test.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;


public class BottomPanel extends JPanel {

	//JPanel bottomContainer = new JPanel();
	JButton btnEmergLand;
	JTextArea consolTA;
	
	public BottomPanel(){
		Dimension size = getPreferredSize();
		size.height = 100;
		//size.width = 250;
		setPreferredSize(size);
		
		//setBorder(BorderFactory.createTitledBorder("Consol"));
		
		// Set layout manager
		setLayout(new GridBagLayout());
		
		// Create Swing component
		btnEmergLand = new JButton("Emergency Landing");
		//consolTA = new JTextArea();
				
		//consolPanel = new ConsolPanel();
		
		
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
		
		
		add(btnEmergLand, gridConst);
		//gridConst.gridy = 2;
		//add(consolPanel, gridConst);
		
		// Add behaviour
		btnEmergLand.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent actEv) {
				//ConsolPanel.consolTA.append("yo\n");
				//consolTA.append("Hello\n");
			}
					
		});
		
		//this.add(bottomContainer);
		
		//setBorder(BorderFactory.createTitledBorder(""));
	}
}
