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
    
    
    //public to sett the clients choice
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

