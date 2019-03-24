package models;

public class DatabaseModel {
	
	public static int signInUser(String username, char[] password) {
		return -1; // user does not exist
	}
	
	public static boolean userExists(String username) {
		return false; 
	}
	
	public static boolean insertUser(String username, char[] password) {
		return false; // return false if user not added
	}
	
	

}
