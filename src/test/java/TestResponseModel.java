package test.java;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import models.DatabaseModel;
import models.EdamamRequestModel;
import models.GroceryListModel;
import models.ListTypeEnum;
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
		for(int i = 0 ; i < rm.getNumberOfRecipes() ; i++) {
			rm.addToList(i, ListTypeEnum.FAVORITES.type, "recipe", false);
			rm.addToList(i, ListTypeEnum.DONOTSHOW.type, "recipe", false);
			rm.addToList(i, ListTypeEnum.TOEXPLORE.type, "recipe", false);
		}
		for(int i = 0 ; i < rm.getNumberOfRestaurants() ; i++) {
			rm.addToList(i, ListTypeEnum.FAVORITES.type, "restaurant", false);
			rm.addToList(i, ListTypeEnum.DONOTSHOW.type, "restaurant", false);
			rm.addToList(i, ListTypeEnum.TOEXPLORE.type, "restaurant", false);
		}
		
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
		rm.addToList(0, ListTypeEnum.DONOTSHOW.type, "restaurant", true);
		Map<String, String> modifiers = rm.getFormattedRestaurantResultsAt(0);
		assertEquals(ListTypeEnum.DONOTSHOW.type, modifiers.get("modifier"));
	}
	
	@Test
	public void getNumberOfListItems() {
		ResponseModel rm = new ResponseModel(id);
		rm.checkParameters("chicken", 5, 1000);
		assertTrue(rm.getSearchResults());
		assertEquals(rm.getNumberOfRestaurants(), 5);
		for(int i = 0 ; i < rm.getNumberOfRecipes() ; i++) {
			rm.addToList(i, ListTypeEnum.FAVORITES.type, "recipe", false);
			rm.addToList(i, ListTypeEnum.DONOTSHOW.type, "recipe", false);
			rm.addToList(i, ListTypeEnum.TOEXPLORE.type, "recipe", false);
		}
		for(int i = 0 ; i < rm.getNumberOfRestaurants() ; i++) {
			rm.addToList(i, ListTypeEnum.FAVORITES.type, "restaurant", false);
			rm.addToList(i, ListTypeEnum.DONOTSHOW.type, "restaurant", false);
			rm.addToList(i, ListTypeEnum.TOEXPLORE.type, "restaurant", false);
		}
		assertEquals(0, rm.getNumberOfListItems(ListTypeEnum.DONOTSHOW.type));
		assertEquals(0, rm.getNumberOfListItems(ListTypeEnum.FAVORITES.type));
		assertEquals(0, rm.getNumberOfListItems(ListTypeEnum.TOEXPLORE.type));
	}
	
	@Test
	public void getFormattedListRecipe() {
		ResponseModel rm = new ResponseModel(id);
		rm.checkParameters("chicken", 5, 1000);
		assertTrue(rm.getSearchResults());
		assertEquals(rm.getNumberOfRestaurants(), 5);
		for(int i = 0 ; i < rm.getNumberOfRecipes() ; i++) {
			rm.addToList(i, ListTypeEnum.FAVORITES.type, "recipe", false);
			rm.addToList(i, ListTypeEnum.DONOTSHOW.type, "recipe", false);
			rm.addToList(i, ListTypeEnum.TOEXPLORE.type, "recipe", false);
		}
		for(int i = 0 ; i < rm.getNumberOfRestaurants() ; i++) {
			rm.addToList(i, ListTypeEnum.FAVORITES.type, "restaurant", false);
			rm.addToList(i, ListTypeEnum.DONOTSHOW.type, "restaurant", false);
			rm.addToList(i, ListTypeEnum.TOEXPLORE.type, "restaurant", false);
		}
		
		assertEquals(0, rm.getNumberOfListItems(ListTypeEnum.DONOTSHOW.type));
		assertEquals(0, rm.getNumberOfListItems(ListTypeEnum.FAVORITES.type));
		assertEquals(0, rm.getNumberOfListItems(ListTypeEnum.TOEXPLORE.type));
		rm.addToList(2, ListTypeEnum.FAVORITES.type, "recipe", true);
		assertNotNull(rm.getFormattedResultListAt(0, ListTypeEnum.FAVORITES.type));
		assertNotNull(rm.getFormattedResultListAt(0, ListTypeEnum.FAVORITES.type).get("prepTime"));
	}
	
	@Test
	public void testFormattedListRestaurant() throws Exception{

		assertEquals(rm.getNumberOfRestaurants(), 5);
		for(int i = 0 ; i < rm.getNumberOfRecipes() ; i++) {
			rm.addToList(i, ListTypeEnum.FAVORITES.type, "recipe", false);
			rm.addToList(i, ListTypeEnum.DONOTSHOW.type, "recipe", false);
			rm.addToList(i, ListTypeEnum.TOEXPLORE.type, "recipe", false);
		}
		for(int i = 0 ; i < rm.getNumberOfRestaurants() ; i++) {
			rm.addToList(i, ListTypeEnum.FAVORITES.type, "restaurant", false);
			rm.addToList(i, ListTypeEnum.DONOTSHOW.type, "restaurant", false);
			rm.addToList(i, ListTypeEnum.TOEXPLORE.type, "restaurant", false);
		}
		assertEquals(0, rm.getNumberOfListItems(ListTypeEnum.DONOTSHOW.type));
		assertEquals(0, rm.getNumberOfListItems(ListTypeEnum.FAVORITES.type));
		assertEquals(0, rm.getNumberOfListItems(ListTypeEnum.TOEXPLORE.type));
		rm.addToList(2, ListTypeEnum.FAVORITES.type, "restaurant", true);
		
		assertNotNull(rm.getFormattedResultListAt(0, ListTypeEnum.FAVORITES.type));
		assertNotNull(rm.getFormattedResultListAt(0, ListTypeEnum.FAVORITES.type).get("drivingTime"));
	}
	
	@Test
	public void testMoveUpDownListRestaurant() throws Exception{

		assertEquals(rm.getNumberOfRestaurants(), 5);
		for(int i = 0 ; i < rm.getNumberOfRecipes() ; i++) {
			rm.addToList(i, ListTypeEnum.FAVORITES.type, "recipe", false);
			rm.addToList(i, ListTypeEnum.DONOTSHOW.type, "recipe", false);
			rm.addToList(i, ListTypeEnum.TOEXPLORE.type, "recipe", false);
		}
		for(int i = 0 ; i < rm.getNumberOfRestaurants() ; i++) {
			rm.addToList(i, ListTypeEnum.FAVORITES.type, "restaurant", false);
			rm.addToList(i, ListTypeEnum.DONOTSHOW.type, "restaurant", false);
			rm.addToList(i, ListTypeEnum.TOEXPLORE.type, "restaurant", false);
		}
		assertEquals(0, rm.getNumberOfListItems(ListTypeEnum.DONOTSHOW.type));
		assertEquals(0, rm.getNumberOfListItems(ListTypeEnum.FAVORITES.type));
		assertEquals(0, rm.getNumberOfListItems(ListTypeEnum.TOEXPLORE.type));
		
		rm.addToList(2, ListTypeEnum.FAVORITES.type, "restaurant", true);
		rm.addToList(3, ListTypeEnum.FAVORITES.type, "restaurant", true);
		String name = rm.getFormattedResultListAt(0, ListTypeEnum.FAVORITES.type).get("name");
		rm.moveUpDownList(0, ListTypeEnum.FAVORITES.type, "restaurant", 1, 2);
		rm.addToList(1, ListTypeEnum.DONOTSHOW.type, "restaurant", true);
		for(int i = 0 ; i < rm.getNumberOfListItems(ListTypeEnum.FAVORITES.type) ; i++) {
			assertNotNull(rm.getFormattedResultListAt(i, ListTypeEnum.FAVORITES.type));
			assertNull(rm.getFormattedResultListAt(i, ListTypeEnum.TOEXPLORE.type));
			if(rm.getFormattedResultListAt(i, ListTypeEnum.FAVORITES.type).get("name").equals(name)) {
				assertEquals("2", rm.getFormattedResultListAt(i, ListTypeEnum.FAVORITES.type).get("place"));
			}
		}
	}
	
	@Test
	public void testMoveUpDownListRecipe() throws Exception{

		assertEquals(rm.getNumberOfRestaurants(), 5);
		for(int i = 0 ; i < rm.getNumberOfRecipes() ; i++) {
			rm.addToList(i, ListTypeEnum.FAVORITES.type, "recipe", false);
			rm.addToList(i, ListTypeEnum.DONOTSHOW.type, "recipe", false);
			rm.addToList(i, ListTypeEnum.TOEXPLORE.type, "recipe", false);
		}
		for(int i = 0 ; i < rm.getNumberOfRestaurants() ; i++) {
			rm.addToList(i, ListTypeEnum.FAVORITES.type, "restaurant", false);
			rm.addToList(i, ListTypeEnum.DONOTSHOW.type, "restaurant", false);
			rm.addToList(i, ListTypeEnum.TOEXPLORE.type, "restaurant", false);
		}
		assertEquals(0, rm.getNumberOfListItems(ListTypeEnum.DONOTSHOW.type));
		assertEquals(0, rm.getNumberOfListItems(ListTypeEnum.FAVORITES.type));
		assertEquals(0, rm.getNumberOfListItems(ListTypeEnum.TOEXPLORE.type));
		
		rm.addToList(2, ListTypeEnum.FAVORITES.type, "recipe", true);
		rm.addToList(3, ListTypeEnum.FAVORITES.type, "recipe", true);
		String name = rm.getFormattedResultListAt(0, ListTypeEnum.FAVORITES.type).get("name");
		rm.moveUpDownList(0, ListTypeEnum.FAVORITES.type, "recipe", 1, 2);
		rm.addToList(1, ListTypeEnum.DONOTSHOW.type, "recipe", true);
		for(int i = 0 ; i < rm.getNumberOfListItems(ListTypeEnum.FAVORITES.type) ; i++) {
			assertNotNull(rm.getFormattedResultListAt(i, ListTypeEnum.FAVORITES.type));
			assertNull(rm.getFormattedResultListAt(i, ListTypeEnum.TOEXPLORE.type));
			if(rm.getFormattedResultListAt(i, ListTypeEnum.FAVORITES.type).get("name").equals(name)) {
				assertEquals("2", rm.getFormattedResultListAt(i, ListTypeEnum.FAVORITES.type).get("place"));
			}
		}	
	}
	
	@Test
	public void getFormattedListNull() {
		ResponseModel rm = new ResponseModel(id);
		rm.checkParameters("chicken", 5, 1000);
		assertTrue(rm.getSearchResults());
		assertEquals(rm.getNumberOfRestaurants(), 5);
		for(int i = 0 ; i < rm.getNumberOfRecipes() ; i++) {
			rm.addToList(i, ListTypeEnum.FAVORITES.type, "recipe", false);
			rm.addToList(i, ListTypeEnum.DONOTSHOW.type, "recipe", false);
			rm.addToList(i, ListTypeEnum.TOEXPLORE.type, "recipe", false);
		}
		for(int i = 0 ; i < rm.getNumberOfRestaurants() ; i++) {
			rm.addToList(i, ListTypeEnum.FAVORITES.type, "restaurant", false);
			rm.addToList(i, ListTypeEnum.DONOTSHOW.type, "restaurant", false);
			rm.addToList(i, ListTypeEnum.TOEXPLORE.type, "restaurant", false);
		}
		assertEquals(0, rm.getNumberOfListItems(ListTypeEnum.DONOTSHOW.type));
		assertEquals(0, rm.getNumberOfListItems(ListTypeEnum.FAVORITES.type));
		assertEquals(0, rm.getNumberOfListItems(ListTypeEnum.TOEXPLORE.type));
		assertNull(rm.getFormattedResultListAt(0, ListTypeEnum.FAVORITES.type));
	}
	
	@Test
	public void testAddToGroceries() throws Exception{
		assertTrue(rm.addToGroceryList(0, id, "0"));
	}
	
	@Test
	public void testAddToGroceriesGetGroceries() throws Exception{
		assertTrue(rm.addToGroceryList(0, id, "0"));
		assertEquals(1, rm.getGroceryList(id).getSize());
		GroceryListModel gl = rm.getGroceryList(id);
		rm.deleteFromGroceryList(id, gl.getItem(0));
		
		assertEquals(0, rm.getGroceryList(id).getSize());
	}
	
	@Test
	public void testAddToGroceriesGetGroceriesInvalid() throws Exception{
		assertTrue(rm.addToGroceryList(0, id, "0"));
		assertNull(rm.getGroceryList(-1));
	}
	
	@Test
	public void testAddToGroceriesDeleteGroceriesInvalid() throws Exception{
		assertFalse(rm.deleteFromGroceryList(id, null));
	}
	
	@Test
	public void testRestaurantFavorites() {
		rm.addToList(0, ListTypeEnum.FAVORITES.type, "restaurant", true);
		Map<String, String> modifiers = rm.getFormattedRestaurantResultsAt(0);
		assertEquals(ListTypeEnum.FAVORITES.type, modifiers.get("modifier"));
	}
	
	@Test
	public void testRestaurantToExplore() {
		rm.addToList(0, ListTypeEnum.TOEXPLORE.type, "restaurant", true);
		Map<String, String> modifiers = rm.getFormattedRestaurantResultsAt(0);
		assertEquals(ListTypeEnum.TOEXPLORE.type, modifiers.get("modifier"));
	}
	
	@Test
	public void testRecipeDoNotShow() {
		for(int i = 0 ; i < rm.getNumberOfRecipes() ; i++) {
			rm.addToList(i, ListTypeEnum.FAVORITES.type, "recipe", false);
			rm.addToList(i, ListTypeEnum.DONOTSHOW.type, "recipe", false);
			rm.addToList(i, ListTypeEnum.TOEXPLORE.type, "recipe", false);
		}
		for(int i = 0 ; i < rm.getNumberOfRestaurants() ; i++) {
			rm.addToList(i, ListTypeEnum.FAVORITES.type, "restaurant", false);
			rm.addToList(i, ListTypeEnum.DONOTSHOW.type, "restaurant", false);
			rm.addToList(i, ListTypeEnum.TOEXPLORE.type, "restaurant", false);
		}
		rm.addToList(0, ListTypeEnum.DONOTSHOW.type, "recipe", true);
		Map<String, String> modifiers = rm.getFormattedRecipeResultsAt(0);
		assertEquals(ListTypeEnum.DONOTSHOW.type, modifiers.get("modifier"));
	}
	
	@Test
	public void testRecipeFavorites() {
		rm.addToList(0, ListTypeEnum.FAVORITES.type, "recipe", true);
		Map<String, String> modifiers = rm.getFormattedRecipeResultsAt(0);
		assertEquals(ListTypeEnum.FAVORITES.type, modifiers.get("modifier"));
	}
	
	@Test
	public void testRecipeToExplore() {
		rm.addToList(0, ListTypeEnum.TOEXPLORE.type, "recipe", true);
		Map<String, String> modifiers = rm.getFormattedRecipeResultsAt(0);
		assertEquals(ListTypeEnum.TOEXPLORE.type, modifiers.get("modifier"));
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
