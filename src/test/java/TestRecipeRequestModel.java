package test.java;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import models.DatabaseModel;
import models.ListTypeEnum;
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
	public void testSetList() {
		//assertEquals(ResponseCodeModel.OK, cached.completeTask("coffee", 5));
		assertTrue(cached.setListResult(0, true, ListTypeEnum.FAVORITES));
		assertFalse(cached.setListResult(-1, true, ListTypeEnum.FAVORITES));
		assertFalse(cached.setListResult(100, true, ListTypeEnum.FAVORITES));
	}
	
	@Test
	public void testSetDelete() {
		assertTrue(cached.setListResult(0, true, ListTypeEnum.DONOTSHOW));
		for(int i = 0 ; i < cached.getResultsSize() ; i++) {
			assertTrue(cached.setListResult(i, false, ListTypeEnum.DONOTSHOW));
			assertTrue(cached.setListResult(i, false, ListTypeEnum.TOEXPLORE));
			assertTrue(cached.setListResult(i, false, ListTypeEnum.FAVORITES));
		}
	}
	
	@Test
	public void testMoveUpDown() {
		cached.checkParameters("coffee", 5);
		assertEquals(ResponseCodeModel.OK, cached.completeTask());
		assertEquals(5, cached.getResultsSize());
		assertTrue(cached.setListResult(0, true, ListTypeEnum.DONOTSHOW));
		assertTrue(cached.setListResult(0, true, ListTypeEnum.DONOTSHOW));
		assertTrue(cached.moveUpDownList(0, 1, 2, ListTypeEnum.DONOTSHOW.type));
		cached.sort();
	}
	
	@Test
	public void testUpdateList() {
		assertFalse(cached.updateList(-1));
	}
	
	@Test
	public void testMoveUpDownList() {
		assertFalse(cached.moveUpDownList(-1, 1, 2, ListTypeEnum.FAVORITES.type));
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
	
	@Test
	public void testSetDoNotShow() {
		assertTrue(cached.setListResult(0, true, ListTypeEnum.DONOTSHOW));
		for(int i = 0 ; i < cached.getResultsSize() ; i++) {
			assertTrue(cached.setListResult(i, true, ListTypeEnum.DONOTSHOW));
			assertTrue(cached.setListResult(i, true, ListTypeEnum.TOEXPLORE));
			assertTrue(cached.setListResult(i, true, ListTypeEnum.FAVORITES));
		}
		
		cached.checkParameters("coffee", 5);
		assertEquals(ResponseCodeModel.OK, cached.completeTask());
		assertEquals(5, cached.getResultsSize());
		assertTrue(cached.setListResult(0, true, ListTypeEnum.DONOTSHOW));
		assertFalse(cached.setListResult(-1, true, ListTypeEnum.DONOTSHOW));
		assertFalse(cached.setListResult(100, true, ListTypeEnum.DONOTSHOW));
		cached.sort();
	}

}
