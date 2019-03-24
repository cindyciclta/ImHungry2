package test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.junit.Test;

import java.sql.Connection;

import models.DatabaseModel;

public class TestDatabaseModel {
	
	public static void deleteUser(String username) throws Exception{
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/CalendarDB?user=root&password=root&useSSL=false");
		
		// the mysql insert statement to have date of upload
		String query = "DELETE * from users where user_name = (?)";
		PreparedStatement preparedStmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
	    preparedStmt.setString (1, username);
	    ResultSet rs = preparedStmt.executeQuery();
	}

	@Test
	public void testAddNewUser() throws Exception{
		String username = "test";
		String password = "test";
		
		assertTrue(DatabaseModel.insertUser(username, password.toCharArray()));
		deleteUser(username);
	}
	
	@Test
	public void testAddExistingUser() throws Exception{
		String username = "test";
		String password = "test";
		
		assertTrue(DatabaseModel.insertUser(username, password.toCharArray()));
		
		assertFalse(DatabaseModel.insertUser(username, password.toCharArray()));
		
		deleteUser(username);
	}
	
	@Test
	public void testUserExists() throws Exception{
		String username = "test";
		String password = "test";
		
		DatabaseModel.insertUser(username, password.toCharArray());
		deleteUser(username);
		assertTrue(DatabaseModel.userExists(username));
	}
	
	@Test
	public void testUserDoesNotExist() throws Exception{
		String username = "test";
		String password = "test";
		
		DatabaseModel.insertUser(username, password.toCharArray());
		deleteUser(username);
		assertFalse(DatabaseModel.userExists("aaah"));
	}
	
	@Test
	public void testSignInValid() throws Exception{
		String username = "test";
		String password = "test";
		
		DatabaseModel.insertUser(username, password.toCharArray());
		assertTrue(DatabaseModel.signInUser(username, password.toCharArray()) != -1);
		deleteUser(username);
	}
	
	@Test
	public void testSignInUsernameInvalid() throws Exception{
		String username = "test";
		String password = "test";
		
		DatabaseModel.insertUser(username, password.toCharArray());
		assertTrue(DatabaseModel.signInUser("invalid", password.toCharArray()) == -1);
		deleteUser(username);
	}
	
	@Test
	public void testSignInPasswordInvalid() throws Exception{
		String username = "test";
		String password = "test";
		
		DatabaseModel.insertUser(username, password.toCharArray());
		assertTrue(DatabaseModel.signInUser(username, new char[5]) == -1);
		deleteUser(username);
	}
}
