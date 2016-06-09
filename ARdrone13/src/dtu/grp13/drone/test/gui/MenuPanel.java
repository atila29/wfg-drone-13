package dtu.grp13.drone.test.gui;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;


public class MenuPanel extends JPanel {

	JButton btnStart, btnFindPoint, btnMore, btnEmergLand;
	String points[]={
			"Find Point 1",
			"Find Point 2",
			"Find Point 3"
	};
	JComboBox cb=new JComboBox(points);
	
	public MenuPanel(){
		Dimension size = getPreferredSize();
		size.width = 250;
		setPreferredSize(size);
		
		setBorder(BorderFactory.createTitledBorder("Menu"));
		
		setLayout(new GridBagLayout());
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
		
		
		btnStart = new JButton("Start");
		btnFindPoint = new JButton("Find Points");
		btnMore = new JButton("More");
		btnEmergLand = new JButton("Land");
		
		add(btnStart, gridConst);
		gridConst.gridy = 2;
		add(cb, gridConst);
		gridConst.gridy = 3;
		add(btnFindPoint, gridConst);
		gridConst.gridy = 4;
		add(btnMore, gridConst);
		gridConst.gridy = 5;
		add(btnEmergLand, gridConst);
	}
}
