package test.java;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import models.YelpRequestModel;
import models.DatabaseModel;
import models.MockYelpRequestModel;
import models.ResponseCodeModel;

public class TestYelpRequestModel {

	private static MockYelpRequestModel cached;
	private static final String username = "testYelp";
	private static int id;
	
	@BeforeClass
	public static void setUp() throws Exception{
		String password = "test";
		
		assertTrue(DatabaseModel.insertUser(username, password.toCharArray()));
		id = DatabaseModel.GetUserID(username);
		int searchId = DatabaseModel.AddSearchTermToHistory(id, "coffee", 5, 1000);
		cached = new MockYelpRequestModel(searchId);
		assertTrue(cached.checkParameters("coffee", 5, 1000));
		assertEquals(ResponseCodeModel.OK, cached.completeTask()); //added radius: 1000
	}
	
	@AfterClass
	public static void tearDownBeforeClass() throws Exception {
		TestDatabaseModel.deleteUser(username);
	}
	
	@Test
	public void testInvalidLimit() {
		//YelpRequestModel e = new YelpRequestModel();
		assertFalse(cached.checkParameters("coffee", -5, 1000));
	}
	
	@Test
	public void testNullTerm() {
		//YelpRequestModel e = new YelpRequestModel();
		assertFalse(cached.checkParameters(null, 5, 1000));
	}
	
	@Test
	public void testEmptyTerm() {
		//YelpRequestModel e = new YelpRequestModel();
		assertFalse(cached.checkParameters(" ", 5, 1000));
	}
	
	@Test
	public void testResultsSize() {
		assertEquals(5, cached.getResultsSize());
		
	}
	
	@Test
	public void testGetFormattedResultsNegative() {
		assertNull(cached.getFormattedResultsFieldsAt(-1));
	}
	
	@Test
	public void testGetFormattedResultsOob() {
		assertNull(cached.getFormattedResultsFieldsAt(5));
	}
	
	@Test
	public void testGetFormattedResults() {
		assertNotNull(cached.getFormattedResultsFieldsAt(1));
	}
	
	@Test
	public void testGetFormattedDetailsNegative() {
		assertNull(cached.getFormattedDetailsFieldsAt(-1));
	}
	
	@Test
	public void testGetFormattedDetailssOob() {
		assertNull(cached.getFormattedDetailsFieldsAt(100));
	}
	
	@Test
	public void testGetFormattedDetails() {
		assertNotNull(cached.getFormattedDetailsFieldsAt(1));
	}
	
	@Test
	public void testNotNullResults() {
		assertNotNull(cached.getResults());
	}
	
	@Test
	public void testSetFavorites() {
		assertTrue(cached.setFavoriteResult(0, true));
		assertFalse(cached.setFavoriteResult(-1, true));
		assertFalse(cached.setFavoriteResult(100, true));
	}
	
	@Test
	public void testSetFavoritesWithOldFavorite() throws Exception{
		assertTrue(cached.setFavoriteResult(0, true));
		int searchId = DatabaseModel.AddSearchTermToHistory(id, "coffee", 5, 1000);
		MockYelpRequestModel m = new MockYelpRequestModel(searchId);
		assertTrue(m.checkParameters("coffee", 5, 1000));
		assertEquals(ResponseCodeModel.OK, m.completeTask());
	}
	
	@Test
	public void testSetToExplore() {
		//assertEquals(ResponseCodeModel.OK, cached.completeTask("coffee", 5));
		assertTrue(cached.setToExploreResult(0, true));
		assertFalse(cached.setToExploreResult(-1, true));
		assertFalse(cached.setToExploreResult(100, true));
	}
	
	
	@Test
	public void testSetDoNotShow() {
		cached.checkParameters("coffee", 5, 1000);
		assertEquals(ResponseCodeModel.OK, cached.completeTask());
		assertEquals(5, cached.getResultsSize());
		assertTrue(cached.setDoNotShowResult(0, true));
		assertFalse(cached.setDoNotShowResult(-1, true));
		assertFalse(cached.setDoNotShowResult(100, true));
		cached.sort();
	}
	
	@Test
	public void testExistingResults() {
		cached.checkParameters("coffee", 5, 1000);
		assertEquals(ResponseCodeModel.OK, cached.completeTask());
		assertNotNull(cached.getResults());
	}
	
	@Test
	public void testBadRequest() {
		cached.checkParameters("coffee", 5, 1000);
		cached.responseCode = 300;
		assertEquals(ResponseCodeModel.OK, cached.completeTask());
		assertNotNull(cached.getResults());
	}
	
	@Test
	public void testBadRadius() {
		MockYelpRequestModel m = new MockYelpRequestModel(id);
		m.checkParameters("DROP DATABASE", 5, 1000);
		m.completeTask();
	}
	
	
}
