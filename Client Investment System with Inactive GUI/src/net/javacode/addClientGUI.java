
package net.javacode;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;



public class addClientGUI implements ActionListener{
	
	public static enum AType{
		ADD,
		RETURN,
		BROKERAGE,
		RETIREMENT		
	}
	private JLabel subTitle;
	private JLabel addTitle;
	private JLabel clientNameLabel;
	private JLabel clientServiceLabel;
	private JTextField clientNameField;
	private JRadioButton brokerageRadio;
	private JRadioButton retirementRadio;
	private JFrame frame;
	private JPanel panel;
	private String username;
	private JButton buttonAdd;
	private JButton buttonReturn;
	private boolean connected;
	private ButtonGroup groupX;
	
	private String clientNameFinal;
	private String clientService;

	public addClientGUI(boolean tCon, String tUsr) {

		clientService = "";
		connected = tCon;
		username = tUsr;
		
		frame = new JFrame();
		panel = new JPanel();
		

		frame.setSize(750, 350);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(panel);
		
		panel.setLayout(null);

		subTitle = new JLabel();
		subTitle.setText("");
		subTitle.setBounds(10,40,550,25);
		panel.add(subTitle);
		

		
		addTitle = new JLabel("Enter Client Info");
		addTitle.setBounds(10, 10, 550, 25);
		panel.add(addTitle);

		clientNameLabel = new JLabel("Client Name");
		clientNameLabel.setBounds(70, 75, 75, 25);
		panel.add(clientNameLabel);
		
		clientNameField = new JTextField();
		clientNameField.setBounds(10, 105, 200, 25 );
		panel.add(clientNameField);
		
		clientServiceLabel = new JLabel("Client Service");
		clientServiceLabel.setBounds(70, 145, 105, 25);
		panel.add(clientServiceLabel);
		groupX = new ButtonGroup();

		brokerageRadio = new JRadioButton("Brokerage");
		brokerageRadio.setBounds(25, 170, 85, 25);
		brokerageRadio.setActionCommand(AType.BROKERAGE.name());
		brokerageRadio.addActionListener(this);
		brokerageRadio.setSelected(true);
		groupX.add(brokerageRadio);
		panel.add(brokerageRadio);
		
		retirementRadio = new JRadioButton("Retirement");
		retirementRadio.setBounds(110, 170, 105, 25);
		retirementRadio.setActionCommand(AType.RETIREMENT.name());
		retirementRadio.addActionListener(this);
		groupX.add(retirementRadio);
		panel.add(retirementRadio);

		buttonAdd = new JButton("Add Client");
		buttonAdd.setBounds(20, 210, 95, 55);
		buttonAdd.addActionListener(this);
		buttonAdd.setActionCommand(AType.ADD.name());
		panel.add(buttonAdd);

		buttonReturn = new JButton("Return");
		buttonReturn.setBounds(115, 210, 95, 55);
		buttonReturn.addActionListener(this);
		buttonReturn.setActionCommand(AType.RETURN.name());
		panel.add(buttonReturn);
		
		frame.setTitle("Investment Management System - Add Client");
		frame.setVisible(true);
	}

	
	@Override
	public void actionPerformed(ActionEvent e) {
		//returns to the main menu
		if(e.getActionCommand() == AType.RETURN.name()) {
	   		mainMenuGUI maPage = new mainMenuGUI(connected, username);
    		frame.dispose();
    		//seems like this was not needed since we can check what the radio is set to when the ADD button is pressed
		} else if(e.getActionCommand() == AType.BROKERAGE.name()) {
			clientService = "Brokerage";
		} else if(e.getActionCommand() == AType.RETIREMENT.name()) {
			clientService = "Retirement";
		} else if(e.getActionCommand() == AType.ADD.name()) {

			clientNameFinal = "";
			clientNameFinal = clientNameField.getText();
			subTitle.setText("");
			
			if(brokerageRadio.isSelected()) {
				clientService = "Brokerage";
			} else if(retirementRadio.isSelected()) {
				clientService = "Retirement";
			}
			
			//various validations to see if name is entered or client service is entered
			if(clientNameFinal.equals("")){
				subTitle.setText("Client Name cannot be blank!");
				subTitle.setForeground(Color.red);
				clientNameField.setText("");
				return;
				
			}
			if(clientService.equals("")) {
				subTitle.setText("Client Service Radio Error! ");
				subTitle.setForeground(Color.red);
				clientNameField.setText("");
				return;
			}
			
			if(connected == true) {
				if((serverConnector.addClient(clientNameFinal, clientService)) == false ) {
					subTitle.setText("Client could not be added! Server Connector Error!" );
					subTitle.setForeground(Color.red);
				} else {
					subTitle.setForeground(Color.black);
					subTitle.setText("Client " + clientNameFinal + " added successfully");
				}

			} else {
				if((driver.AddCustomer(connected, true, clientNameFinal, clientService)) == false ) {
					subTitle.setText("Client could not be added! Mock DB Error!" );
					subTitle.setForeground(Color.red);
				} else {
					subTitle.setForeground(Color.black);
					subTitle.setText("Client " + clientNameFinal + " added successfully");
				}
				
			}		
		}	
	}
}
