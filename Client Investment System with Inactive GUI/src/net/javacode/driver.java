//CS-499 Category Three - Driver
//Elijah Hickey
//9.27.21

package net.javacode;

import java.util.ArrayList;
import java.net.URISyntaxException;
import java.util.Scanner;
import java.util.Collections;
import java.io.Console;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.concurrent.TimeUnit;


public class driver {
	
	//Final ver for spelling sake
	//Program does not allow user to enter these vals, this is done to ensure they are entered correct
	//They must be corrected to be recognized by the system
	public static final String SC_BROK = "Brokerage";
	public static final String SC_RETI = "Retirement";
	
	public static final String SC_ADM = "Admin";
	public static final String SC_EMP = "Employee";
	public static final String SC_INT = "Intern";
	
    private static ArrayList<Client> clientList = new ArrayList();
    private static ArrayList<User> userList = new ArrayList();

    
    // *******************************************************************************************************************************************
    // *******************************************************************************************************************************************
    // *************************************                          G U I                              *****************************************
    // *******************************************************************************************************************************************
    // *******************************************************************************************************************************************
    
    private static final String GUICONF = "--"; // needs to be CONFIRM to start as GUI, else will start as just a console app
    //private static final String GUICONF = "CONFIRM";
    
    // *******************************************************************************************************************************************
    // *******************************************************************************************************************************************
    // *******************************************************************************************************************************************
    // *******************************************************************************************************************************************
    
    
    
    //Function to add a mock list to the client arraylist
    //Will later incorporate a database instead of a mock list
	public static void initializeClientList() {
		Client newClient1 = new Client(1, "Bob Jones", SC_BROK);
		clientList.add(newClient1);
		
		Client newClient2 = new Client(2, "Sarah Davis", SC_RETI);
		clientList.add(newClient2);
		
		Client newClient3 = new Client(3, "Amy Friendly", SC_BROK);
		clientList.add(newClient3);
		
		Client newClient4 = new Client(4, "Johnny Smith", SC_BROK);
		clientList.add(newClient4);
		
		Client newClient5 = new Client(5, "Carol Spears", SC_RETI);
		clientList.add(newClient5);
		
		//a unique client only found in HC data
		//mysql DB has a client named SQL Client with Brokerage
		Client newClient6 = new Client(6, "Tony Hawk", SC_BROK);
		clientList.add(newClient6);
	}
	
