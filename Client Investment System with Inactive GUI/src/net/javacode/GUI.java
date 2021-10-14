package net.javacode;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.TimeUnit;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.javacode.loginGUI.CType;

public class GUI implements ActionListener{
	
	private JLabel label;
	private JFrame frame;
	private JPanel panel;

	private JButton buttonYes;
	private JButton buttonNo;
	
	
	private enum Actions{
		YES,
		NO
	}
	
	public GUI() {
		
		frame = new JFrame();
		panel = new JPanel();

		frame.setSize(350, 195);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(panel);
		
		panel.setLayout(null);

		
		label = new JLabel("Attempt to connect to Investment Database?");
		label.setBounds(30, 10, 550, 25);
		panel.add(label);

		
		
		buttonYes = new JButton("Yes");
		buttonYes.setBounds(80, 70, 55, 25);
		buttonYes.setActionCommand(Actions.YES.name());
		buttonYes.addActionListener(this);
		panel.add(buttonYes);

		
		buttonNo = new JButton("No");
		buttonNo.setBounds(180, 70, 55, 25);
		buttonNo.setActionCommand(Actions.NO.name());
		buttonNo.addActionListener(this);
		panel.add(buttonNo);


		frame.setTitle("Investment Management System");
		
		frame.setVisible(true);

	}


	//passes to main the various connection status, to indicate if the user chose to load mock DB, if the attempt to connect to DB passed or failed
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand() == Actions.YES.name()) {
			if(driver.testConnection() == true) {
				frame.dispose();
				loginGUI loginP = new loginGUI(true, CType.CONNECTED);
			} else {
				frame.dispose();
				loginGUI loginP = new loginGUI(false, CType.FAILED);		
			}
		} else if(e.getActionCommand() == Actions.NO.name()) {
			frame.dispose();
			loginGUI loginP = new loginGUI(false, CType.OPT);
		}
	}
}
