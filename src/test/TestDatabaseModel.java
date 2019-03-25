package test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;

import models.DatabaseModel;

public class TestDatabaseModel{
	
	public static void deleteUser(String username) throws Exception{
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/ImHungry?user=root&password=NewPassword&useSSL=false");
		
		// the mysql insert statement to have date of upload
		String query = "DELETE from users where user_name = (?)";
		PreparedStatement preparedStmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
	    preparedStmt.setString (1, username);
	    preparedStmt.executeUpdate();
	}
	
	@Test
	public void testCreateInstance() {
		assertNotNull(new DatabaseModel());
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
		assertTrue(DatabaseModel.userExists(username));
		deleteUser(username);
	}
	
	@Test
	public void testUserDoesNotExist() throws Exception{
		String username = "test";
		String password = "test";
		
		DatabaseModel.insertUser(username, password.toCharArray());
		assertFalse(DatabaseModel.userExists("aaah"));
		deleteUser(username);
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
	
	@Test
	public void testSearchHistory() throws Exception{
		String username = "test";
		String password = "test";
		DatabaseModel.insertUser(username, password.toCharArray());
		int id = DatabaseModel.GetUserID("test");
		assertTrue(DatabaseModel.AddSearchToHistory(id,"pizza", 5, 1000));
		
		deleteUser(username);
	}
	@Test
	public void testGetSearchHistory() throws Exception{
		String username = "test";
		String password = "test";
		DatabaseModel.insertUser(username, password.toCharArray());
		int id = DatabaseModel.GetUserID("test");
		assertTrue(DatabaseModel.AddSearchToHistory(id,"pizza", 5, 1000));
		assertNotNull(DatabaseModel.GetSearchHistory(id));
		deleteUser(username);
	}
	@Test
	public void testSearchHistoryInvalidUserid() throws Exception{
		String username = "test";
		String password = "test";
		DatabaseModel.insertUser(username, password.toCharArray());
		assertTrue(DatabaseModel.AddSearchToHistory(-1,"pizza", 5, 1000));
		deleteUser(username);
	}
}
