package test.java;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import models.DatabaseModel;
import models.MockRecipeRequestModel;
import models.ResponseCodeModel;

public class TestRecipeRequestModel{
	private static MockRecipeRequestModel cached;
	
	private static final String username = "testRecipeRequestModel";
	private static int id;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		String password = "test";
		
		assertTrue(DatabaseModel.insertUser(username, password.toCharArray()));
		id = DatabaseModel.GetUserID(username);
		int searchId = DatabaseModel.AddSearchTermToHistory(id, "coffee", 5, 5000);
		
		cached = new MockRecipeRequestModel(searchId);
		assertTrue(cached.checkParameters("coffee", 5));
		assertEquals(ResponseCodeModel.OK, cached.completeTask());
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		
		TestDatabaseModel.deleteUser(username);
	}

	@Before
	public void setUp() throws Exception {
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testInvalidLimit() {
		assertFalse(cached.checkParameters("chicken", -5));
	}
	
	@Test
	public void testNullTerm() {
		assertFalse(cached.checkParameters(null, 5));
	}
	
	@Test
	public void testEmptyTerm() {
		assertFalse(cached.checkParameters(" ", 5));
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
		assertNull(cached.getFormattedResultsFieldsAt(100));
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
		assertNull(cached.getFormattedDetailsFieldsAt(5));
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
	public void testExistingResults() {
		cached.checkParameters("coffee", 5);
		assertEquals(ResponseCodeModel.OK, cached.completeTask());
		assertNotNull(cached.getResults());
	}
	@Test
	public void testSetFavorites() {
		//assertEquals(ResponseCodeModel.OK, cached.completeTask("coffee", 5));
		assertTrue(cached.setFavoriteResult(0, true));
		assertFalse(cached.setFavoriteResult(-1, true));
		assertFalse(cached.setFavoriteResult(100, true));
	}
	
	@Test
	public void testSetToExplore() {
		//assertEquals(ResponseCodeModel.OK, cached.completeTask("coffee", 5));
		assertTrue(cached.setToExploreResult(0, true));
		assertFalse(cached.setToExploreResult(-1, true));
		assertFalse(cached.setToExploreResult(100, true));
	}
	
	@Test
	public void testMoveUpDown() {
		assertTrue(cached.setDoNotShowResult(0, true));
		assertTrue(cached.setDoNotShowResult(1, true));
		assertTrue(cached.moveUpDownList(0, 1, 2, "donotshow"));
		cached.sort();
	}
	
	@Test
	public void testUpdateList() {
		assertFalse(cached.updateList(-1));
	}
	
	@Test
	public void testMoveUpDownList() {
		assertFalse(cached.moveUpDownList(-1, 1, 2, "favorites"));
	}
	
	
	@Test
	public void testSetDoNotShow() {
		//assertEquals(ResponseCodeModel.OK, cached.completeTask("coffee", 5));
		assertTrue(cached.setDoNotShowResult(0, true));
		for(int i = 0 ; i < cached.getResultsSize() ; i++) {
			cached.setDoNotShowResult(i, false);
			cached.setToExploreResult(i, false);
			cached.setFavoriteResult(i, false);
		}
		
		assertTrue(cached.setDoNotShowResult(0, true));
		assertFalse(cached.setDoNotShowResult(-1, true));
		assertFalse(cached.setDoNotShowResult(100, true));
		cached.sort();
	}
	
	@Test
	public void testBadLimit() {
		cached.ExistRequest("term", -1);
	}
	
	@Test
	public void testBadLimitTask() {
		MockRecipeRequestModel m = new MockRecipeRequestModel(id);
		m.checkParameters("DROP DATABASE", 5);
		m.completeTask();
	}

}
