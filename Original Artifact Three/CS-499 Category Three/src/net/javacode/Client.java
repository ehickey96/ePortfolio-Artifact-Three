//CS-499 Category Three - Client
//Elijah Hickey
//9.27.21

package net.javacode;

public class Client {

//basic client object. 
//Later iterations could include more info
	private int clientNum;
	private String clientNam;
	private String clientServ;
	
	Client(){
		
	}
	
	Client(int tClientNum, String tClientNam, String tClientServ){
		clientNum = tClientNum;
		clientNam = tClientNam;
		clientServ = tClientServ;
	}
	
	public int getClientNum() {
		return clientNum;
	}
	
	public String getClientNam() {
		return clientNam;
	}
	
	public String getClientServ() {
		return clientServ;
	}
	
	public void setClientNum(int tVar) {
		clientNum = tVar;
	}
	
	public void setClientNam(String tVar) {
		clientNam = tVar;
	}
	
	public void setClientServ(String tVar) {
		clientServ = tVar;
	}
	  	
	//used when displayInfo is called from main() in the driver class
	public void printClientData() {
		System.out.println(clientNum + ". " + clientNam + " selected option " + clientServ );
	}
}


