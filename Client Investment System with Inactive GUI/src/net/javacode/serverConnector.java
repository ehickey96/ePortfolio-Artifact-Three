//CS-499 Category Three - ServerConnector
//Elijah Hickey
//9.27.21

package net.javacode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


//handles connecitons to the server
public class serverConnector {

	//hard code UN/PWD
    public static final String URL = "jdbc:mysql://localhost:3306/investmentDB";

    public static final String USERNAME = "investmentAdmin";
    public static final String PASSWORD = "499password";
	
    
    //Tests connection
    //useful for error prevention/added security incase server goes offline after initial verification
    public static Connection getConnection() throws SQLException {

		Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

		return connection;

    }
    
    //handles user result sets
    private static User resultsinterpreter(ResultSet tResults) throws SQLException{
    	ResultSet results = tResults;
    	User user = new User();
    	user.setUserId(results.getInt("id"));
    	user.setUserFullName(results.getString("name"));
    	user.setUserUsername(results.getString("username"));
    	user.setUserPassword(results.getString("password"));
    	user.setUserPrivilege(results.getString("privilege"));
    	return user;
    }
    
    //handles client result sets
    private static Client resultsinterpreterClient(ResultSet tResults) throws SQLException{
    	ResultSet results = tResults;
    	Client client = new Client();
    	client.setClientNum(results.getInt("id"));
    	client.setClientNam(results.getString("name"));
    	client.setClientServ(results.getString("service"));

    	return client;
    }
    
    //method to add a client (can be passed from GUI interface or Console interface
    public static boolean addClient(String cName, String cChoice) {
    	try {
    		Connection connection = getConnection();
    		try {
    			PreparedStatement statement = connection.prepareStatement("INSERT INTO clients VALUES (NULL, ?, ?)");
    			statement.setString(1,  cName);
    			statement.setString(2, cChoice);
    			int i = statement.executeUpdate();

    			
    			
    			if(i == 1) {
    				return true;
    			}
    			
    			
			}catch (SQLException e) {
				e.printStackTrace();
			}
			
    		return false;
    		
    	}catch(SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("Error: Connection lost after intitional connection test. Now Closeing.", e);
    	}
    }
    
    
    //function to get the user with just the username
    public static User getUser(String username) {
    	try {
    		Connection connection = getConnection();
    		try {
				PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE username=?");
    			statement.setString(1, username);
				ResultSet results = statement.executeQuery();
				
				if(results.next()) {
					return resultsinterpreter(results);
				}

    		} catch(SQLException e) {
				e.printStackTrace();

    		}
    		
			return null;

    		
    	} catch (SQLException e) {
    		throw new RuntimeException("Error: Connection lost after initial connection test. Now Closing.", e);
    	}
    }
    
    
    //Function to reinitialize the id vals in the mysql database. 
    //if there is clients with IDs 1, 2, and 3, and client 2 is deleted, the last client will still have id 3
    //this method is run after a client is  deletes
    //it will grab all the clients from the mysql db, store in an array
    //then it will truncate the mysql db, and make sure that the auto_increment value is properly set
    //finally, it will go through and add each client from the array back into the database via add client
    public static boolean updateAutoInc() {
    	try {
    		Connection connection = getConnection();
    		try {
    			int count = 0;
    			String cName = "";
    			String cChoice = "";
    			//Statement statement = connection.createStatement();
    			//ResultSet results = statement.executeQuery("SELECT * FROM clients");
    			
				ArrayList<Client> serverClientsHolder = new ArrayList();
				Boolean success = true;

				serverClientsHolder.clear();
				serverClientsHolder.addAll(getAllClients());
				

				int l = serverClientsHolder.size();
    
    			Statement statementT = connection.createStatement();
    			int i = statementT.executeUpdate("TRUNCATE TABLE clients");

    			Statement statementA = connection.createStatement();
    			int j = statementA.executeUpdate("ALTER TABLE clients AUTO_INCREMENT = 1");
    			
    			PreparedStatement statementU = connection.prepareStatement("INSERT INTO clients VALUES (NULL, ?, ?)");
    			
    			//cycles through and add each stored client one by one
    			for(int k = 0; k < serverClientsHolder.size(); k++) {
    				cName = serverClientsHolder.get(k).getClientNam();
    				cChoice = serverClientsHolder.get(k).getClientServ();
        			statementU.setString(1,  cName);
        			statementU.setString(2, cChoice);
        			statementU.executeUpdate();
        			count ++;
    			}

    			//for some reason these are showing as 0, but none of the other similar checks found throughout this do that. I can tell they are successfully working so I will ignore.
    			//if(i != 1) {
    				//System.out.println("FAILURE DURING UPDATE AUTO INC. SEE VAR i: " + i);
    				//success = false;
    			//}
    			//if(j != 1) {
    				//System.out.println("FAILURE DURING UPDATE AUTO INC. SEE VAR j: " + i);
    				//success = false;

    			//}
    			
    			
    			//this method used count to see how many clients were added, and compares that to L (the number of clients previously)
    			//this ensures that the correct number of clients were added. 
    			if(count != l) {
    				System.out.println("FAILURE DURING UPDATE AUTO INC. SEE VAR count & l: " + count + " L " + l);
    				success = false;

    			}
    			
	
    			
    			if(success == true) {
    				return success;
    			}
    			
    			
    			
			}catch (SQLException e) {
				e.printStackTrace();
			}
			
    		return false;
    		
    	}catch(SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("Error: Connection lost after intitional connection test. Now Closeing.", e);
    	}
    }
    
