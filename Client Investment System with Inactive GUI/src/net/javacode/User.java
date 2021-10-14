//CS-499 Category Three - User
//Elijah Hickey
//9.27.21

package net.javacode;

public class User {
	//simple user object
	private int uId;
	private String uNam;
	private String uUsernam;
	private String uPwd;
	private String uPrivilege;
	

	User(){
		
	}
	User(int tId, String tNam, String tUserNam, String tPwd, String tPrivilege){
		uId = tId;
		uNam = tNam;
		uUsernam = tUserNam;
		uPwd = tPwd;
		uPrivilege = tPrivilege;
	}
	
	public int getUserID() {
		return uId;
	}
	public String getUserName() {
		return uNam;
	}

	public String getUserUsername() {
		return uUsernam;
	}
	public String getUserPassword() {
		return uPwd;
	}
	public String getUserPrivilege() {
		return uPrivilege;
	}
	
	public void setUserId(int tVar) {
		uId = tVar;
	}
	public void setUserFullName(String tVar) {
		uNam = tVar;
		
	}
	public void setUserUsername(String tVar) {
		uUsernam = tVar;
		
	}
	
	public void setUserPassword(String tVar) {
		uPwd = tVar;
		
	}
	public void setUserPrivilege(String tVar) {
		uPrivilege = tVar;
	}
	
	
}



