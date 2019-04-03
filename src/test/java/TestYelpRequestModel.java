package test.java;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import models.YelpRequestModel;
import models.MockYelpRequestModel;
import models.ResponseCodeModel;

public class TestYelpRequestModel {

private static MockYelpRequestModel cached;
	
	@BeforeClass
	public static void setUp() {
		cached = new MockYelpRequestModel();
		assertTrue(cached.yelp.checkParameters("coffee", 5, 1000));
		assertEquals(ResponseCodeModel.OK, cached.completeTask("coffee",5, 1000)); //added radius: 1000
	}
	
	@Test
	public void testInvalidLimit() {
		//YelpRequestModel e = new YelpRequestModel();
		assertFalse(cached.yelp.checkParameters("coffee", -5, 1000));
	}
	
	@Test
	public void testNullTerm() {
		//YelpRequestModel e = new YelpRequestModel();
		assertFalse(cached.yelp.checkParameters(null, 5, 1000));
	}
	
	@Test
	public void testEmptyTerm() {
		//YelpRequestModel e = new YelpRequestModel();
		assertFalse(cached.yelp.checkParameters(" ", 5, 1000));
	}
	
	@Test
	public void testResultsSize() {
		assertEquals(5, cached.yelp.getResultsSize());
		
	}
	
	@Test
	public void testGetFormattedResultsNegative() {
		assertNull(cached.yelp.getFormattedResultsFieldsAt(-1));
	}
	
	@Test
	public void testGetFormattedResultsOob() {
		assertNull(cached.yelp.getFormattedResultsFieldsAt(5));
	}
	
	@Test
	public void testGetFormattedResults() {
		assertNotNull(cached.yelp.getFormattedResultsFieldsAt(1));
	}
	
	@Test
	public void testGetFormattedDetailsNegative() {
		assertNull(cached.yelp.getFormattedDetailsFieldsAt(-1));
	}
	
	@Test
	public void testGetFormattedDetailssOob() {
		assertNull(cached.yelp.getFormattedDetailsFieldsAt(5));
	}
	
	@Test
	public void testGetFormattedDetails() {
		assertNotNull(cached.yelp.getFormattedDetailsFieldsAt(1));
	}
	
	@Test
	public void testNotNullResults() {
		assertNotNull(cached.yelp.getResults());
	}
	
	@Test
	public void testSetFavorites() {
		//assertEquals(ResponseCodeModel.OK, cached.completeTask("coffee", 5));
		assertTrue(cached.yelp.setFavoriteResult(0, true));
		assertFalse(cached.yelp.setFavoriteResult(-1, true));
		assertFalse(cached.yelp.setFavoriteResult(100, true));
	}
	
	@Test
	public void testSetToExplore() {
		//assertEquals(ResponseCodeModel.OK, cached.completeTask("coffee", 5));
		assertTrue(cached.yelp.setToExploreResult(0, true));
		assertFalse(cached.yelp.setToExploreResult(-1, true));
		assertFalse(cached.yelp.setToExploreResult(100, true));
	}
	
	
	@Test
	public void testSetDoNotShow() {
		//assertEquals(ResponseCodeModel.OK, cached.completeTask("coffee", 5));
		assertTrue(cached.yelp.setDoNotShowResult(0, true));
		assertFalse(cached.yelp.setDoNotShowResult(-1, true));
		assertFalse(cached.yelp.setDoNotShowResult(100, true));
		cached.yelp.sort();
	}
	@Test
	public void testExistingResults() {
		assertEquals(ResponseCodeModel.OK, cached.completeTask("coffee", 5, 1000));
		assertNotNull(cached.yelp.getResults());
	}
	
	@Test
	public void testBadRequest() {
		cached.yelp.responseCode = 300;
		assertEquals(ResponseCodeModel.OK, cached.completeTask("coffee", 5, 1000));
		assertNotNull(cached.yelp.getResults());
	}
	
	@Test
	public void testBadRadius() {
		assertEquals(ResponseCodeModel.INTERNAL_ERROR, cached.completeTask("cosdfsdffee", 1, 0));
	}
	
	
}
