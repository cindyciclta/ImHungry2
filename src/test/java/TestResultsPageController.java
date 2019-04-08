package test.java;

import controllers.RedirectionController;
import controllers.ResultsPageController;
import models.DatabaseModel;

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

public class TestResultsPageController extends Mockito{
	
	@Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @Mock
    RequestDispatcher rd;
    
    @Mock
    HttpSession sess;

    private static final String username = "testresult";
    private static int id;
    
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		String password = "test";
		
		assertTrue(DatabaseModel.insertUser(username, password.toCharArray()));
		id = DatabaseModel.GetUserID(username);
		RedirectionController.tokens.put("fakeToken", id);
	
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception{
		TestDatabaseModel.deleteUser(username);
	}
	
	@Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        
    }

	@Test
    public void testInvalidAction() throws Exception {
        when(request.getParameter("action")).thenReturn( null );
        when(request.getParameter("token")).thenReturn( "fakeToken" );
        when(request.getRequestDispatcher("SearchPageController")).thenReturn(rd);
        new ResultsPageController().service(request, response);
        verify(rd).forward(request, response);
    }
	
	@Test
    public void testInvalidActionInvalidToken() throws Exception {
        when(request.getParameter("action")).thenReturn( null );
        when(request.getParameter("token")).thenReturn( null );
        when(request.getRequestDispatcher("SignInView.jsp")).thenReturn(rd);
        new ResultsPageController().service(request, response);
        verify(rd).forward(request, response);
    }
	
	@Test
    public void testInvalidActionEmpty() throws Exception {
        when(request.getParameter("action")).thenReturn( "" );
        when(request.getParameter("token")).thenReturn( "fakeToken" );
        when(request.getRequestDispatcher("SearchPageController")).thenReturn(rd);
        new ResultsPageController().service(request, response);
        verify(rd).forward(request, response);
    }
	
	@Test
    public void testSearchInvalid() throws Exception {
        when(request.getParameter("action")).thenReturn( "results" );
        when(request.getParameter("token")).thenReturn( "fakeToken" );
        when(request.getParameter("term")).thenReturn( null );
        when(request.getParameter("limit")).thenReturn( "5" );
        when(request.getRequestDispatcher("SearchPageController")).thenReturn(rd);
        new ResultsPageController().service(request, response);
        verify(rd).forward(request, response);
    }
	
	@Test
    public void testSearchInvalidEmpty() throws Exception {
        when(request.getParameter("action")).thenReturn( "results" );
        when(request.getParameter("token")).thenReturn( "fakeToken" );
        when(request.getParameter("term")).thenReturn( "" );
        when(request.getParameter("limit")).thenReturn( "5" );
        when(request.getRequestDispatcher("SearchPageController")).thenReturn(rd);
        new ResultsPageController().service(request, response);
        verify(rd).forward(request, response);
    }
	
	@Test
    public void testSearchInvalidLimit() throws Exception {
        when(request.getParameter("action")).thenReturn( "results" );
        when(request.getParameter("token")).thenReturn( "fakeToken" );
        when(request.getParameter("term")).thenReturn( "chicken" );
        when(request.getParameter("limit")).thenReturn( null );
        when(request.getRequestDispatcher("SearchPageController")).thenReturn(rd);
        new ResultsPageController().service(request, response);
        verify(rd).forward(request, response);
    }
	
	@Test
    public void testSearchRemoveOld() throws Exception {
        when(request.getParameter("action")).thenReturn( "" );
        when(request.getParameter("token")).thenReturn( "fakeToken" );
        when(request.getParameter("index")).thenReturn( "0" );
        when(request.getRequestDispatcher("SearchPageController")).thenReturn(rd);
        new ResultsPageController().service(request, response);
        verify(rd).forward(request, response);
    }
	
	@Test
    public void testSearchRemoveNoIndex() throws Exception {
        when(request.getParameter("action")).thenReturn( "" );
        when(request.getParameter("token")).thenReturn( "fakeToken" );
        when(request.getParameter("index")).thenReturn( null );
        when(request.getRequestDispatcher("SearchPageController")).thenReturn(rd);
        new ResultsPageController().service(request, response);
        verify(rd).forward(request, response);
    }
	
	@Test
    public void testSearchInvalidLimitEmpty() throws Exception {
        when(request.getParameter("action")).thenReturn( "results" );
        when(request.getParameter("term")).thenReturn( "chicken" );
        when(request.getParameter("token")).thenReturn( "fakeToken" );
        when(request.getParameter("limit")).thenReturn( "" );
        when(request.getRequestDispatcher("SearchPageController")).thenReturn(rd);
        new ResultsPageController().service(request, response);
        verify(rd).forward(request, response);
    }
	
	@Test
    public void testSearchValid() throws Exception {
        when(request.getParameter("action")).thenReturn( "results" );
        when(request.getParameter("term")).thenReturn( "chicken" );
        when(request.getParameter("token")).thenReturn( "fakeToken" );
        when(request.getSession()).thenReturn(sess);
        when(request.getParameter("limit")).thenReturn( "5" );
        when(request.getParameter("radius")).thenReturn( "1000" );
        when(request.getRequestDispatcher("ResultsPageView.jsp")).thenReturn(rd);
        when(request.getRequestDispatcher("SearchPageView.jsp")).thenReturn(rd);
        new ResultsPageController().service(request, response);
        verify(rd).forward(request, response);
    }
	
	@Test
    public void testSearchInvalidActionResults() throws Exception {
        when(request.getParameter("action")).thenReturn( "fakeResults" );
        when(request.getParameter("term")).thenReturn( "chicken" );
        when(request.getParameter("token")).thenReturn( "fakeToken" );
        when(request.getParameter("limit")).thenReturn( "5" );
        when(request.getParameter("radius")).thenReturn( "1000" );
        when(request.getRequestDispatcher("SignInView.jsp")).thenReturn(rd);
        new ResultsPageController().service(request, response);
        verify(rd).forward(request, response);
    }
	
	@Test
    public void invalidParamValid() throws Exception {
        when(request.getParameter("action")).thenReturn( "results" );
        when(request.getParameter("term")).thenReturn( "chicken" );
        when(request.getParameter("limit")).thenReturn( "-5" );
        when(request.getParameter("token")).thenReturn( "fakeToken" );
        when(request.getParameter("radius")).thenReturn( "1000" );
        when(request.getRequestDispatcher("SearchPageView.jsp")).thenReturn(rd);
        new ResultsPageController().service(request, response);
        verify(rd).forward(request, response);
    }
	
	@Test
	public void nullToken() throws Exception {
		when(request.getParameter("action")).thenReturn( "results" );
        when(request.getParameter("term")).thenReturn( "chicken" );
        when(request.getParameter("limit")).thenReturn( "5" );
        when(request.getParameter("token")).thenReturn( "");
        when(request.getParameter("radius")).thenReturn( "1000" );
        when(request.getRequestDispatcher("SignInView.jsp")).thenReturn(rd);
        new ResultsPageController().service(request, response);
        verify(rd).forward(request, response);
	}
}
