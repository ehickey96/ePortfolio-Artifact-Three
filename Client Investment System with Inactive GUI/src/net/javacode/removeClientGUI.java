
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

public class removeClientGUI implements ActionListener{
	
	private String username;
	private boolean connected;
	private String display;
	

	private enum DeleteCh{
		RETURN, 
		DELETE
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
	
	private JTextField clientRemoveConfField;
	private JLabel clientRemoveConfLabel;
	
	private String clientNumRaw;
	private String clientConfRaw;
	
	private int clientNumInt;
	private int clientListSize;
	
	private String clientChoiceString;
	
	
	public removeClientGUI(boolean tCon, String tUsr) {
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
		
	    displayLabel = new JLabel("Client Info Display");
	    displayLabel.setBounds(10, 10, 350, 25);
		panel.add(displayLabel);
		
		subDisplay = new JLabel("   Client's Name       Service Selected ");
		subDisplay.setBounds(10, 35, 350, 25);
		panel.add(subDisplay);

		if(connected == true) {
			if(serverConnector.getAllClients() != null) {
				serverConnector.updateAutoInc();
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


		
		clientNumLabel = new JLabel("Enter Client Number you wish to delete (integer only)");
		clientNumLabel.setBounds(350, 85, 300, 25);
		panel.add(clientNumLabel);

		
		clientNumField = new JTextField();
		clientNumField.setBounds(450, 115, 85, 25);
		panel.add(clientNumField);
		
		clientRemoveConfLabel = new JLabel("Type CONFIRM if you wish to delete a client");
		clientRemoveConfLabel.setBounds(350, 150, 400, 25);
		panel.add(clientRemoveConfLabel);
		
		clientRemoveConfField = new JTextField();
		clientRemoveConfField.setBounds(450, 180, 85, 25);
		panel.add(clientRemoveConfField);
		
		bEnter = new JButton("DELETE");
		bEnter.setBounds(450, 220, 85, 25);
		bEnter.setActionCommand(DeleteCh.DELETE.name());
		bEnter.addActionListener(this);
		panel.add(bEnter);
		
		bReturn = new JButton("RETURN");
		bReturn.setBounds(450, 255, 85, 25);
		bReturn.setActionCommand(DeleteCh.RETURN.name());
		bReturn.addActionListener(this);
		panel.add(bReturn);


		frame.setTitle("Investment Management System - Delete Client");
		//frame.pack();
		
		frame.setVisible(true);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		clientNumInt = -1;
		
		if(e.getActionCommand() == DeleteCh.RETURN.name()) {
			mainMenuGUI rmPage = new mainMenuGUI(connected, username);
			frame.dispose();
			
		} else if(e.getActionCommand()== DeleteCh.DELETE.name()) {
			
			clientConfRaw = clientRemoveConfField.getText();
			clientNumRaw = clientNumField.getText();
			clientListSize = clientListGUI.size();
			
			try {
				clientNumInt = Integer.valueOf(clientNumRaw);
			}catch(NumberFormatException e1) {
				clientNumLabel.setText("That was not an int, try again");
				clientNumLabel.setForeground(Color.red);
				clientNumField.setText("");
				clientRemoveConfField.setText("");
				System.out.println(e1.getMessage());
				return;
			}

			
			if(clientNumInt < 1 || clientNumInt > clientListGUI.size()) {
				clientNumLabel.setText("That client does not exist, try again");
				clientNumLabel.setForeground(Color.red);
				clientRemoveConfField.setText("");
				clientNumField.setText("");
				return;
				
			}
			if(!clientConfRaw.equals("CONFIRM")) {
				clientRemoveConfField.setText("");
				clientNumField.setText("");
				clientRemoveConfLabel.setText("You must type confirm if you wish to delete a user");
				clientRemoveConfLabel.setForeground(Color.red);
				return;
			}

			

			
			if(driver.RemoveCustomer(connected, clientNumInt) == true) {
				mainMenuGUI mmPage = new mainMenuGUI(connected, username);
				frame.dispose();
				
			} else {
				displayLabel.setText("Client could not be added! Try again");
				displayLabel.setForeground(Color.red);
				clientRemoveConfField.setText("");
				clientNumField.setText("");
				
			}
			

			
			
			
		}

	}

	
}