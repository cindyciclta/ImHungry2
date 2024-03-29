package test.java;

import controllers.RedirectionController;
import models.DatabaseModel;
import models.ListTypeEnum;
import models.ResponseModel;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.io.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.*;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestRedirectionController extends Mockito{
	
	@Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @Mock
    RequestDispatcher rd;
    
    @Mock
    HttpSession sess;
    
    private static ResponseModel rm;
    
    private static final String username = "testRedirectionController";
    private static String index;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		String password = "test";
		
		assertTrue(DatabaseModel.insertUser(username, password.toCharArray()));
		int id = DatabaseModel.GetUserID(username);
		
		rm = new ResponseModel(id);
		rm.checkParameters("chicken", 2);
		assertTrue(rm.getSearchResults());
		index = RedirectionController.addResponse(rm);
		RedirectionController.tokens.put("fakeToken", id);
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception{
		RedirectionController.removeResponse(index);
		TestDatabaseModel.deleteUser(username);
	}
	
	@Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

	@Test
    public void testInvalidAction() throws Exception {
        when(request.getParameter("action")).thenReturn( null );
        when(request.getRequestDispatcher("SearchPageView.jsp")).thenReturn(rd);
        new RedirectionController().service(request, response);
        verify(rd).forward(request, response);
    }
	
	@Test
    public void testInvalidActionEmpty() throws Exception {
        when(request.getParameter("action")).thenReturn( "" );
        when(request.getRequestDispatcher("SearchPageView.jsp")).thenReturn(rd);
        new RedirectionController().service(request, response);
        verify(rd).forward(request, response);
    }
	
	@Test
    public void testSearchInvalid() throws Exception {
        when(request.getParameter("action")).thenReturn( "search" );
        when(request.getParameter("term")).thenReturn( null );
        when(request.getParameter("limit")).thenReturn( "5" );
        when(request.getRequestDispatcher("SearchPageView.jsp")).thenReturn(rd);
        new RedirectionController().service(request, response);
        verify(rd).forward(request, response);
    }
	
	@Test
    public void testSearchInvalidEmpty() throws Exception {
        when(request.getParameter("action")).thenReturn( "search" );
        when(request.getParameter("index")).thenReturn( "" );
        when(request.getRequestDispatcher("SearchPageView.jsp")).thenReturn(rd);
        new RedirectionController().service(request, response);
        verify(rd).forward(request, response);
    }
	
	@Test
    public void testSearchInvalidLimit() throws Exception {
        when(request.getParameter("action")).thenReturn( "search" );
        when(request.getParameter("index")).thenReturn( null );
        when(request.getRequestDispatcher("SearchPageView.jsp")).thenReturn(rd);
        new RedirectionController().service(request, response);
        verify(rd).forward(request, response);
    }
	
	
	@Test
    public void testManageListDoNotShow() throws Exception {
        when(request.getParameter("action")).thenReturn( "managelist" );
        when(request.getParameter("index")).thenReturn( index );
        when(request.getParameter("list")).thenReturn( ListTypeEnum.DONOTSHOW.type );
        when(request.getRequestDispatcher("ManageListView.jsp")).thenReturn(rd);
        new RedirectionController().service(request, response);
        verify(rd).forward(request, response);
    }
	
	@Test
    public void testManageListFavorites() throws Exception {
        when(request.getParameter("action")).thenReturn( "managelist" );
        when(request.getParameter("index")).thenReturn( index );
        when(request.getParameter("list")).thenReturn( ListTypeEnum.FAVORITES.type );
        when(request.getParameter("length")).thenReturn( "10" );
        when(request.getParameter("jsonarray")).thenReturn( "jsarray" );
        when(request.getRequestDispatcher("ManageListView.jsp")).thenReturn(rd);
        new RedirectionController().service(request, response);
        verify(rd).forward(request, response);
    }
	
	@Test
    public void testManageListToExplore() throws Exception {
        when(request.getParameter("action")).thenReturn( "managelist" );
        when(request.getParameter("index")).thenReturn( index );
        when(request.getParameter("list")).thenReturn( ListTypeEnum.TOEXPLORE.type );
        when(request.getRequestDispatcher("ManageListView.jsp")).thenReturn(rd);
        new RedirectionController().service(request, response);
        verify(rd).forward(request, response);
    }
	
	@Test
    public void testManageGroceryList() throws Exception {
        when(request.getParameter("action")).thenReturn( "managegrocerylist" );
        when(request.getParameter("index")).thenReturn( index );
        when(request.getParameter("token")).thenReturn( "fakeToken" );
        when(request.getRequestDispatcher("GroceryListView.jsp")).thenReturn(rd);
        new RedirectionController().service(request, response);
        verify(rd).forward(request, response);
    }
	
	@Test
    public void testDeleteGroceryList() throws Exception {
        when(request.getParameter("action")).thenReturn( "deletegrocery" );
        when(request.getParameter("index")).thenReturn( index );
        when(request.getParameter("token")).thenReturn( "fakeToken" );
        when(request.getParameter("item")).thenReturn( "chicken" );
        new RedirectionController().service(request, response);
    }
	
	@Test
    public void testDeleteGroceryListNotCorrectAction() throws Exception {
        when(request.getParameter("action")).thenReturn( "deletegsdfsdrocery" );
        when(request.getParameter("index")).thenReturn( "0" );
        when(request.getParameter("token")).thenReturn( "fakeToken" );
        when(request.getParameter("item")).thenReturn( "chicken" );
        new RedirectionController().service(request, response);
    }
	
	@Test
	public void testRemoveResponseSqlAttack() throws Exception{
		RedirectionController.removeResponse("DROP DATABASE");
	}
	
	
	
	@Test
    public void testRecipes() throws Exception {
        when(request.getParameter("action")).thenReturn( "recipe" );
        when(request.getParameter("index")).thenReturn( index );
        when(request.getParameter("item")).thenReturn( "0" );
        when(request.getRequestDispatcher("DetailedRecipeView.jsp")).thenReturn(rd);
        new RedirectionController().service(request, response);
        verify(rd).forward(request, response);
    }
	
	@Test
    public void testRestaurants() throws Exception {
        when(request.getParameter("action")).thenReturn( "restaurant" );
        when(request.getParameter("index")).thenReturn( index );
        when(request.getParameter("item")).thenReturn( "0" );
        when(request.getRequestDispatcher("DetailedRestaurantView.jsp")).thenReturn(rd);
        new RedirectionController().service(request, response);
        verify(rd).forward(request, response);
    }
	
	@Test
    public void testResults() throws Exception {
        when(request.getParameter("action")).thenReturn( "results" );
        RedirectionController r = new RedirectionController();
        String fakeResponse = RedirectionController.addResponse(rm);
        when(request.getParameter("index")).thenReturn( fakeResponse );
        when(request.getRequestDispatcher("ResultsPageView.jsp")).thenReturn(rd);
        when(request.getParameter("term")).thenReturn( "chicken" );
        when(request.getParameter("length")).thenReturn( "10" );
        when(request.getParameter("token")).thenReturn( "fakeToken" );
        when(request.getSession()).thenReturn(sess);
        when(request.getParameter("jsonarray")).thenReturn( "jsarray" );
        
        r.service(request, response);
        verify(rd).forward(request, response);
    }
	
	@Test
    public void testResultsInvalid() throws Exception {
        when(request.getParameter("action")).thenReturn( "results" );
        RedirectionController r = new RedirectionController();
        String fakeResponse = RedirectionController.addResponse(rm);
        when(request.getParameter("index")).thenReturn( fakeResponse );
        when(request.getRequestDispatcher("ResultsPageView.jsp")).thenReturn(rd);
        when(request.getParameter("term")).thenReturn( "chicken" );
        when(request.getParameter("length")).thenReturn( "10" );
        when(request.getParameter("token")).thenReturn( "error" );
        when(request.getSession()).thenReturn(sess);
        when(request.getParameter("jsonarray")).thenReturn( "jsarray" );
        RedirectionController.tokens.put("error", -1);
        r.service(request, response);
    }
	
	@Test
    public void testErase() throws Exception {
        when(request.getParameter("action")).thenReturn( "erase" );
        when(request.getParameter("index")).thenReturn( "0" );
        new RedirectionController().service(request, response);
    }
	
	@Test
    public void testAddToList() throws Exception {
        when(request.getParameter("action")).thenReturn( "addtolist" );
        when(request.getParameter("index")).thenReturn( index );
        when(request.getParameter("item")).thenReturn( "0" );
        when(request.getParameter("list")).thenReturn( ListTypeEnum.DONOTSHOW.type );
        when(request.getParameter("type")).thenReturn( "restaurant" );
        when(request.getParameter("term")).thenReturn( "chicken" );
        new RedirectionController().service(request, response);
    }
	
	@Test
    public void testAddToListMoveList() throws Exception {
        when(request.getParameter("action")).thenReturn( "movetolist" );
        when(request.getParameter("index")).thenReturn( index );
        when(request.getParameter("item")).thenReturn( "0" );
        when(request.getParameter("list")).thenReturn( ListTypeEnum.DONOTSHOW.type );
        when(request.getParameter("type")).thenReturn( "restaurant" );
        when(request.getParameter("term")).thenReturn( "chicken" );
        new RedirectionController().service(request, response);
    }
	
	@Test
    public void testMoveList() throws Exception {
        when(request.getParameter("action")).thenReturn( "moveplaceinlist" );
        when(request.getParameter("index")).thenReturn( index );
        when(request.getParameter("item")).thenReturn( "0" );
        when(request.getParameter("oldplace")).thenReturn( "1" );
        when(request.getParameter("newplace")).thenReturn( "2" );
        when(request.getParameter("list")).thenReturn( ListTypeEnum.DONOTSHOW.type );
        when(request.getParameter("type")).thenReturn( "restaurant" );
        when(request.getParameter("term")).thenReturn( "chicken" );
        new RedirectionController().service(request, response);
    }
	
	@Test
    public void testAddGrocery() throws Exception {
        when(request.getParameter("action")).thenReturn( "addtogrocery" );
        when(request.getParameter("index")).thenReturn( index );
        when(request.getParameter("token")).thenReturn( "fakeToken" );
        when(request.getParameter("item")).thenReturn( "0" );
        when(request.getParameter("ingredientindex")).thenReturn( "0" );
        when(request.getParameter("type")).thenReturn( "restaurant" );
        when(request.getParameter("term")).thenReturn( "chicken" );
        new RedirectionController().service(request, response);
    }
	
	@Test
    public void testAddGrocerySQL() throws Exception {
        when(request.getParameter("action")).thenReturn( "addtogrocery" );
        when(request.getParameter("index")).thenReturn( "DROP DATABASE" );
        when(request.getParameter("token")).thenReturn( "fakeToken" );
        when(request.getParameter("item")).thenReturn( "0" );
        when(request.getParameter("ingredientindex")).thenReturn( "0" );
        when(request.getParameter("type")).thenReturn( "restaurant" );
        when(request.getParameter("term")).thenReturn( "chicken" );
        new RedirectionController().service(request, response);
    }
	
	@Test
    public void testAddGroceryInvalid() throws Exception {
        when(request.getParameter("action")).thenReturn( "addtogrocery" );
        when(request.getParameter("index")).thenReturn( "-1" );
        when(request.getParameter("token")).thenReturn( "fakeToken" );
        when(request.getParameter("item")).thenReturn( "0" );
        when(request.getParameter("ingredientindex")).thenReturn( "-1" );
        when(request.getParameter("type")).thenReturn( "restaurant" );
        when(request.getParameter("term")).thenReturn( "chicken" );
        new RedirectionController().service(request, response);
    }
	
	@Test
    public void testRemoveFromList() throws Exception {
        when(request.getParameter("action")).thenReturn( "removefromlist" );
        when(request.getParameter("index")).thenReturn( index );
        when(request.getParameter("item")).thenReturn( "0" );
        when(request.getParameter("list")).thenReturn( ListTypeEnum.DONOTSHOW.type );
        when(request.getParameter("type")).thenReturn( "restaurant" );
        when(request.getParameter("term")).thenReturn( "chicken" );
        new RedirectionController().service(request, response);
    }
	
	@Test
    public void testResultsList() throws Exception {
        when(request.getParameter("action")).thenReturn( "results" );
        when(request.getParameter("term")).thenReturn( "chicken" );
        when(request.getParameter("index")).thenReturn( "1" );
        when(request.getParameter("item")).thenReturn( "0" );
        when(request.getParameter("list")).thenReturn( ListTypeEnum.DONOTSHOW.type );
        when(request.getParameter("type")).thenReturn( "restaurant" );
    }
	
	@Test
	public void testSqlAttack() throws Exception{
		RedirectionController.getDirectReference("DROP DATABASE");
	}
	
	
	
	
	
	
	

}
