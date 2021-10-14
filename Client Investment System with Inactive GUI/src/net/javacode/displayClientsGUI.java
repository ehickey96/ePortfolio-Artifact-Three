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

public class displayClientsGUI implements ActionListener{
	
	private String username;
	private boolean connected;
	private String display;
	
    private static ArrayList<Client> clientListGUI;

    private JLabel displayLabel;
    private JLabel subDisplay;
    private JTextArea bigDisplay;
	private JFrame frame;
	private JPanel panel;
	private JButton bReturn;
	
	public displayClientsGUI(boolean tCon, String tUsr) {
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
				clientListGUI.clear();
				clientListGUI.addAll(serverConnector.getAllClients());

			} else {
				displayLabel.setText("Server Client List is empty!");
				displayLabel.setForeground(Color.red);
			}
		} else {
			
			clientListGUI.clear();
			clientListGUI.addAll(driver.returnClientsToGUI());
			
		}
		String temp = "";
		for(int i = 0; i < clientListGUI.size(); i++) {
			temp = clientListGUI.get(i).printClientGUI();
			display = display.concat(temp);
			display = display.concat("\r\n");
		}

		

		bigDisplay = new JTextArea(display);
		bigDisplay.setBounds(10, 80, 300, 275);
		bigDisplay.setEditable(false);
		panel.add(bigDisplay);

		bReturn = new JButton("RETURN");
		bReturn.setBounds(10, 375, 85, 25);
		bReturn.addActionListener(this);
		panel.add(bReturn);


		frame.setTitle("Investment Management System - Display Clients");
		//frame.pack();
		
		frame.setVisible(true);
		
	}

	//only one button, returns to main menu
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		mainMenuGUI mmPage = new mainMenuGUI(connected, username);
		frame.dispose();
	}

	
}
