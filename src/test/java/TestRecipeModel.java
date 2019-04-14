package test.java;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Test;

import models.IngredientModel;
import models.RecipeModel;
import models.RestaurantModel;

public class TestRecipeModel {
	
	@Test
	public void testSetName() {
		RecipeModel rm = new RecipeModel();
		assertTrue(rm.setName("name"));
	}
	
	@Test
	public void testSetNameNull() {
		RecipeModel rm = new RecipeModel();
		assertFalse(rm.setName(null));
	}
	
	@Test
	public void testSetNameEmpty() {
		RecipeModel rm = new RecipeModel();
		assertFalse(rm.setName("   "));
	}
	
	@Test
	public void testSetImageUrl() {
		RecipeModel rm = new RecipeModel();
		assertTrue(rm.setImageUrl("google.com"));
	}
	
	@Test
	public void testSetImageUrlNull() {
		RecipeModel rm = new RecipeModel();
		assertFalse(rm.setImageUrl(null));
	}
	
	@Test
	public void testSetImageUrlEmpty() {
		RecipeModel rm = new RecipeModel();
		assertFalse(rm.setImageUrl("   "));
	}
	
	@Test
	public void testSetStars() {
		RecipeModel rm = new RecipeModel();
		assertTrue(rm.setStars(4));
	}
	
	@Test
	public void testSetStarsNegative() {
		RecipeModel rm = new RecipeModel();
		assertFalse(rm.setStars(-1));
	}
	
	@Test
	public void testSetStarsOverFive() {
		RecipeModel rm = new RecipeModel();
		assertFalse(rm.setStars(6));
	}

	@Test
	public void testFormattedResultsDetails() {
		RecipeModel rm = new RecipeModel();
		rm.setName("nameOfRecipe");
		IngredientModel i = new IngredientModel();
		i.validateIngredients("ingredient", 10, "");
		rm.addIngredient(i);
		rm.addInstruction("make food");
		Map<String, String> res = rm.getFormattedFieldsForDetailsPage();
		assertEquals(res.get("name"), "nameOfRecipe");
	}
	
	@Test
	public void testModifierDoNotShow() {
		RecipeModel rm = new RecipeModel();
		rm.setInDoNotShow(true);
		Map<String, String> res = rm.getFormattedFieldsForResults();
		assertEquals("donotshow", res.get("modifier"));
	}
	
	@Test
	public void testModifierFavorites() {
		RecipeModel rm = new RecipeModel();
		rm.setInFavorites(true);
		Map<String, String> res = rm.getFormattedFieldsForResults();
		assertEquals("favorites", res.get("modifier"));
	}
	
	@Test
	public void testToExplore() {
		RecipeModel rm = new RecipeModel();
		rm.setInToExplore(true);
		Map<String, String> res = rm.getFormattedFieldsForResults();
		assertEquals("toexplore", res.get("modifier"));
	}
	
	@Test
	public void testFormattedResults() {
		RecipeModel rm = new RecipeModel();
		rm.setStars(3);
		rm.addIngredient(new IngredientModel());
		rm.addInstruction("make food");
		rm.setName("nameOfRecipe");
		Map<String, String> res = rm.getFormattedFieldsForResults();
		assertEquals(res.get("name"), "nameOfRecipe");
	}
	
	@Test
	public void testFormattedNoIngreidnets() {
		RecipeModel rm = new RecipeModel();
		rm.setStars(3);
		rm.setName("nameOfRecipe");
		Map<String, String> res = rm.getFormattedFieldsForDetailsPage();
		assertEquals(res.get("name"), "nameOfRecipe");
	}
	
	@Test
	public void setTestPrepTimeInvalid() {
		RecipeModel rm = new RecipeModel();
		assertFalse(rm.setPrepTime(-5));
	}
	
	@Test
	public void setTestPrepTime() {
		RecipeModel rm = new RecipeModel();
		assertTrue(rm.setPrepTime(5));
	}
	
	@Test
	public void setTestCookTime() {
		RecipeModel rm = new RecipeModel();
		assertTrue(rm.setCookTime(5));
	}
	
	@Test
	public void setTesCookTimeInvalid() {
		RecipeModel rm = new RecipeModel();
		assertFalse(rm.setCookTime(-5));
	}
	
	@Test
	public void testAddIngredientNull() {
		RecipeModel rm = new RecipeModel();
		assertFalse(rm.addIngredient(null));
	}
	
	@Test
	public void testAddInstructionNull() {
		RecipeModel rm = new RecipeModel();
		assertFalse(rm.addInstruction(null));
	}
	