    //method that is passed a clients id, then performs a statement to remove that client
    public static boolean removeClient(int clientId) {
    	try {
    		Connection connection = getConnection();
    		try {
    			Statement statement = connection.createStatement();
    			int i = statement.executeUpdate("DELETE FROM clients WHERE id=" + clientId);

    			
    			if(i == 1) {
    				return true;
    			}
    			
    			
			}catch (SQLException e) {
				e.printStackTrace();
			}
			
    		return false;
    		
    	}catch(SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("Error: Connection lost after intitional connection test. Now Closeing.", e);
    	}
    }
    
    //public to set the clients choice
    //is passed the clients id and their choice (in a string, either Retirement or Brokerage)
    //users NEVER type retirement or brokerage
    public static boolean updateClientChoice(int clientId, String choice) {
    	try {
    		Connection connection = getConnection();
    		try {
    			PreparedStatement statement = connection.prepareStatement("UPDATE clients SET service =? WHERE id=?");
    			statement.setString(1,  choice);
    			statement.setInt(2, clientId);
    			int i = statement.executeUpdate();
    			
    			if(i == 1) {
    				return true;
    			}
    			
    			
			}catch (SQLException e) {
				e.printStackTrace();
			}
			
    		return false;
    		
    	}catch(SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("Error: Connection lost after intitional connection test. Now Closeing.", e);
    	}
    }
    
    
    //function to get all the clients (will then call the client interpreter)
    public static ArrayList getAllClients() {
    	try {
			Connection connection = getConnection();
			try {
				Statement statement = connection.createStatement();
				ResultSet results = statement.executeQuery("SELECT * FROM clients");
				
				ArrayList<Client> serverClients = new ArrayList();
				
				while(results.next()) {
					Client client = resultsinterpreterClient(results);
					serverClients.add(client);
				}
				
				return serverClients;
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
	    	return null;

			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("Error: Connection lost after intitional connection test. Now Closeing.", e);
		}
    	
    }
    
    //To validate, I have two functions,
    //This one looks for a match in both username and password. If this doesn't find one, 
    //the driver will call the other function which looks for just a matching username
    //So first it checks for username & password match, then username if first returns null, then it knows there was no matching username if both return null
    public static User validateUser(String username, String password)   {
    	try {
			Connection connection = getConnection();
			
			try {
				PreparedStatement statementBoth = connection.prepareStatement("SELECT * FROM users WHERE username=? AND password=?");

				statementBoth.setString(1, username);
				statementBoth.setString(2, password);
				ResultSet results = statementBoth.executeQuery();
				
				if(results.next()) {
					return resultsinterpreter(results);
				}
			} catch(SQLException e) {
				e.printStackTrace();
			}
			
			
			return null;
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("Error: Connection lost after intitional connection test. Now Closeing.", e);
		}
  	
    }
    
    //second validation that looks to see if there was a matching username at all
    public static User validateUserNoPass(String username, String password)   {
    	try {
			Connection connection = getConnection();
			
			try {
				PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE username=?");

				statement.setString(1, username);
				ResultSet results = statement.executeQuery();
				
				if(results.next()) {
					return resultsinterpreter(results);
				}
			} catch(SQLException e) {
				e.printStackTrace();
			}
			
			
			return null;
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("Error: Connection lost after intitional connection test. Now Closeing.", e);
		}
  	
    }

}

