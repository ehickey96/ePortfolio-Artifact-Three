
package net.javacode;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;



public class loginGUI implements ActionListener{
	
	public static enum CType{
		CONNECTED,
		FAILED,
		OPT
	}

	private JLabel subTitle;
	private JLabel loginTitle;
	private JLabel userIDLabel;
	private JLabel userPasswordLabel;
	private JTextField userIDField;
	private JPasswordField userPasswordField; 
	private JFrame frame;
	private JPanel panel;
	private String username;
	private String password;
	private JButton buttonLogin;
	private boolean connected;

	public loginGUI(boolean tCon, CType tType) {

		connected = tCon;
		frame = new JFrame();
		panel = new JPanel();
		
		userIDField = new JTextField();
		userPasswordField = new JPasswordField();

		frame.setSize(750, 350);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(panel);
		
		panel.setLayout(null);

		subTitle = new JLabel();
		if(tType.name() == CType.CONNECTED.name()) {
			subTitle.setText("Successfully Connected to Investment Database");
		} else if(tType.name() == CType.FAILED.name()) {
			subTitle.setText("Unable to Connect to Investment Database");
			subTitle.setForeground(Color.red);

		} else if(tType.name() == CType.OPT.name())	{
			subTitle.setText("Loaded Mock Database");

		} else {
			subTitle.setText("Error");

		}
		subTitle.setBounds(10,10,550,25);
		panel.add(subTitle);
		
		loginTitle = new JLabel("Enter Username & Password");
		loginTitle.setBounds(10, 40, 550, 25);
		panel.add(loginTitle);

		
		userIDLabel = new JLabel("Username");
		userIDLabel.setBounds(10, 90, 75, 25);
		panel.add(userIDLabel);
		
		userIDField.setBounds(135, 90, 200, 25 );
		panel.add(userIDField);
		

		
		userPasswordLabel = new JLabel("Password");
		userPasswordLabel.setBounds(10, 155, 75, 25);
		panel.add(userPasswordLabel);
		
		userPasswordField.setBounds(135, 155, 200, 25);
		panel.add(userPasswordField);
		
		buttonLogin = new JButton("Login");
		buttonLogin.setBounds(20, 210, 95, 55);
		buttonLogin.addActionListener(this);
		panel.add(buttonLogin);

		
		
		
	

		frame.setTitle("Investment Management System - Login");
		//frame.pack();
		
		frame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String username = "";
		String password = "";
		username = userIDField.getText();
		password = userPasswordField.getText();
		
		//tests if connection works
		if((username = driver.CheckUserPermissionAccess(username,password,connected, true)) == null) {
    		loginTitle.setText("Invalid Username/Password. Please try again");
    		loginTitle.setForeground(Color.red);
    		userIDField.setText("");
    		userPasswordField.setText("");

    	} else {

    		mainMenuGUI mmPage = new mainMenuGUI(connected, username);
    		frame.dispose();

    	}
	
	}

}
