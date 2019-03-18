package test;

import controllers.RedirectionController;
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
    
    private static ResponseModel rm;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		rm = new ResponseModel();
		rm.checkParameters("chicken", 2);
		assertTrue(rm.getSearchResults());
		RedirectionController.addResponse(rm);
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
        when(request.getParameter("index")).thenReturn( "0" );
        when(request.getParameter("list")).thenReturn( "donotshow" );
        when(request.getRequestDispatcher("ManageListView.jsp")).thenReturn(rd);
        new RedirectionController().service(request, response);
        verify(rd).forward(request, response);
    }
	
	@Test
    public void testManageListFavorites() throws Exception {
        when(request.getParameter("action")).thenReturn( "managelist" );
        when(request.getParameter("index")).thenReturn( "0" );
        when(request.getParameter("list")).thenReturn( "favorites" );
        when(request.getParameter("length")).thenReturn( "10" );
        when(request.getParameter("jsonarray")).thenReturn( "jsarray" );
        when(request.getRequestDispatcher("ManageListView.jsp")).thenReturn(rd);
        new RedirectionController().service(request, response);
        verify(rd).forward(request, response);
    }
	
	@Test
    public void testManageListToExplore() throws Exception {
        when(request.getParameter("action")).thenReturn( "managelist" );
        when(request.getParameter("index")).thenReturn( "0" );
        when(request.getParameter("list")).thenReturn( "toexplore" );
        when(request.getRequestDispatcher("ManageListView.jsp")).thenReturn(rd);
        new RedirectionController().service(request, response);
        verify(rd).forward(request, response);
    }
	
	@Test
    public void testRecipes() throws Exception {
        when(request.getParameter("action")).thenReturn( "recipe" );
        when(request.getParameter("index")).thenReturn( "1" );
        when(request.getParameter("item")).thenReturn( "0" );
        when(request.getRequestDispatcher("DetailedRecipeView.jsp")).thenReturn(rd);
        new RedirectionController().service(request, response);
        verify(rd).forward(request, response);
    }
	
	@Test
    public void testRestaurants() throws Exception {
        when(request.getParameter("action")).thenReturn( "restaurant" );
        when(request.getParameter("index")).thenReturn( "1" );
        when(request.getParameter("item")).thenReturn( "0" );
        when(request.getRequestDispatcher("DetailedRestaurantView.jsp")).thenReturn(rd);
        new RedirectionController().service(request, response);
        verify(rd).forward(request, response);
    }
	
	@Test
    public void testResults() throws Exception {
        when(request.getParameter("action")).thenReturn( "results" );
        when(request.getParameter("index")).thenReturn( "0" );
        when(request.getRequestDispatcher("ResultsPageView.jsp")).thenReturn(rd);
        when(request.getParameter("term")).thenReturn( "chicken" );
        when(request.getParameter("length")).thenReturn( "10" );
        when(request.getParameter("jsonarray")).thenReturn( "jsarray" );
        RedirectionController r = new RedirectionController();
        RedirectionController.responses.put(0, new ResponseModel());
        r.service(request, response);
        verify(rd).forward(request, response);
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
        when(request.getParameter("index")).thenReturn( "1" );
        when(request.getParameter("item")).thenReturn( "0" );
        when(request.getParameter("list")).thenReturn( "donotshow" );
        when(request.getParameter("type")).thenReturn( "restaurant" );
        when(request.getParameter("term")).thenReturn( "chicken" );
        new RedirectionController().service(request, response);
    }
	
	@Test
    public void testRemoveFromList() throws Exception {
        when(request.getParameter("action")).thenReturn( "removefromlist" );
        when(request.getParameter("index")).thenReturn( "1" );
        when(request.getParameter("item")).thenReturn( "0" );
        when(request.getParameter("list")).thenReturn( "donotshow" );
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
        when(request.getParameter("list")).thenReturn( "donotshow" );
        when(request.getParameter("type")).thenReturn( "restaurant" );
    
    }
	
	
	
	
	
	
	

}
