package test.java;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;

import models.DatabaseModel;
import models.GroceryListModel;
import models.RecipeModel;
import models.RestaurantModel;
import models.SearchTermModel;

public class TestDatabaseModel{
	
	public static void deleteUser(String username) throws Exception{
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = DatabaseModel.getConnection();
		
		// clean the searches database as well
		int id = DatabaseModel.GetUserID(username);
		
		// clean grocery list
		String query = "DELETE from grocery_list where user_id = (?)";
		PreparedStatement preparedStmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
	    preparedStmt.setInt (1, id);
	    preparedStmt.executeUpdate();
	    preparedStmt.close();
		
		// clean places
	    query = "DELETE from places where user_id = (?)";
		preparedStmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
	    preparedStmt.setInt (1, id);
	    preparedStmt.executeUpdate();
	    preparedStmt.close();
	    
		//  clean favorites
		query = "DELETE from list_restaurants where user_id = (?)";
		preparedStmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
	    preparedStmt.setInt (1, id);
	    preparedStmt.executeUpdate();
	    preparedStmt.close();
	    
	    query = "DELETE from list_recipes where user_id = (?)";
		preparedStmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
	    preparedStmt.setInt (1, id);
	    preparedStmt.executeUpdate();
	    preparedStmt.close();
		
		
		Vector<SearchTermModel> searches = DatabaseModel.GetSearchHistory(id);
		for(SearchTermModel search : searches) {
			query = "DELETE from recipes where search_id = (?)";
			preparedStmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		    preparedStmt.setInt (1, search.search_id);
		    preparedStmt.executeUpdate();
		    preparedStmt.close();
		    
		    query = "DELETE from restaurants where search_id = (?)";
			preparedStmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		    preparedStmt.setInt (1, search.search_id);
		    preparedStmt.executeUpdate();
		    preparedStmt.close();
		}
		
		query = "DELETE from searches where user_id = (?)";
		preparedStmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
	    preparedStmt.setInt (1, id);
	    preparedStmt.executeUpdate();
	    preparedStmt.close();
		
		// the mysql insert statement to have date of upload
		query = "DELETE from users where user_name = (?)";
		preparedStmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
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
		ArrayList<String> urls = new ArrayList<>();
		urls.add("dddd");
		assertTrue(DatabaseModel.AddSearchToHistory(id,"pizza", 5, 1000, urls));
		deleteUser(username);
	}
	
	@Test
	public void testSearchHistoryLotsOfUrls() throws Exception{
		String username = "test";
		String password = "test";
		DatabaseModel.insertUser(username, password.toCharArray());
		int id = DatabaseModel.GetUserID("test");
		ArrayList<String> urls = new ArrayList<>();
		for(int i = 0 ; i < 20 ; i++) {
			urls.add("dddd" + i);
		}
		assertTrue(DatabaseModel.AddSearchToHistory(id,"pizza", 5, 1000, urls));
		assertNotNull(DatabaseModel.GetSearchHistory(id));
		deleteUser(username);
	}
	
	@Test
	public void testRemoveAdjacentNull() throws Exception{
		DatabaseModel.removeAdjacentDuplicates(null);
	}
	
	@Test
	public void testRemoveAdjacentEmpty() throws Exception{
		DatabaseModel.removeAdjacentDuplicates(new Vector<SearchTermModel>());
	}
	
	@Test
	public void testRemoveAdjacentValid() throws Exception{
		Vector<SearchTermModel> removal = new Vector<SearchTermModel>();
		for(int i = 0 ; i < 3 ; i++) {
			SearchTermModel s = new SearchTermModel();
			s.term = "sdsdfsdf";
			removal.add(s);
		}
		DatabaseModel.removeAdjacentDuplicates(removal);
		assertEquals(1, removal.size());
	}
	
	
	@Test
	public void testGetSearchHistory() throws Exception{
		String username = "test";
		String password = "test";
		DatabaseModel.insertUser(username, password.toCharArray());
		int id = DatabaseModel.GetUserID("test");
		assertTrue(DatabaseModel.AddSearchToHistory(id,"pizza", 5, 1000, new ArrayList<String>()));
		assertNotNull(DatabaseModel.GetSearchHistory(id));
		deleteUser(username);
	}
	@Test
	public void testSearchHistoryInvalidUserid() throws Exception{
		String username = "test";
		String password = "test";
		DatabaseModel.insertUser(username, password.toCharArray());
		assertEquals(-1, DatabaseModel.AddSearchTermToHistory(-1,"pizza", 5, 1000));
		deleteUser(username);
	}
	
	@Test
	public void getInvalidUsernameID() throws Exception{
		String username = "aaaajhklhjkh";
		assertEquals(-1, DatabaseModel.GetUserID(username));
	}
	
	@Test
	public void getSearchIdNoId() throws Exception{
		assertEquals(-1, DatabaseModel.getSearchId("psdfsdf", 1000, 1));
	}
	
