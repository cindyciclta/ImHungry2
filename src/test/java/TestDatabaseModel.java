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
import models.ListTypeEnum;
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
	    
	    DatabaseModel.closeConnection();
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
	
	@Test
	public void deleteGroceryList() throws Exception{
		String username = "test";
		String password = "test";
		DatabaseModel.insertUser(username, password.toCharArray());
		DatabaseModel.InsertIntoGroceryList(username, "apple");
		GroceryListModel gl = DatabaseModel.getGroceryListFromUser(username);
		int id = DatabaseModel.GetUserID(username);
		DatabaseModel.deleteFromGroceryList(id, "apple");
		gl = DatabaseModel.getGroceryListFromUser(username);
		assertEquals(0, gl.getSize());
		deleteUser(username);
	}
	
	@Test
	public void InsertGroceryIdListValid() throws Exception{
		String username = "test";
		String password = "test";
		
		DatabaseModel.insertUser(username, password.toCharArray());
		int id = DatabaseModel.GetUserID(username);
		DatabaseModel.InsertIntoGroceryList(id, "apple");
		GroceryListModel gl = DatabaseModel.getGroceryListFromUser(username);
		assertEquals("apple", gl.getItem(0));
		deleteUser(username);
	}
	
	@Test
	public void InsertGroceryListInValid() throws Exception{
		String username = "test";
		String password = "test";
		DatabaseModel.insertUser(username, password.toCharArray());
		DatabaseModel.InsertIntoGroceryList("sdfsd", "apple");
		GroceryListModel gl = DatabaseModel.getGroceryListFromUser(username);
		assertEquals(0, gl.getSize());
		deleteUser(username);
	}

	public void addRestaurantToListExplore() throws Exception{
		String username = "test";
		String password = "test";
		DatabaseModel.insertUser(username, password.toCharArray());
		int id = DatabaseModel.GetUserID(username);
		RestaurantModel rm = new RestaurantModel();
		
		rm.setName("blah");
		rm.setInList(ListTypeEnum.TOEXPLORE, true);
		
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
		rm.setInList(ListTypeEnum.TOEXPLORE, true);
		
		DatabaseModel.AddSearchTermToHistory(id, "pizza", 10, 10);
		int searchId = DatabaseModel.getSearchId("pizza", 10, 10);
		DatabaseModel.insertRecipe(rm, searchId);
		DatabaseModel.insertRecipeIntoList(searchId, rm);
		
		List<RecipeModel> recipes = DatabaseModel.getRecipesInList(searchId);
		assertEquals(1, recipes.size());
		deleteUser(username);
	}
	
	@Test
	public void moveRecipeToList() throws Exception{
		String username = "test";
		String password = "test";
		DatabaseModel.insertUser(username, password.toCharArray());
		int id = DatabaseModel.GetUserID(username);
		
		RecipeModel rm2 = new RecipeModel();
		rm2.setName("blasdfsdfsh");
		rm2.setInList(ListTypeEnum.TOEXPLORE, true);
		DatabaseModel.AddSearchTermToHistory(id, "pizzalol", 10, 10);
		int searchId = DatabaseModel.getSearchId("pizzalol", 10, 10);
		DatabaseModel.insertRecipe(rm2, searchId);
		DatabaseModel.insertRecipeIntoList(searchId, rm2);
		DatabaseModel.insertRecipeIntoList(searchId, rm2); // KEEP DUPLICATE
		assertEquals(1, rm2.getOrder());
		
		
		RecipeModel rm = new RecipeModel();
		rm.setName("blah");
		rm.setInList(ListTypeEnum.TOEXPLORE, true);
		DatabaseModel.AddSearchTermToHistory(id, "pizza", 10, 10);
		searchId = DatabaseModel.getSearchId("pizza", 10, 10);
		DatabaseModel.insertRecipe(rm, searchId);
		DatabaseModel.insertRecipeIntoList(searchId, rm);
		assertEquals(2, rm.getOrder());
		
		rm.setInList(ListTypeEnum.DONOTSHOW, true);
		DatabaseModel.insertRecipeIntoList(searchId, rm);
		assertEquals(1, rm.getOrder());
		
		List<RecipeModel> recipes = DatabaseModel.getRecipesInList(searchId);
		assertEquals(2, recipes.size());
		deleteUser(username);
	}
	
	@Test
	public void moveRestaurant() throws Exception{
		String username = "test";
		String password = "test";
		DatabaseModel.insertUser(username, password.toCharArray());
		int id = DatabaseModel.GetUserID(username);
		
		RestaurantModel rm2 = new RestaurantModel();
		rm2.setName("blasdfsdfsh");
		rm2.setInList(ListTypeEnum.TOEXPLORE, true);
		DatabaseModel.AddSearchTermToHistory(id, "pizzalol", 10, 10);
		int searchId = DatabaseModel.getSearchId("pizzalol", 10, 10);
		DatabaseModel.insertRestaurant(rm2, searchId);
		DatabaseModel.insertRestaurantIntoList(searchId, rm2);
		DatabaseModel.insertRestaurantIntoList(searchId, rm2);
		assertEquals(1, rm2.getOrder());
		
		
		RestaurantModel rm = new RestaurantModel();
		rm.setName("blah");
		rm.setInList(ListTypeEnum.TOEXPLORE, true);
		DatabaseModel.AddSearchTermToHistory(id, "pizza", 10, 10);
		searchId = DatabaseModel.getSearchId("pizza", 10, 10);
		DatabaseModel.insertRestaurant(rm, searchId);
		DatabaseModel.insertRestaurantIntoList(searchId, rm);
		assertEquals(2, rm.getOrder());
		
		rm.setInList(ListTypeEnum.DONOTSHOW, true);
		DatabaseModel.insertRestaurantIntoList(searchId, rm);
		assertEquals(1, rm.getOrder());
		
		List<RestaurantModel> recipes = DatabaseModel.getRestaurantsInList(searchId);
		assertEquals(2, recipes.size());
		deleteUser(username);
	}
	
	@Test
	public void moveRestaurantUp() throws Exception{
		String username = "test";
		String password = "test";
		DatabaseModel.insertUser(username, password.toCharArray());
		int id = DatabaseModel.GetUserID(username);
		
		RestaurantModel rm4 = new RestaurantModel();
		rm4.setName("blasdfsdfshss");
		rm4.setInList(ListTypeEnum.TOEXPLORE, true);
		DatabaseModel.AddSearchTermToHistory(id, "pizzalossl", 10, 10);
		int searchId = DatabaseModel.getSearchId("pizzalossl", 10, 10);
		DatabaseModel.insertRestaurant(rm4, searchId);
		DatabaseModel.insertRestaurantIntoList(searchId, rm4);
		assertEquals(1, rm4.getOrder());
		
		RestaurantModel rm2 = new RestaurantModel();
		rm2.setName("blasdfsdfsh");
		rm2.setInList(ListTypeEnum.TOEXPLORE, true);
		DatabaseModel.AddSearchTermToHistory(id, "pizzalol", 10, 10);
		searchId = DatabaseModel.getSearchId("pizzalol", 10, 10);
		DatabaseModel.insertRestaurant(rm2, searchId);
		DatabaseModel.insertRestaurantIntoList(searchId, rm2);
		assertEquals(2, rm2.getOrder());
		
		
		RestaurantModel rm = new RestaurantModel();
		rm.setName("blah");
		rm.setInList(ListTypeEnum.TOEXPLORE, true);
		DatabaseModel.AddSearchTermToHistory(id, "pizza", 10, 10);
		searchId = DatabaseModel.getSearchId("pizza", 10, 10);
		DatabaseModel.insertRestaurant(rm, searchId);
		DatabaseModel.insertRestaurantIntoList(searchId, rm);
		assertEquals(3, rm.getOrder());
		
		RestaurantModel rm3 = new RestaurantModel();
		rm3.setName("bjjhijijilah");
		rm3.setInList(ListTypeEnum.TOEXPLORE, true);
		DatabaseModel.AddSearchTermToHistory(id, "pizdfdfza", 10, 10);
		searchId = DatabaseModel.getSearchId("pizdfdfza", 10, 10);
		DatabaseModel.insertRestaurant(rm3, searchId);
		DatabaseModel.insertRestaurantIntoList(searchId, rm3);
		assertEquals(4, rm3.getOrder());
		
		int itemId = DatabaseModel.getItemIdFromRestaurant(rm);
		DatabaseModel.updatePlaceOnMoveUpDown(itemId, searchId, "toexplore", rm.getOrder(), 2, "restaurant");
		
		List<RestaurantModel> recipes = DatabaseModel.getRestaurantsInList(searchId);
		assertEquals(4, recipes.size());
		for(int i = 0 ; i < 2 ; i++) {
			if(recipes.get(i).getFormattedFieldsForResultsPage().get("name").equals("blah")) {
				assertEquals(1, recipes.get(i).getOrder());
			}
		}
		deleteUser(username);
	}
	
	@Test
	public void moveRestaurantDown() throws Exception{
		String username = "test";
		String password = "test";
		DatabaseModel.insertUser(username, password.toCharArray());
		int id = DatabaseModel.GetUserID(username);
		
		RestaurantModel rm2 = new RestaurantModel();
		rm2.setName("blasdfsdfsh");
		rm2.setInList(ListTypeEnum.TOEXPLORE, true);
		DatabaseModel.AddSearchTermToHistory(id, "pizzalol", 10, 10);
		int searchId = DatabaseModel.getSearchId("pizzalol", 10, 10);
		DatabaseModel.insertRestaurant(rm2, searchId);
		DatabaseModel.insertRestaurantIntoList(searchId, rm2);
		assertEquals(1, rm2.getOrder());
		
		RestaurantModel rm = new RestaurantModel();
		rm.setName("blah");
		rm.setInList(ListTypeEnum.TOEXPLORE, true);
		DatabaseModel.AddSearchTermToHistory(id, "pizza", 10, 10);
		searchId = DatabaseModel.getSearchId("pizza", 10, 10);
		DatabaseModel.insertRestaurant(rm, searchId);
		DatabaseModel.insertRestaurantIntoList(searchId, rm);
		assertEquals(2, rm.getOrder());
		
		RestaurantModel rm3 = new RestaurantModel();
		rm3.setName("aaaablah");
		rm3.setInList(ListTypeEnum.TOEXPLORE, true);
		DatabaseModel.AddSearchTermToHistory(id, "pizzaa", 10, 10);
		searchId = DatabaseModel.getSearchId("pizzaa", 10, 10);
		DatabaseModel.insertRestaurant(rm3, searchId);
		DatabaseModel.insertRestaurantIntoList(searchId, rm3);
		assertEquals(3, rm3.getOrder());
		
		int itemId = DatabaseModel.getItemIdFromRestaurant(rm2);
		DatabaseModel.updatePlaceOnMoveUpDown(itemId, searchId, "toexplore", rm2.getOrder(), 2, "restaurant");
		
		List<RestaurantModel> recipes = DatabaseModel.getRestaurantsInList(searchId);
		assertEquals(3, recipes.size());
		for(int i = 0 ; i < 2 ; i++) {
			if(recipes.get(i).getFormattedFieldsForResultsPage().get("name").equals("blah")) {
				assertEquals(1, recipes.get(i).getOrder());
			}else {
				assertEquals(2, recipes.get(i).getOrder());
			}
		}
		deleteUser(username);
	}
	
	@Test
	public void deleteRecipeFromList() throws Exception{
		String username = "test";
		String password = "test";
		DatabaseModel.insertUser(username, password.toCharArray());
		int id = DatabaseModel.GetUserID(username);
		
		RecipeModel rm2 = new RecipeModel();
		rm2.setName("blasdfsdfsh");
		rm2.setInList(ListTypeEnum.TOEXPLORE, true);
		DatabaseModel.AddSearchTermToHistory(id, "pizzalol", 10, 10);
		int searchId = DatabaseModel.getSearchId("pizzalol", 10, 10);
		DatabaseModel.insertRecipe(rm2, searchId);
		DatabaseModel.insertRecipeIntoList(searchId, rm2);
		assertEquals(1, rm2.getOrder());
		
		
		RecipeModel rm = new RecipeModel();
		rm.setName("blah");
		rm.setInList(ListTypeEnum.TOEXPLORE, true);
		DatabaseModel.AddSearchTermToHistory(id, "pizza", 10, 10);
		int search2 = DatabaseModel.getSearchId("pizza", 10, 10);
		DatabaseModel.insertRecipe(rm, search2);
		DatabaseModel.insertRecipeIntoList(search2, rm);
		assertEquals(2, rm.getOrder());
		
		DatabaseModel.deleteRecipe(rm2, searchId);
		
		List<RecipeModel> recipes = DatabaseModel.getRecipesInList(searchId);
		assertEquals(1, recipes.size());
		assertEquals(1, recipes.get(0).getOrder());
		deleteUser(username);
	}
	
	@Test
	public void deleteInvalidId() throws Exception{
		String username = "test";
		String password = "test";
		DatabaseModel.insertUser(username, password.toCharArray());
		int id = DatabaseModel.GetUserID(username);
		
		RecipeModel rm = new RecipeModel();
		rm.setName("blah");
		rm.setInList(ListTypeEnum.TOEXPLORE, true);
		DatabaseModel.AddSearchTermToHistory(id, "pizza", 10, 10);
		int search2 = DatabaseModel.getSearchId("pizza", 10, 10);
		DatabaseModel.insertRecipe(rm, search2);
		DatabaseModel.insertRecipeIntoList(search2, rm);
		assertEquals(1, rm.getOrder());
		
		DatabaseModel.deleteRecipe(rm, -1);
		assertEquals(1, DatabaseModel.getRecipesInList(search2).size());
		deleteUser(username);
	}
	
	@Test
	public void deleteRestaurantInvalidId() throws Exception{
		String username = "test";
		String password = "test";
		DatabaseModel.insertUser(username, password.toCharArray());
		int id = DatabaseModel.GetUserID(username);
		
		RestaurantModel rm = new RestaurantModel();
		rm.setName("blah");
		rm.setInList(ListTypeEnum.TOEXPLORE, true);
		DatabaseModel.AddSearchTermToHistory(id, "pizza", 10, 10);
		int search2 = DatabaseModel.getSearchId("pizza", 10, 10);
		DatabaseModel.insertRestaurant(rm, search2);
		DatabaseModel.insertRestaurantIntoList(search2, rm);
		assertEquals(1, rm.getOrder());
		
		DatabaseModel.deleteRestaurant(rm, -1);
		assertEquals(1, DatabaseModel.getRestaurantsInList(search2).size());
		deleteUser(username);
	}

	@Test
	public void deleteRestaurantFromList() throws Exception{
		String username = "test";
		String password = "test";
		DatabaseModel.insertUser(username, password.toCharArray());
		int id = DatabaseModel.GetUserID(username);
		
		RestaurantModel rm2 = new RestaurantModel();
		rm2.setName("blasdfsdfsh");
		rm2.setInList(ListTypeEnum.TOEXPLORE, true);
		DatabaseModel.AddSearchTermToHistory(id, "pizzalol", 10, 10);
		int searchId = DatabaseModel.getSearchId("pizzalol", 10, 10);
		DatabaseModel.insertRestaurant(rm2, searchId);
		DatabaseModel.insertRestaurantIntoList(searchId, rm2);
		assertEquals(1, rm2.getOrder());
		
		
		RestaurantModel rm = new RestaurantModel();
		rm.setName("blah");
		rm.setInList(ListTypeEnum.TOEXPLORE, true);
		DatabaseModel.AddSearchTermToHistory(id, "pizza", 10, 10);
		int search2 = DatabaseModel.getSearchId("pizza", 10, 10);
		DatabaseModel.insertRestaurant(rm, search2);
		DatabaseModel.insertRestaurantIntoList(search2, rm);
		assertEquals(2, rm.getOrder());
		
		DatabaseModel.deleteRestaurant(rm2, searchId);
		
		List<RestaurantModel> recipes = DatabaseModel.getRestaurantsInList(searchId);
		assertEquals(1, recipes.size());
		assertEquals(1, recipes.get(0).getOrder());
		deleteUser(username);
	}

}
