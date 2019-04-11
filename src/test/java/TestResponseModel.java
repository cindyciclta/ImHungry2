package test.java;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import models.DatabaseModel;
import models.EdamamRequestModel;
import models.ResponseModel;

public class TestResponseModel {
	
	private static ResponseModel rm;
	
	private static int id;
	private static final String username = "testRM"; 

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		String password = "test";
		
		assertTrue(DatabaseModel.insertUser(username, password.toCharArray()));
		id = DatabaseModel.GetUserID(username);
		
		rm = new ResponseModel(id);
		rm.checkParameters("chicken", 5, 1000);
		assertTrue(rm.getSearchResults());
		assertEquals(rm.getNumberOfRestaurants(), 5);
	}
	
	@AfterClass
	public static void tearDownBeforeClass() throws Exception {
		TestDatabaseModel.deleteUser(username);
	}
	
	@Test
	public void testDuplicate() {
		ResponseModel rm = new ResponseModel(id);
		rm.checkParameters("chicken", 5, 1000);
		assertTrue(rm.getSearchResults());
		assertNotEquals(-1, rm.getSearchId());
	}

	@Test
	public void testGetSearchTerm() {
		assertEquals("chicken", rm.getSearchTerm());
	}
	
	@Test
	public void testGetFormattedDetailedRecipeAt() {
		assertNotNull(rm.getFormattedDetailedRecipeAt(1));
	}
	
	@Test
	public void testGetFormattedDetailedRestaurantAt() {
		assertNotNull(rm.getFormattedDetailedRestaurantAt(1));
	}
	
	@Test
	public void testSort() {
		rm.sort();

		// check sorting
		int oldPrep = -1;
		for(int i = 0 ; i < rm.getNumberOfRecipes() ; i++) {
			Map<String, String> words = rm.getFormattedDetailedRecipeAt(i);
			int newPrep = Integer.parseInt(words.get("prepTime").split(" ")[0]);
			assert oldPrep <= newPrep;
			oldPrep = newPrep;
		}
	}
	
	@Test
	public void testRestaurantDoNotShow() {
		rm.addToList(0, "donotshow", "restaurant", true);
		Map<String, String> modifiers = rm.getFormattedRestaurantResultsAt(0);
		assertEquals("donotshow", modifiers.get("modifier"));
	}
	
	@Test
	public void testRestaurantFavorites() {
		rm.addToList(0, "favorites", "restaurant", true);
		Map<String, String> modifiers = rm.getFormattedRestaurantResultsAt(0);
		assertEquals("favorites", modifiers.get("modifier"));
	}
	
	@Test
	public void testRestaurantToExplore() {
		rm.addToList(0, "toexplore", "restaurant", true);
		Map<String, String> modifiers = rm.getFormattedRestaurantResultsAt(0);
		assertEquals("toexplore", modifiers.get("modifier"));
	}
	
	@Test
	public void testRecipeDoNotShow() {
		rm.addToList(0, "donotshow", "recipe", true);
		Map<String, String> modifiers = rm.getFormattedRecipeResultsAt(0);
		assertEquals("donotshow", modifiers.get("modifier"));
	}
	
	@Test
	public void testRecipeFavorites() {
		rm.addToList(0, "favorites", "recipe", true);
		Map<String, String> modifiers = rm.getFormattedRecipeResultsAt(0);
		assertEquals("favorites", modifiers.get("modifier"));
	}
	
	@Test
	public void testRecipeToExplore() {
		rm.addToList(0, "toexplore", "recipe", true);
		Map<String, String> modifiers = rm.getFormattedRecipeResultsAt(0);
		assertEquals("toexplore", modifiers.get("modifier"));
	}
	
	
	
	@Test
	public void testGetFormattedRecipeResultsAt() {
		assertNotNull(rm.getFormattedRecipeResultsAt(1));
	}
	
	@Test
	public void testGetFormattedRestaurantResultsAt() {
		assertNotNull(rm.getFormattedRestaurantResultsAt(1));
	}
	
	@Test
	public void testGetNumberOfRecipes() {
		assertEquals(5, rm.getNumberOfRecipes());
	}
	
	@Test
	public void testGetNumberOfRestaurants() {
		assertEquals(5, rm.getNumberOfRestaurants());
	}
	
	
	@Test
	public void testInvalidLimit() {
		ResponseModel e = new ResponseModel(id);
		assertFalse(e.checkParameters("chicken", -5));
	}
	
	@Test
	public void testNullTerm() {
		ResponseModel e = new ResponseModel(id);
		assertFalse(e.checkParameters(null, 5));
	}
	
	@Test
	public void testEmptyTerm() {
		ResponseModel e = new ResponseModel(id);
		assertFalse(e.checkParameters(" ", 5));
	}
	
	@Test
	public void testInvalidRadius() {
		ResponseModel e = new ResponseModel(id);
		assertFalse(e.checkParameters("food", 5, -1));
	}
	
	@Test
	public void testInvalidCheckWithRadius() {
		ResponseModel e = new ResponseModel(id);
		assertFalse(e.checkParameters(" ", 5, 1));
	}
	
	@Test
	public void testValidCheckWithRadius() {
		ResponseModel e = new ResponseModel(id);
		assertTrue(e.checkParameters("food", 5, 1));
	}
	
	@Test
	public void testInvalidUserId() {
		ResponseModel e = new ResponseModel(-1);
		assertTrue(e.getSearchResults());
	}
	
	@Test
	public void testRadius() {
		ResponseModel e = new ResponseModel(-1);
		e.checkParameters("test", 10, 10);
		assertEquals(10, e.getRadius());
	}
	
	@Test
	public void testLimit() {
		ResponseModel e = new ResponseModel(-1);
		e.checkParameters("test", 100, 10);
		assertEquals(100, e.getLimit());
	}
	

}
