

package net.javacode;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class changeClientsGUI implements ActionListener{
	
	private String username;
	private boolean connected;
	private String display;
	

	private enum ChangeCh{
		RETURN, 
		ENTER
	}
    private static ArrayList<Client> clientListGUI;

    private JLabel displayLabel;
    private JLabel subDisplay;
    private JTextArea bigDisplay;
	private JFrame frame;
	private JPanel panel;
	private JButton bReturn;
	private JButton bEnter;
	
	private JTextField clientNumField;
	private JLabel clientNumLabel;
	
	private JTextField clientChoiceField;
	private JLabel clientChoiceLabel;
	
	private String clientChoiceRaw;
	private String clientNumRaw;
	
	private int clientChoiceInt;
	private int clientNumInt;
	private int clientListSize;
	
	private String clientChoiceString;
	
	
	public changeClientsGUI(boolean tCon, String tUsr) {
		username = tUsr;
		connected = tCon;
	    clientListGUI = new ArrayList();

	    display = "";
		frame = new JFrame();
		panel = new JPanel();
		

		
		frame.setSize(750, 450);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(panel);
		panel.setLayout(null);
		
	    displayLabel = new JLabel("Client Change Service System");
	    displayLabel.setBounds(10, 10, 350, 25);
		panel.add(displayLabel);
		
		subDisplay = new JLabel("   Client's Name       Service Selected ");
		subDisplay.setBounds(10, 35, 350, 25);
		panel.add(subDisplay);

		if(connected == true) {
			if(serverConnector.getAllClients() != null) {
				clientListGUI.clear();
				clientListGUI.addAll(serverConnector.getAllClients());
			}

			//loading from mock DB
		} else {
			
			clientListGUI.clear();
			clientListGUI.addAll(driver.returnClientsToGUI());
			
		}
			
		if(clientListGUI.size() < 1) {
			displayLabel.setText("Client List is empty!");
			display = "Client list is empty!";

		} else {
			String temp = "";
			for(int i = 0; i < clientListGUI.size(); i++) {
				temp = clientListGUI.get(i).printClientGUI();
				display = display.concat(temp);
				display = display.concat("\r\n");
			}

		}
		


		bigDisplay = new JTextArea(display);
		bigDisplay.setBounds(10, 80, 300, 275);
		bigDisplay.setEditable(false);
		panel.add(bigDisplay);


		
		clientNumLabel = new JLabel("Enter Client Number (integer only)");
		clientNumLabel.setBounds(395, 85, 300, 25);
		panel.add(clientNumLabel);

		
		clientNumField = new JTextField();
		clientNumField.setBounds(450, 115, 85, 25);
		panel.add(clientNumField);
		
		clientChoiceLabel = new JLabel("Enter Client Choice (1 for Brokerage, 2 for Retirement)");
		clientChoiceLabel.setBounds(350, 150, 400, 25);
		panel.add(clientChoiceLabel);
		
		clientChoiceField = new JTextField();
		clientChoiceField.setBounds(450, 180, 85, 25);
		panel.add(clientChoiceField);
		
		bEnter = new JButton("ENTER");
		bEnter.setBounds(450, 220, 85, 25);
		bEnter.setActionCommand(ChangeCh.ENTER.name());
		bEnter.addActionListener(this);
		panel.add(bEnter);
		
		bReturn = new JButton("RETURN");
		bReturn.setBounds(450, 255, 85, 25);
		bReturn.setActionCommand(ChangeCh.RETURN.name());
		bReturn.addActionListener(this);
		panel.add(bReturn);


		frame.setTitle("Investment Management System - Change Client Service");
		//frame.pack();
		
		frame.setVisible(true);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		clientChoiceInt = -1;
		clientNumInt = -1;
		clientChoiceString = "";
		if(e.getActionCommand() == ChangeCh.RETURN.name()) {
			mainMenuGUI mmPage = new mainMenuGUI(connected, username);
			frame.dispose();
		} else {
			clientChoiceRaw = clientChoiceField.getText();
			clientNumRaw = clientNumField.getText();
			clientListSize = clientListGUI.size();
			try {
				clientChoiceInt = Integer.valueOf(clientChoiceRaw);
			}catch(NumberFormatException e1) {
				clientChoiceLabel.setText("That was not an int, try again");
				clientChoiceLabel.setForeground(Color.red);
				clientChoiceField.setText("");
				clientNumField.setText("");
				System.out.println(e1.getMessage());
				return;
			}
			
			try {
				clientNumInt = Integer.valueOf(clientNumRaw);
			}catch(NumberFormatException e1) {
				clientNumLabel.setText("That was not an int, try again");
				clientNumLabel.setForeground(Color.red);
				clientChoiceField.setText("");
				clientNumField.setText("");
				System.out.println(e1.getMessage());
				return;
			}
			
			if(clientNumInt < 1 || clientNumInt > clientListGUI.size()) {
				clientNumLabel.setText("That client does not exist, try again");
				clientNumLabel.setForeground(Color.red);
				clientChoiceField.setText("");
				clientNumField.setText("");
				return;
				
			}
			if(clientChoiceInt < 1 || clientChoiceInt > 2) {
				clientNumLabel.setText("Client Choice must be 1 or 2");
				clientNumLabel.setForeground(Color.red);
				clientChoiceField.setText("");
				clientNumField.setText("");
				return;
			}
			
			if(clientChoiceInt == 1) {
				clientChoiceString = "Brokerage";
			} else if(clientChoiceInt == 2) {
				clientChoiceString = "Retirement";
			} else {
				clientChoiceLabel.setText("Client Num Error. Try Again");
				clientChoiceLabel.setForeground(Color.red);
				clientChoiceField.setText("");
				clientNumField.setText("");
				return;
			}
			
			if(driver.ChangeCustomerChoiceGUI(connected, clientNumInt, clientChoiceString) == true) {
				mainMenuGUI mmPage = new mainMenuGUI(connected, username);
				frame.dispose();
				
			} else {
				displayLabel.setText("Client could not be added! Try again");
				displayLabel.setForeground(Color.red);
				clientChoiceField.setText("");
				clientNumField.setText("");
				
			}
			

			
			
			
		}

	}

	
}
