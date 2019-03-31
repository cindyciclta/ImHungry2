package test;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import models.EdamamRequestModel;
import models.ResponseModel;

public class TestResponseModel {
	
	private static ResponseModel rm;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		rm = new ResponseModel();
		rm.checkParameters("chicken", 5, 1000);
		assertTrue(rm.getSearchResults());
		assertEquals(rm.getNumberOfRestaurants(), 5);
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
		ResponseModel e = new ResponseModel();
		assertFalse(e.checkParameters("chicken", -5));
	}
	
	@Test
	public void testNullTerm() {
		ResponseModel e = new ResponseModel();
		assertFalse(e.checkParameters(null, 5));
	}
	
	@Test
	public void testEmptyTerm() {
		ResponseModel e = new ResponseModel();
		assertFalse(e.checkParameters(" ", 5));
	}
	
	@Test
	public void testInvalidRadius() {
		ResponseModel e = new ResponseModel();
		assertFalse(e.checkParameters("food", 5, -1));
	}
	
	@Test
	public void testInvalidCheckWithRadius() {
		ResponseModel e = new ResponseModel();
		assertFalse(e.checkParameters(" ", 5, 1));
	}
	
	@Test
	public void testValidCheckWithRadius() {
		ResponseModel e = new ResponseModel();
		assertTrue(e.checkParameters("food", 5, 1));
	}
	

}