	@Test
	public void getSearchIdValidId() throws Exception{
		String username = "test";
		String password = "test";
		DatabaseModel.insertUser(username, password.toCharArray());
		int id = DatabaseModel.GetUserID(username);
		DatabaseModel.AddSearchTermToHistory(id, "pizza", 10, 1000);
		assertNotEquals(-1, DatabaseModel.getSearchId("pizza", 10, 1000));
		deleteUser(username);
	}
	
	@Test
	public void getSearchIdWrongUser() throws Exception{
		String username = "test";
		String password = "test";
		DatabaseModel.insertUser(username, password.toCharArray());
		int id = DatabaseModel.GetUserID(username);
		
		String second = "tes2";
		DatabaseModel.insertUser(second, password.toCharArray());
		int id2 = DatabaseModel.GetUserID(second);
		
		DatabaseModel.AddSearchTermToHistory(id2, "pizza", 10, 1000);
		assertEquals(-1, DatabaseModel.getSearchIdUser(id, "pizza", 10, 1000));
		deleteUser(username);
		deleteUser(second);
	}
	
	@Test
	public void getSearchIdUserValidId() throws Exception{
		String username = "test";
		String password = "test";
		DatabaseModel.insertUser(username, password.toCharArray());
		int id = DatabaseModel.GetUserID(username);
		DatabaseModel.AddSearchTermToHistory(id, "pizza", 10, 1000);
		assertNotEquals(-1, DatabaseModel.getSearchIdUser(id, "pizza", 10, 1000));
		deleteUser(username);
	}
	
	@Test
	public void getSearchIdNoRadiusNoId() throws Exception{
		assertEquals(-1, DatabaseModel.getSearchId("psdfsdf", 1000));
	}
	
	@Test
	public void getSearchIdNoRadiusValidId() throws Exception{
		String username = "test";
		String password = "test";
		DatabaseModel.insertUser(username, password.toCharArray());
		int id = DatabaseModel.GetUserID(username);
		DatabaseModel.AddSearchTermToHistory(id, "pizza", 10, 1000);
		assertNotEquals(-1, DatabaseModel.getSearchId("pizza", 10));
		deleteUser(username);
	}
	
	@Test
	public void getSearchUserIdNoId() throws Exception{
		String username = "test";
		String password = "test";
		DatabaseModel.insertUser(username, password.toCharArray());
		int id = DatabaseModel.GetUserID(username);
		assertEquals(-1, DatabaseModel.getSearchIdUser(id, "psdfsdf", 1000, 1));	
		deleteUser(username);
	}
	
	@Test
	public void getRestaurantsBadId() throws Exception{
		assertEquals(0, DatabaseModel.getRestaurantsFromSearchTerm("sdfs", 1000, 10).size());
	}
	
	@Test
	public void getRecipesBadId() throws Exception{
		assertEquals(0, DatabaseModel.getRecipesFromSearchTerm("sdfs", 1000).size());
	}
	
	@Test
	public void getRestaurantsValid() throws Exception{
		String username = "test";
		String password = "test";
		DatabaseModel.insertUser(username, password.toCharArray());
		int id = DatabaseModel.GetUserID(username);
		DatabaseModel.AddSearchTermToHistory(id, "pizza", 10, 10);
		int searchId = DatabaseModel.getSearchId("pizza", 10, 10);
		
		RestaurantModel r = new RestaurantModel();
		r.setName("aaaah");
		DatabaseModel.insertRestaurant(r, searchId);
		
		assertNotEquals(0, DatabaseModel.getRestaurantsFromSearchTerm("pizza", 10, 10).size());
		deleteUser(username);
	}
	
	@Test
	public void getRecipesValid() throws Exception{
		String username = "test";
		String password = "test";
		DatabaseModel.insertUser(username, password.toCharArray());
		int id = DatabaseModel.GetUserID(username);
		DatabaseModel.AddSearchTermToHistory(id, "pizza", 10, 10);
		int searchId = DatabaseModel.getSearchId("pizza", 10, 10);
		
		RecipeModel r = new RecipeModel();
		r.setName("aaaah");
		DatabaseModel.insertRecipe(r, searchId);
		
		assertNotEquals(0, DatabaseModel.getRecipesFromSearchTerm("pizza", 10).size());
		deleteUser(username);
	}
	
	@Test

	public void InsertGroceryListValid() throws Exception{
		String username = "test";
		String password = "test";
		DatabaseModel.insertUser(username, password.toCharArray());
		DatabaseModel.InsertIntoGroceryList(username, "apple");
		GroceryListModel gl = DatabaseModel.getGroceryListFromUser(username);
		assertEquals("apple", gl.getItem(0));
		deleteUser(username);
	}