	//like the client list, adds a mock user list to the user array list
	//Will also later be incorporated to the database
	public static void initializeUserList() {
		User newUser1 = new User(1, "Elijah Hickey", "snhuEhick", "4255", SC_ADM);
		userList.add(newUser1);
		
		User newUser2 = new User(2, "Bob Ross", "HappyClouds", "200", SC_EMP);
		userList.add(newUser2);
		
		User newUser3 = new User(3, "Jane Doe", "unknown person", "0", SC_INT);
		userList.add(newUser3);
		
		
		User newUser4 = new User(4, "Mock User", "hcUser", "hcPWD", SC_ADM);
		userList.add(newUser4);
	}
	
	 
	
	
    //Method to ensure that both the username & password match a username/password found within the userList
	//if from the console, uses just the connection val (to see if there is a connection to the server) 
	//GUI val states if the call came from the GUI section of the program or the console
	//gui ver is passed a username & pwd
	public static String CheckUserPermissionAccess(String usr, String pwd, Boolean tConnection, Boolean tGUI) {
		boolean connection = tConnection; 
		boolean fromGUI = tGUI;
		
		String username;
		String password;
		
    	Scanner scnr = new Scanner(System.in);

		if(fromGUI == true) {
			username = usr;
			password = pwd;
		} else {


			System.out.println("Enter your username");
			username = scnr.nextLine();
			System.out.println("Enter your password");
			password = scnr.nextLine();
		}
		
		boolean match = false;
		boolean partMatch = false;




		if(connection == true) {
			
			//this runs a query for a valid username/password combo
			if(serverConnector.validateUser(username, password) != null) {
				match = true;

				try {
					loginRecorder("Successful", username, password);
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println(e.getMessage());
				}
				

				
				//try runs a queury for just a valid username, if a match that means username was valid but password wasnt
			} else if(serverConnector.validateUserNoPass(username, password) != null) {
				try {
					loginRecorder("Partial", username, password);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println(e.getMessage());
				}

				
				//no matching username
			} else {
				try {
					loginRecorder("None", username, password);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println(e.getMessage());
				}
			}
			if(match == true) {
				return username;
			}
			return null;
		}
	
		//will pass to login record the type of login (sucessful, partial or none,) and the username/password to be logged
		//Currently could have some duplicate entries sent to login recording if there are different users with the same username (however this should never be allowed)
		for(int i = 0; i < userList.size(); i++) {
			if(  (username.equals(userList.get(i).getUserUsername()))  && password.equals(userList.get(i).getUserPassword())) {
				//This prints the matching usernames and passwords, used for testing
				
				
				 //				System.out.println("UN: " + username + " matches DB UN: " + userList.get(i).getUserUsername()
				//+ "\r\nPWD: " + password + " matches DB PWD: " + userList.get(i).getUserPassword());
				 

				match = true;
				try {
					loginRecorder("Successful",username, password);
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
				
				
				
			} else if(  (username.equals(userList.get(i).getUserUsername()))  && !password.equals(userList.get(i).getUserPassword())) {
				
				//This also prints a message, stating the matched usernames, but the mismatched passwords, used for testing
				
				 		//		System.out.println("UN: " + username + " matches DB UN: " + userList.get(i).getUserUsername()
			//	+ "\r\nPWD: " + password + " DOES NOT match DB PWD: " + userList.get(i).getUserPassword());
				
				 

				
				partMatch = true;
				try {
					loginRecorder("Partial",username, password);
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
				
			}
		}
		if(match != true && partMatch != true) {
			try {
				loginRecorder("None",username, password);
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
		if(match == true) {
			return username;
		}

		return null;
	}
	
	//records logins by type to a file, documenting if the login failed or succeeded, and what username and password was used.
	//Will create the text file if not made, if not will simply add to it
	//Obviously, a full application should never record login info like this, was done as a proof of concept 
	public static void loginRecorder(String recordType, String usr, String pwd) throws IOException {
		String fileName = "LoginRecorderDoc.txt";

		String type = recordType;
		
		String cens = "*****"; //censored pwd should also not indicated the length, so fixed length here
		
		
		//used pwd if wanting to display full password, use cens if wanting a censored password
		
		//Currently will only censor the password found within a successful login
		if(type== "Successful") {
			try {
				WriteToFile data = new WriteToFile( fileName , true );
				data.writeToFile("Successful Login by user: " + usr + " with password: " + cens );
			}
			catch(IOException e) {
				System.out.println(e.getMessage());
			}
		} else if (type == "Partial") {
			try {
				WriteToFile data = new WriteToFile( fileName , true );
				data.writeToFile("Failed Login (Incorrect Password) User: " + usr + " with password: " + pwd );
			}
			catch(IOException e) {
				System.out.println(e.getMessage());
			}
		} else if (type == "None") {
			try {
				WriteToFile data = new WriteToFile( fileName , true );
				data.writeToFile("Failed Login (Incorrect Username) User: " + usr + " with password: " + pwd );
			}
			catch(IOException e) {
				System.out.println(e.getMessage());
			}
			
		} else {
			System.out.println("Login Record System Failure. Contact Admin");
			//System.exit(0);
		}
		
	

	}
	
	//simple grab of client list (hardcoded)
	//used by GUI section of the program
	public static ArrayList returnClientsToGUI() {
		return clientList;
		
	}
	
	//refreshing the clientList arrayList with updated client list from the database
	public static void clientListUpdater() {
		clientList.clear();
		clientList.addAll(serverConnector.getAllClients());
	}


	//method to simply print all clients from the client array list
	public static void DisplayInfo(Boolean tConnection) {
		boolean connection = tConnection; 


		if(connection == true) {
			if(serverConnector.getAllClients() != null) {
				clientListUpdater();

			}
			else {
				System.out.println("Server Client List is empty!!");
				return;
			}
		}
		
		if (clientList.size() < 1) {
			System.out.println("\nThere are no clients to display.");
			return;
		}
		System.out.println("\r\n   Client's Name     Service Selected ");

		for(Client eachClient: clientList)
			eachClient.printClientData();
		
	}
	
	//method that will ask the user for the clients number, then the clients new choice. 
	//Will validate for proper data type and ensure its within the expected range.
	public static void ChangeCustomerChoice(Boolean tConnection) {
		boolean connection = tConnection; 

    	Scanner scnr = new Scanner(System.in);
		int changechoice, newservice, cycled;
		cycled = 0;
		changechoice = 0;
		newservice = 0;
		
		//updates the client list array if connected to the db, then shows the clients
		if(connection == true) {
			clientListUpdater();
		}
		
		DisplayInfo(connection);
		
		//gets the client number & choice. validates both
		System.out.println("\r\nEnter the number of the client that you wish to change");
		
		do {
    		if( (changechoice < 1 || changechoice > clientList.size()) && cycled != 0) {
    			System.out.println("That client does not exist. Try again.");
    		}
		    while(!scnr.hasNextInt()) {
		    	System.out.println("Client change options are integers only");
		    	scnr.next();	
		    }
    		cycled = 1;
    		changechoice = scnr.nextInt();
		} while(changechoice < 1 || changechoice > clientList.size());
		
		System.out.println("\r\nEnter the client's new service choice (1 = Brokerage, 2 = Retirement)");
		
		cycled = 0;
		
		do {
			if((newservice < 1 || newservice > 2) && cycled != 0) {
				System.out.println("Client choice can only be 1 or 2 (1 = Brokerage, 2 = Retirement)\r\nTry again.");

			}
			
			while(!scnr.hasNextInt()) {
				System.out.println("Client choice are integer only, Try again.");
				scnr.next();
			}
			cycled = 1;
			newservice = scnr.nextInt();
		} while(newservice < 1 || newservice > 2);
		
		String servString = "";
		if(newservice == 1) {
			servString = SC_BROK;
		} else if(newservice == 2) {
			servString = SC_RETI;
		} else {
			System.out.println("Error: Client could not be entered");
			return;
		}

		if(connection == true) {
			serverConnector.updateClientChoice(changechoice, servString);
			clientListUpdater();

		} else {
			clientList.get(changechoice-1).setClientServ(servString);

		}
		
		
		
	}
	
	
	//method that is passed connection status, gui status, and the name/choice. 
	//if from console, the user must enter the name/choice
	public static boolean AddCustomer(Boolean tConnection, Boolean tGUI, String cName, String cChoice ) {
		Boolean connected = tConnection;
		Boolean GUI = tGUI;
		String name = cName;
		String choice = cChoice;
		Boolean success = false;
		int cycled = 0;

		int userCh = 0;
		Scanner scnr = new Scanner(System.in);
		
		int sizeBefore = clientList.size();
		int id = clientList.size();

		//GUI get & set 
		if(GUI == true) {
			if(connected == true) {
				if(serverConnector.addClient(name, choice) == true) {
					success = true;
					clientListUpdater();

				} else {
					System.out.println("Could not add client to server via GUI");
					success = false;
				}
			} else {
				Client newClient = new Client(id + 1, name, choice);
				clientList.add(newClient);
				success = true;
				
			}
			
			
			
		} else if (GUI == false) { //get client info from user via console (GUI was not used)

			
			System.out.println("Enter the clients name");
			name = scnr.nextLine();
			
			if(name.equals("")) {
				System.out.println("Name cannot be empty! Returning to menu");
				success = false;
				return success;
			}
			System.out.println("Enter the clients service choice (1 for Brokerage, 2 for Retirement)");
			cycled = 0;
			do {
				if((userCh < 1 || userCh > 2) && cycled != 0) {
					System.out.println("Client choice can only be 1 or 2 (1 = Brokerage, 2 = Retirement)\r\nTry again.");

				}
				
				while(!scnr.hasNextInt()) {
					System.out.println("Client choice are integer only, Try again.");
					scnr.next();
				}
				cycled = 1;
				userCh = scnr.nextInt();
			} while(userCh < 1 || userCh > 2);
			
			if(userCh == 1) {
				choice = SC_BROK;
				
			} else if(userCh == 2) {
				choice = SC_RETI;
			} else {
				System.out.println("Unknown error checking choice range. Returning to menu.");
				success = false;
				return success;
			}
			
			//adds to MySQL database or mock Database
			if(connected == true) {
				if(serverConnector.addClient(name, choice) == true) {
					success = true;
					clientListUpdater();
				} else {
					System.out.println("Could not add client to the Mock DB via Console");
					success = false;
					return success;
				}
			} else {
				Client newClient = new Client(id + 1, name, choice);
				clientList.add(newClient);
				success = true;
			}
		} else {
			System.out.println("GUI Status error. GUI status was: " + GUI);
			success = false;
			return success;
		}
		
		if(connected == false) {
			if((sizeBefore + 1) != clientList.size()){
				success = false;
				System.out.println("Unknown add error POST. ");
			}
		}


		if(success == true && GUI == false) {
			System.out.println("Client " + clientList.get(id).getClientNam() +  " added successfully.");
		}
		return success;
	}
	
	//this is passed user id, and connection status
	//it will be passed -1 as the id if it comes from the console app, indicating the client id must be collected from the user
	
	public static boolean RemoveCustomer(Boolean tConnection, int tId) {
	    Boolean success = false;
		Boolean connected = tConnection;
    	Scanner scnr = new Scanner(System.in);
    	int cycled = 0;
    	int clientIdOld = 0;
    	int clientSizeBfr = clientList.size();
		if(connected == true) {
			if(serverConnector.updateAutoInc()!= true) {
				System.out.println("Update ID increments failed");
			} else {
				clientListUpdater();

			}
		}
		
		DisplayInfo(connected);
		
		int clientId = tId;
		
		//came from console ver of app, user must enter values
		if(clientId == -1) {
			
			System.out.println("Enter client's ID");
			do {
	    		if( (clientId < 1 || clientId > clientList.size()) && cycled != 0) {
	    			System.out.println("That client does not exist. Try again.");
	    		}
			    while(!scnr.hasNextInt()) {
			    	System.out.println("Client change options are integers only");
			    	scnr.next();	
			    }
	    		cycled = 1;
	    		clientId = scnr.nextInt();
			} while(clientId < 1 || clientId > clientList.size());
			
		}
		
		//if client id is not -1, it came from GUI
		if(connected == true) {
			if(serverConnector.removeClient(clientId) == true) {
				clientListUpdater();
				if(serverConnector.updateAutoInc() == true) {
					success = true;
				} else {
					success = false;
					System.out.println("Server auto inc updater error. Client may have been removed.");
					return success;
				}

			} else {
				System.out.println("System could not verified client was removed. Returning to menu");
				success = false;
				return success;
			}

		} else {
			clientIdOld = clientId - 1;
			clientList.remove(clientId - 1);
			for(int i = clientIdOld; i < clientList.size(); i++) {
				clientList.get(i).setClientNum(
						(clientList.get(i).getClientNum() - 1 )
						);
				
			}
			if((clientSizeBfr-1) != clientList.size()) {
				System.out.println("Unknown error. Could not validate client was removed Post. Returning to menu");
				success = false;
				return success;
			}
			success = true;
		}
		if(success == true) {
			System.out.println("Client removed successfully");
		}
		return success;
	}

	//this is a seperate method to handle changing customer choice from just the GUI
	//The GUI was added after, so it was easier to make a seprate method
	public static boolean ChangeCustomerChoiceGUI(Boolean tConnection, int tId, String tChoice) {
		Boolean connected = tConnection;
		int clientID = tId;
		String clientChoice = tChoice;
		Boolean success = false;
		if(connected == true) {
			if(serverConnector.updateClientChoice(clientID, clientChoice)) {
				clientListUpdater();
				success = true;

			}
		} else {
			
			clientList.get(clientID-1).setClientServ(clientChoice);

			success = true;
		}
		
		return success;
		
	}

	
	//returns the full name of the user if passed the username
	//returns "" if user not found
	public static String userFullNameReturner(String tUsr) {
		String fullName = "";
		String username = tUsr;
	
		for(int i = 0; i < userList.size(); i++) {
			if(userList.get(i).getUserUsername().equals(username)) {
				fullName = userList.get(i).getUserName();
			}
		}
		return fullName;
	}
	//returns the perm of the user if passed the username
	//returns "" if user not found
	public static String userPermReturner(String tUsr) {
		String perm = "";
		String username = tUsr;
	
		for(int i = 0; i < userList.size(); i++) {
			if(userList.get(i).getUserUsername().equals(username)) {
				perm = userList.get(i).getUserPrivilege();
			}
		}
		return perm;
	}


	//this is to check to see if there is a connection to the db
	public static boolean testConnection() {
		boolean successfullyConnected = false;
		
		try {
			serverConnector.getConnection();
			System.out.println("Successfully connected to the Investment Database");
			successfullyConnected = true;

		}catch (SQLException e) {
			System.out.println("Investment Database could no be loaded: Mock Database loaded instead.");
			successfullyConnected = false;
			
			e.printStackTrace();
		}
		
		return successfullyConnected;
		
	}

	
	public static void main(String[] args) {
		boolean connectionVal = false; 
		String connect = "Y";
		boolean cycled = false;
    	Scanner scnr = new Scanner(System.in);
    	
		int choice;
		boolean unPwdCheck = true;
		String username = "";
		User loggedInUser;
		String loggedPermission = "";
		String name = "";
		
		
		initializeClientList();
		initializeUserList();
		
		//runs the GUI version of app
		if(GUICONF.equals("CONFIRM")) {
			new GUI();
			
		//this is the normal console app	
		} else {

			//lets the user choose if they want to use DB or mock DB (hardcoded data)
			
			do {
				if( ((!connect.equals("Y")) && (!connect.equals("N")) ) && cycled == true) {
					System.out.println("You did not enter Y or N.");
				}
				
				System.out.println("Attempt to connect to Investment Database? Y/N");
				
				cycled = true;
				connect = scnr.next().toUpperCase();
				scnr.nextLine();
				
			} while( (!connect.equals("Y")) && (!connect.equals("N")) );
			

			//attempts to connect to db. if cant runs mock DB;
			if(connect.equals("Y")) {

				try {
					serverConnector.getConnection();
					System.out.println("Successfully connected to the Investment Database");
					connectionVal = true;

				}catch (SQLException e) {
					System.out.println("Investment Database could no be loaded: Mock Database loaded instead.");
					connectionVal = false;
					
					e.printStackTrace();
				}
			} else {
				connectionVal = false;
			}
		
			
			//added this since there was a delay with displaying the exception in the case of unable to connect to db
			
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//originally here
			//initializeClientList();
			//initializeUserList();
			System.out.println(" -------------------------------------------------------------------"
					+ "\r\n|       CS-499, Category Three -- Software Engineering & Design     |"
					+ "\r\n|                    Created by Elijah Hickey                       |"
					+ "\r\n -------------------------------------------------------------------\r\n");
			
	    	System.out.println("Hello! Welcome to our investment Company\n");
	    	
	    	
	    	if((username = CheckUserPermissionAccess("","",connectionVal, false)) == null) {
	    		unPwdCheck = false;
	    	} 
	    	
	    	while(unPwdCheck != true) {
	    		System.out.println("Invalid Username/Password. Please try again");
	    		if((username = CheckUserPermissionAccess("", "",connectionVal, false)) == null) {
	        		unPwdCheck = false;
	        	} else {
	        		unPwdCheck = true;
	        	}
	        	
	        	
	    	}
	    	if(connectionVal == true) {
	        	loggedInUser = serverConnector.getUser(username);
	        	loggedPermission = loggedInUser.getUserPrivilege();
	        	name = loggedInUser.getUserName();

	    	} else {
	    		for(int i = 0; i < userList.size(); i++) {
	    			if(userList.get(i).getUserUsername().equals(username)) {
	    				loggedPermission = userList.get(i).getUserPrivilege();
	    				name = userList.get(i).getUserName();
	    			}
	    		}
	    	}
	    	
	    	choice = 0;
	    	

	    	
	    	System.out.println("Welcome to the Investment System " + name + " your current permission level is " + loggedPermission);
	    	
	    	//looping menu system, will either call the users choice, exit program, or prompt user to enter a valid entry
	    	while(choice != 5) {
	    		
	    		System.out.println("\r\nWhat would you like to do?"
	    				+ "\r\nDISPLAY the client list (enter 1)"
	    				+ "\r\nCHANGE a client's choice (enter 2)"
	    				+ "\r\nADD a client (enter 3)"
	    				+ "\r\nREMOVE a client (enter 4)"
	    				+ "\r\nEXIT the program.. (enter 5)");
	    		cycled = false;
	    		
	    		do {
	        		if((choice < 1 || choice > 5) && cycled == true) {
	        			System.out.println("Menu options cannot be less than 1 or greater than 5. Try again.");
	        		}
	    		    while(!scnr.hasNextInt()) {
	    		    	System.out.println("Menu options are integers only");
	    		    	scnr.next();	
	    		    }
		    		cycled = true;
	    		    choice = scnr.nextInt();
	    		} while(choice < 1 || choice > 5);
	    		
	    		
	    		System.out.println("\r\nYou choose " + choice );
	    		
	    		if(choice == 1) {
	    			DisplayInfo(connectionVal);
	    		}
	    		//intern users cannot change client choice
	    		else if (choice == 2) {
	    			if(loggedPermission.equals(SC_ADM) || loggedPermission.equals(SC_EMP)) {
	        			ChangeCustomerChoice(connectionVal);

	    			} else if (loggedPermission.equals(SC_INT)) {
	    				System.out.println("You do not have permission to change a client's choice");
	    			} else {
	    				System.out.println("User Permission Error. Permission read as: " + loggedPermission);
	    	    		return;
	    			}
	    			
	    		} else if (choice == 3) {
	    			if(loggedPermission.equals(SC_ADM) || loggedPermission.equals(SC_EMP)) {
	        			AddCustomer(connectionVal, false, "", "");

	    			} else if(loggedPermission.equals(SC_INT)) {
	    				System.out.println("You do not have permission to add a client");
	    			} else {
	    				System.out.println("User Permission Error. Permission read as: " + loggedPermission);
	    				return;
	    			}
	    			
	    		} else if(choice == 4) {
	    			if(loggedPermission.equals(SC_ADM)) {
	    				RemoveCustomer(connectionVal, -1);
	    			} else {
	    				System.out.println("You do not have permission to change a client's choice");
	    			}
	    		}
	    		
	    	}
	    	
			scnr.close();
	    	System.out.println("Goodbye");
	    	return;
	    }

		
	}
		
}


