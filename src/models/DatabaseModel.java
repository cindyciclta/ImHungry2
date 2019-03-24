package models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseModel {
	
	public static int signInUser(String username, char[] password) throws Exception{
		// Get from SQL
		Connection conn = null;
		ResultSet rs = null;
		int returnVal = -1;
		
		conn = getConnection();
		
		// the mysql insert statement to have date of upload
		String query = "SELECT (user_id) from users where user_name = (?) and user_password = SHA1(?)";
		PreparedStatement preparedStmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
	    preparedStmt.setString (1, username);
	    preparedStmt.setString(2, String.valueOf(password));
	    rs = preparedStmt.executeQuery();
	    if(rs.next()) {
	    	 returnVal = rs.getInt("user_id");
	    }
	    
	    rs.close();
		conn.close();
		return returnVal;
	}
	
	private static Connection getConnection() throws ClassNotFoundException, SQLException{
		Class.forName("com.mysql.jdbc.Driver");
		return DriverManager.getConnection("jdbc:mysql://localhost/ImHungry?user=root&password=root&useSSL=false");
	}
	
	public static boolean userExists(String username) throws Exception {
		// Get from SQL
		Connection conn = null;
		ResultSet rs = null;
		boolean returnVal = false;
		
		conn = getConnection();
		
		// the mysql insert statement to have date of upload
		String query = "SELECT * from users where user_name = (?)";
		PreparedStatement preparedStmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
	    preparedStmt.setString (1, username);
	    rs = preparedStmt.executeQuery();
	    if(rs.next()) {
	    	 returnVal = true;
	    }
	    rs.close();
		conn.close();
		return returnVal;

	}
	
	public static boolean insertUser(String username, char[] password) throws Exception{
		// Get from SQL
		Connection conn = null;
		boolean returnVal = true;
		
		conn = getConnection();
		
		if(DatabaseModel.userExists(username)) {
			returnVal = false;
		}
		
		// the mysql insert statement to have date of upload
		String sql = "INSERT INTO users (user_name, user_password) VALUES (?, SHA1(?))";
		PreparedStatement preparedStmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		preparedStmt = conn.prepareStatement(sql);
		preparedStmt.setString(1, username);
		preparedStmt.setString(2, String.valueOf(password)); 
		int affectedRows = preparedStmt.executeUpdate();
		
		conn.close();
		return returnVal;
	}
}
