package test.java;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import models.YelpRequestModel;
import models.DatabaseModel;
import models.ListTypeEnum;
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
		assertTrue(cached.setListResult(0, true, ListTypeEnum.FAVORITES));
		assertFalse(cached.setListResult(-1, true, ListTypeEnum.FAVORITES));
		assertFalse(cached.setListResult(100, true, ListTypeEnum.FAVORITES));
	}
	
	@Test
	public void testSetFavoritesWithOldFavorite() throws Exception{
		assertTrue(cached.setListResult(0, true, ListTypeEnum.FAVORITES));
		int searchId = DatabaseModel.AddSearchTermToHistory(id, "coffee", 5, 1000);
		MockYelpRequestModel m = new MockYelpRequestModel(searchId);
		assertTrue(m.checkParameters("coffee", 5, 1000));
		assertEquals(ResponseCodeModel.OK, m.completeTask());
	}
	
	
	@Test
	public void testSetDoNotShow() {
		assertTrue(cached.setListResult(0, true, ListTypeEnum.DONOTSHOW));
		for(int i = 0 ; i < cached.getResultsSize() ; i++) {
			assertTrue(cached.setListResult(i, true, ListTypeEnum.DONOTSHOW));
			assertTrue(cached.setListResult(i, true, ListTypeEnum.TOEXPLORE));
			assertTrue(cached.setListResult(i, true, ListTypeEnum.FAVORITES));
		}
		
		cached.checkParameters("coffee", 5, 1000);
		assertEquals(ResponseCodeModel.OK, cached.completeTask());
		assertEquals(5, cached.getResultsSize());
		assertTrue(cached.setListResult(0, true, ListTypeEnum.DONOTSHOW));
		assertFalse(cached.setListResult(-1, true, ListTypeEnum.DONOTSHOW));
		assertFalse(cached.setListResult(100, true, ListTypeEnum.DONOTSHOW));
		cached.sort();
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
		cached.checkParameters("coffee", 5, 1000);
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