	@Test
	public void testAddInstructionInvalid() {
		RecipeModel rm = new RecipeModel();
		assertFalse(rm.addInstruction("   "));
	}
	
	@Test
	public void testRecipeModelAscending() {
		RecipeModel rm = new RecipeModel();
		rm.setName("test1");
		rm.setPrepTime(10);
		RecipeModel rm2 = new RecipeModel();
		rm2.setPrepTime(11);
		rm2.setName("test2");
		assertEquals(-1, rm.compareTo(rm2));
		
		List<RecipeModel> rms = new ArrayList<>();
		rms.add(rm);
		rms.add(rm2);
		Collections.sort(rms);
		assertEquals("test2", rms.get(1).getFormattedFieldsForResults().get("name"));
		assertEquals("test1", rms.get(0).getFormattedFieldsForResults().get("name"));
		
	}
	
	@Test
	public void testLowerThan() {
		RecipeModel rm = new RecipeModel();
		rm.setPrepTime(5);
		
		RecipeModel rmLarger = new RecipeModel();
		rmLarger.setPrepTime(10);
		
		assertEquals(-1, rm.compareTo(rmLarger));
	}
	
	@Test
	public void testHigherThan() {
		RecipeModel rm = new RecipeModel();
		rm.setPrepTime(5);
		
		RecipeModel rmLarger = new RecipeModel();
		rmLarger.setPrepTime(10);
		
		assertEquals(1, rmLarger.compareTo(rm));
	}
	
	@Test
	public void testEqual() {
		RecipeModel rm = new RecipeModel();
		rm.setPrepTime(10);
		
		RecipeModel rmEqual = new RecipeModel();
		rmEqual.setPrepTime(10);
		
		assertEquals(0, rmEqual.compareTo(rm));
	}
	
	@Test
	public void testThisFavorite() {
		RecipeModel rm = new RecipeModel();
		rm.setInFavorites(true);
		
		RecipeModel rmFavorite = new RecipeModel();
		rmFavorite.setInFavorites(false);
		
		assertEquals(1, rmFavorite.compareTo(rm));
	}
	
	@Test
	public void testOtherFavorite() {
		RecipeModel rm = new RecipeModel();
		rm.setInFavorites(true);
		
		RecipeModel rmFavorite = new RecipeModel();
		rmFavorite.setInFavorites(false);
		
		assertEquals(-1, rm.compareTo(rmFavorite));
	}
	
	@Test
	public void testBothFavorite() {
		RecipeModel rm = new RecipeModel();
		rm.setInFavorites(true);
		rm.setPrepTime(1);
		
		RecipeModel rmFavorite = new RecipeModel();
		rmFavorite.setInFavorites(true);
		rmFavorite.setPrepTime(2);
		
		assertEquals(-1, rm.compareTo(rmFavorite));
	}
	
	@Test
	public void testNeitherFavorite() {
		RecipeModel rm = new RecipeModel();
		rm.setInFavorites(false);
		rm.setPrepTime(1);
		
		RecipeModel rmFavorite = new RecipeModel();
		rmFavorite.setInFavorites(false);
		rmFavorite.setPrepTime(2);
		
		assertEquals(-1, rm.compareTo(rmFavorite));
	}
	
	@Test
	public void testIngredients() {
		RecipeModel rm = new RecipeModel();
		assertEquals(0, rm.getIngredients().size());
	}
	
	@Test
	public void testEqualsNull() {
		RecipeModel rm = new RecipeModel();
		assertFalse(rm.equals(null));
	}
	
	@Test
	public void testEqualsWrongClass() {
		RecipeModel rm = new RecipeModel();
		assertFalse(rm.equals("restaurant"));
	}
	
	@Test
	public void testEqualsNullName() {
		RecipeModel rm = new RecipeModel();
		rm.setName("sdfs");
		assertFalse(rm.equals(new RecipeModel()));
	}
	
	@Test
	public void testEqualsNullNameOther() {
		RecipeModel rm = new RecipeModel();
		RecipeModel rm2 = new RecipeModel();
		rm2.setName("sdfs");
		assertFalse(rm.equals(rm2));
	}
	
	@Test
	public void testEqualValid() {
		RecipeModel rm = new RecipeModel();
		rm.setName("sdfs");
		rm.hashCode();
		RecipeModel rm2 = new RecipeModel();
		rm2.setName("sdfs");
		assertTrue(rm.equals(rm2));
	}
	

	@Test
	public void testEqualWrongName() {
		RecipeModel rm = new RecipeModel();
		rm.setName("sdfs");
		rm.hashCode();
		RecipeModel rm2 = new RecipeModel();
		rm2.setName("sdfsdfs");
		assertFalse(rm.equals(rm2));
	}

}
