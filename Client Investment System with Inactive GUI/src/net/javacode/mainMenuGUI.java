package net.javacode;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;




public class mainMenuGUI implements ActionListener{
	

	private JLabel menTitle;
	private JLabel subTitle;
	private JLabel menLine1;
	private JLabel menLine2;
	private JLabel menLine3;
	private JLabel menLine4;
	private JLabel menLine5;
	private JLabel menLine6;
	
	private JFrame frame;
	private JPanel panel;
	private String username;
	
	private JButton bDisplay;
	private JButton bChange;
	private JButton bAdd;
	private JButton bRemove;
	private JButton bExit;

	
	private User user;
	private String userPerm;
	private String name;
	private boolean connected;
	
	private enum menCh {
		DISPLAY,
		ADD,
		CHANGE,
		REMOVE,
		EXIT
	}
	
	public mainMenuGUI(boolean tCon, String usr) {

		connected = tCon;
		username = usr;

		frame = new JFrame();
		panel = new JPanel();

		
		frame.setSize(750, 350);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(panel);
		panel.setLayout(null);

		if(connected == true) {
			user = serverConnector.getUser(username);
	    	userPerm = user.getUserPrivilege();
	    	name = user.getUserName();
		} else {
			name = driver.userFullNameReturner(username);
			userPerm = driver.userPermReturner(username);
		}
    
		
		menTitle = new JLabel("Welcome to the Investment System " + name );
		menTitle.setBounds(10, 10, 350, 25);
		panel.add(menTitle);
		
		subTitle = new JLabel("Your current permission level is " + userPerm);
		subTitle.setBounds(10, 35, 350, 25);
		panel.add(subTitle);

		
		menLine1 = new JLabel("What would you like to do?");
		menLine1.setBounds(10, 85,200,25);
		panel.add(menLine1);
		
		
		menLine2 = new JLabel("DISPLAY the client list");
		menLine2.setBounds(10,110, 200, 25);
		panel.add(menLine2);
		
		bDisplay = new JButton("DISPLAY");
		bDisplay.setBounds(215, 110, 105, 25);
		bDisplay.setActionCommand(menCh.DISPLAY.name());
		bDisplay.addActionListener(this);
		panel.add(bDisplay);
		
		menLine3 = new JLabel("CHANGE a client's choice");
		menLine3.setBounds(10, 135, 200, 25);
		panel.add(menLine3);
		
		bChange = new JButton("CHANGE");
		bChange.setBounds(215, 135, 105, 25);
		bChange.setActionCommand(menCh.CHANGE.name());
		bChange.addActionListener(this);
		panel.add(bChange);
		
		
		menLine5 = new JLabel("Add a client");
		menLine5.setBounds(10, 160, 105, 25);
		panel.add(menLine5);
		
		
		bAdd = new JButton("ADD");
		bAdd.setBounds(215, 160, 105, 25);
		bAdd.setActionCommand(menCh.ADD.name());
		bAdd.addActionListener(this);
		panel.add(bAdd);
		
		menLine6 = new JLabel("Remove a client");
		menLine6.setBounds(10, 185, 105, 25);
		panel.add(menLine6);
		
		bRemove = new JButton("REMOVE");
		bRemove.setBounds(215, 185, 105, 25);
		bRemove.setActionCommand(menCh.REMOVE.name());
		bRemove.addActionListener(this);
		panel.add(bRemove);
		
		menLine4 = new JLabel("EXIT the program");
		menLine4.setBounds(10, 210, 200, 25);
		panel.add(menLine4);
		
		bExit = new JButton("EXIT");
		bExit.setBounds(215, 210, 105, 25);
		bExit.setActionCommand(menCh.EXIT.name());
		bExit.addActionListener(this);
		panel.add(bExit);
		
		

		


		frame.setTitle("Investment Management System - Main Menu");
		//frame.pack();
		
		frame.setVisible(true);
	}

	//buttons are to DISPLAY, CHANGE, ADD, REMOVE, EXIT
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getActionCommand() == menCh.DISPLAY.name()) {

    		displayClientsGUI displayGUI = new displayClientsGUI(connected, username);
    		frame.dispose();
    		
		} else if (e.getActionCommand() == menCh.CHANGE.name()) {
			if(userPerm.equals("Admin") || userPerm.equals("Employee")) {
				changeClientsGUI changeGUI = new changeClientsGUI(connected, username);
	    		frame.dispose();

			} else {
				menTitle.setText("You do not have permission to change a customers choice");
				menTitle.setForeground(Color.red);
			}
			
		} else if(e.getActionCommand() == menCh.ADD.name()) {
			if(userPerm.equals("Admin") || userPerm.equals("Employee")) {
				addClientGUI addGUI = new addClientGUI(connected, username);
	    		frame.dispose();
	    		

			} else {
				menTitle.setText("You do not have permission to add a customers");
				menTitle.setForeground(Color.red);
			}
		} else if(e.getActionCommand() == menCh.REMOVE.name()) {
			if(userPerm.equals("Admin")) {
				removeClientGUI removeGUI = new removeClientGUI(connected, username);
	    		frame.dispose();

			} else {
				menTitle.setText("You do not have permission to remove a customer");
				menTitle.setForeground(Color.red);
			}
		} else if (e.getActionCommand() == menCh.EXIT.name()) {
			frame.dispose();
			System.exit(0);
		}
		
		

	}

}
