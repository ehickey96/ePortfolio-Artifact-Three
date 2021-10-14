//CS-499 Category Three - Driver
//Elijah Hickey
//9.27.21

package net.javacode;

import java.util.ArrayList;
import java.net.URISyntaxException;
import java.util.Scanner;
import java.util.Collections;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;
import java.awt.GraphicsEnvironment;
import java.io.Console;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.concurrent.TimeUnit;

public class driver {
	
    private static ArrayList<Client> clientList = new ArrayList();
    private static ArrayList<User> userList = new ArrayList();

	
    //Function to add a mock list to the client arraylist
    //Will later incorporate a database instead of a mock list
	public static void initializeClientList() {
		Client newClient1 = new Client(1, "Bob Jones", "Brokerage");
		clientList.add(newClient1);
		
		Client newClient2 = new Client(2, "Sarah Davis", "Retirement");
		clientList.add(newClient2);
		
		Client newClient3 = new Client(3, "Amy Friendly", "Brokerage");
		clientList.add(newClient3);
		
		Client newClient4 = new Client(4, "Johnny Smith", "Brokerage");
		clientList.add(newClient4);
		
		Client newClient5 = new Client(5, "Carol Spears", "Retirement");
		clientList.add(newClient5);
	}
	
	//like the client list, adds a mock user list to the user array list
	//Will also later be incorporated to the database
	public static void initializeUserList() {
		User newUser1 = new User(1, "Elijah Hickey", "snhuEhick", "4255", "Admin");
		userList.add(newUser1);
		
		User newUser2 = new User(2, "Bob Ross",  "HappyClouds", "200", "Employee");
		userList.add(newUser2);
		
		User newUser3 = new User(3, "Jane Doe",  "unknown person", "0", "Intern");
		userList.add(newUser3);
	}
	
    
    //Method to ensure that both the username & password match a username/password found within the userList
	public static String CheckUserPermissionAccess(Boolean tConnection) {
		boolean connection = tConnection; 
		boolean match = false;
		boolean partMatch = false;
		String username;
		String password;
    	Scanner scnr = new Scanner(System.in);


		System.out.println("Enter your username");
		username = scnr.nextLine();
		System.out.println("Enter your password");
		password = scnr.nextLine();


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
				
				/*
				 * 				System.out.println("UN: " + username + " matches DB UN: " + userList.get(i).getUserUsername()
				+ "\r\nPWD: " + password + " matches DB PWD: " + userList.get(i).getUserPassword());
				 */

				match = true;
				try {
					loginRecorder("Successful",username, password);
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
				
				
				
			} else if(  (username.equals(userList.get(i).getUserUsername()))  && !password.equals(userList.get(i).getUserPassword())) {
				
				//This also prints a message, stating the matched usernames, but the mismatched passwords, used for testing
				/*
				 * 				System.out.println("UN: " + username + " matches DB UN: " + userList.get(i).getUserUsername()
				+ "\r\nPWD: " + password + " DOES NOT match DB PWD: " + userList.get(i).getUserPassword());
				
				 */

				
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
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
		LocalDateTime now = LocalDateTime.now();  
		String type = recordType;
		
		String cens = "*****"; //censored pwd should also not indicated the length, so fixed length here
		
		
		//used pwd if wanting to display full password, use cens if wanting a censored password
		
		//Currently will only censor the password found within a successful login
		if(type== "Successful") {
			try {
				WriteToFile data = new WriteToFile( fileName , true );
				data.writeToFile("Successful Login by user: " + usr + " with password: " + cens + " at " + dtf.format(now));
			}
			catch(IOException e) {
				System.out.println(e.getMessage());
			}
		} else if (type == "Partial") {
			try {
				WriteToFile data = new WriteToFile( fileName , true );
				data.writeToFile("Failed Login (Incorrect Password) User: " + usr + " with password: " + pwd + " at " + dtf.format(now));
			}
			catch(IOException e) {
				System.out.println(e.getMessage());
			}
		} else if (type == "None") {
			try {
				WriteToFile data = new WriteToFile( fileName , true );
				data.writeToFile("Failed Login (Incorrect Username) User: " + usr + " with password: " + pwd + " at " + dtf.format(now));
			}
			catch(IOException e) {
				System.out.println(e.getMessage());
			}
			
		} else {
			System.out.println("Login Record System Failure. Contact Admin");
			//System.exit(0);
		}
		
	

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
		
		if(connection == true) {
			clientListUpdater();
		}
		
		DisplayInfo(connection);
		
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
			servString = "Brokerage";
		} else if(newservice == 2) {
			servString = "Retirement";
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



	public static void main(String[] args) {
		//TODO force console window to appear 
		

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
		
		initializeClientList();
		initializeUserList();
		System.out.println(" -------------------------------------------------------------------"
				+ "\r\n|       CS-499, Category Three -- Software Engineering & Design     |"
				+ "\r\n|                    Created by Elijah Hickey                       |"
				+ "\r\n -------------------------------------------------------------------\r\n");
		
    	System.out.println("Hello! Welcome to our investment Company\n");
    	
    	
    	if((username = CheckUserPermissionAccess(connectionVal)) == null) {
    		unPwdCheck = false;
    	} 
    	
    	while(unPwdCheck != true) {
    		System.out.println("Invalid Username/Password. Please try again");
    		if((username = CheckUserPermissionAccess(connectionVal)) == null) {
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
    	while(choice != 3) {
    		
    		System.out.println("\r\nWhat would you like to do?"
    				+ "\r\nDISPLAY the client list (enter 1)"
    				+ "\r\nChange a client's choice (enter 2)"
    				+ "\r\nExit the program.. (enter 3)");
    		cycled = false;
    		
    		do {
        		if((choice < 1 || choice > 3) && cycled == true) {
        			System.out.println("Menu options cannot be less than 1 or greater than 3. Try again.");
        		}
    		    while(!scnr.hasNextInt()) {
    		    	System.out.println("Menu options are integers only");
    		    	scnr.next();	
    		    }
	    		cycled = true;
    		    choice = scnr.nextInt();
    		} while(choice < 1 || choice > 3);
    		
    		
    		System.out.println("\r\nYou chose " + choice );
    		
    		if(choice == 1) {
    			DisplayInfo(connectionVal);
    		}
    		//intern users cannot change client choice
    		else if (choice == 2) {
    			if(loggedPermission.equals("Admin") || loggedPermission.equals("Employee")) {
        			ChangeCustomerChoice(connectionVal);

    			} else if (loggedPermission.equals("Intern")) {
    				System.out.println("You do not have permission to change a client's choice");
    			} else {
    	    		System.out.println("User Permission Error: Closing");
    	    		return;
    			}
    			
    		}
    		
    	}
    	
		scnr.close();
    	System.out.println("Goodbye");
    	return;
    }

}


