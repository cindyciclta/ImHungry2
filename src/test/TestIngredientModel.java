package test;

import static org.junit.Assert.*;

import org.junit.Test;

import models.IngredientModel;

public class TestIngredientModel {
	
	@Test
	public void testIngredientModelValidFormat() {
		IngredientModel i = new IngredientModel();
		assertTrue(i.validateIngredients("oil", 10.5, "fluid ounces"));
		assertEquals(i.formatIngredients(), "oil");
	}

}