	public void addRestaurantToListExplore() throws Exception{
		String username = "test";
		String password = "test";
		DatabaseModel.insertUser(username, password.toCharArray());
		int id = DatabaseModel.GetUserID(username);
		RestaurantModel rm = new RestaurantModel();
		
		rm.setName("blah");
		rm.setInToExplore(true);
		
		DatabaseModel.AddSearchTermToHistory(id, "pizza", 10, 10);
		int searchId = DatabaseModel.getSearchId("pizza", 10, 10);
		DatabaseModel.insertRestaurant(rm, searchId);
		DatabaseModel.insertRestaurantIntoList(searchId, rm);
		
		List<RestaurantModel> restaurants = DatabaseModel.getRestaurantsInList(searchId);
		assertEquals(1, restaurants.size());
		deleteUser(username);
	}
	
	@Test
	public void addRestaurantToListFavorites() throws Exception{
		String username = "test";
		String password = "test";
		DatabaseModel.insertUser(username, password.toCharArray());
		int id = DatabaseModel.GetUserID(username);
		RestaurantModel rm = new RestaurantModel();
		
		rm.setName("blah");
		rm.setInFavorites(true);
		
		DatabaseModel.AddSearchTermToHistory(id, "pizza", 10, 10);
		int searchId = DatabaseModel.getSearchId("pizza", 10, 10);
		DatabaseModel.insertRestaurant(rm, searchId);
		DatabaseModel.insertRestaurantIntoList(searchId, rm);
		
		List<RestaurantModel> restaurants = DatabaseModel.getRestaurantsInList(searchId);
		assertEquals(1, restaurants.size());
		deleteUser(username);
	}
	
	@Test
	public void addRestaurantToDoNotShow() throws Exception{
		String username = "test";
		String password = "test";
		DatabaseModel.insertUser(username, password.toCharArray());
		int id = DatabaseModel.GetUserID(username);
		RestaurantModel rm = new RestaurantModel();
		
		rm.setName("blah");
		rm.setInDoNotShow(true);
		
		DatabaseModel.AddSearchTermToHistory(id, "pizza", 10, 10);
		int searchId = DatabaseModel.getSearchId("pizza", 10, 10);
		DatabaseModel.insertRestaurant(rm, searchId);
		DatabaseModel.insertRestaurantIntoList(searchId, rm);
		
		List<RestaurantModel> restaurants = DatabaseModel.getRestaurantsInList(searchId);
		assertEquals(1, restaurants.size());
		deleteUser(username);
	}
	
	@Test
	public void addRecipeToListExplore() throws Exception{
		String username = "test";
		String password = "test";
		DatabaseModel.insertUser(username, password.toCharArray());
		int id = DatabaseModel.GetUserID(username);
		RecipeModel rm = new RecipeModel();
		
		rm.setName("blah");
		rm.setInToExplore(true);
		
		DatabaseModel.AddSearchTermToHistory(id, "pizza", 10, 10);
		int searchId = DatabaseModel.getSearchId("pizza", 10, 10);
		DatabaseModel.insertRecipe(rm, searchId);
		DatabaseModel.insertRecipeIntoList(searchId, rm);
		
		List<RecipeModel> recipes = DatabaseModel.getRecipesInList(searchId);
		assertEquals(1, recipes.size());
		deleteUser(username);
	}
	
	@Test
	public void addRecipeToListFavorites() throws Exception{
		String username = "test";
		String password = "test";
		DatabaseModel.insertUser(username, password.toCharArray());
		int id = DatabaseModel.GetUserID(username);
		RecipeModel rm = new RecipeModel();
		
		rm.setName("blah");
		rm.setInFavorites(true);
		
		DatabaseModel.AddSearchTermToHistory(id, "pizza", 10, 10);
		int searchId = DatabaseModel.getSearchId("pizza", 10, 10);
		DatabaseModel.insertRecipe(rm, searchId);
		DatabaseModel.insertRecipeIntoList(searchId, rm);
		
		List<RecipeModel> recipes = DatabaseModel.getRecipesInList(searchId);
		assertEquals(1, recipes.size());
		deleteUser(username);
	}
	
	@Test
	public void addRecipeToDoNotShow() throws Exception{
		String username = "test";
		String password = "test";
		DatabaseModel.insertUser(username, password.toCharArray());
		int id = DatabaseModel.GetUserID(username);
		RecipeModel rm = new RecipeModel();
		
		rm.setName("blah");
		rm.setInDoNotShow(true);
		
		DatabaseModel.AddSearchTermToHistory(id, "pizza", 10, 10);
		int searchId = DatabaseModel.getSearchId("pizza", 10, 10);
		DatabaseModel.insertRecipe(rm, searchId);
		DatabaseModel.insertRecipeIntoList(searchId, rm);
		
		List<RecipeModel> recipes = DatabaseModel.getRecipesInList(searchId);
		assertEquals(1, recipes.size());
		deleteUser(username);
	}

}
