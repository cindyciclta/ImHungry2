package test.java;

import controllers.RedirectionController;
import controllers.SearchPageController;
import models.DatabaseModel;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

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

public class TestSearchPageController extends Mockito{
	
	@Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @Mock
    RequestDispatcher rd;
    
    public static int id = -1;
    public static final String username = "search";
    public static final String password = "search";
    public static final String token = "searchToken";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		if(!DatabaseModel.userExists(username)) {
			DatabaseModel.insertUser(username, password.toCharArray());
			id = DatabaseModel.signInUser(username, password.toCharArray()).getId();
			RedirectionController.tokens.put(token, id);
		}
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		// Delete added user
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = DatabaseModel.getConnection();
		
		
		// clean the searches database as well
		int id = DatabaseModel.GetUserID(username);
		
		String query = "DELETE from searches where user_id = (?)";
		PreparedStatement preparedStmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
	    preparedStmt.setInt (1, id);
	    preparedStmt.executeUpdate();
	    preparedStmt.close();
		
		// the mysql insert statement to have date of upload
		query = "DELETE from users where user_name = (?)";
		preparedStmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
	    preparedStmt.setString (1, username);
	    preparedStmt.executeUpdate();
	    
	    RedirectionController.tokens.remove(token);
	}
	
	@Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

	@Test
    public void testInvalidAction() throws Exception {
        when(request.getParameter("action")).thenReturn( null );
        when(request.getParameter("token")).thenReturn( token );
        when(request.getRequestDispatcher("SearchPageView.jsp")).thenReturn(rd);
        new SearchPageController().service(request, response);
        verify(rd).forward(request, response);
    }
	
	@Test
    public void testInvalidActionEmpty() throws Exception {
        when(request.getParameter("action")).thenReturn( "" );
        when(request.getParameter("token")).thenReturn( token );
        when(request.getRequestDispatcher("SearchPageView.jsp")).thenReturn(rd);
        new SearchPageController().service(request, response);
        verify(rd).forward(request, response);
    }
	
	@Test
    public void testActionRedirect() throws Exception {
		when(request.getParameter("token")).thenReturn( token );
        when(request.getParameter("action")).thenReturn( "redirect" );
        when(request.getRequestDispatcher("SearchPageView.jsp")).thenReturn(rd);
        new SearchPageController().service(request, response);
        verify(rd).forward(request, response);
    }
	
	@Test
    public void testSearchInvalid() throws Exception {
		when(request.getParameter("token")).thenReturn( token );
        when(request.getParameter("action")).thenReturn( "search" );
        when(request.getParameter("term")).thenReturn( null );
        when(request.getParameter("limit")).thenReturn( "5" );
        when(request.getRequestDispatcher("SearchPageView.jsp")).thenReturn(rd);
        new SearchPageController().service(request, response);
        verify(rd).forward(request, response);
    }
	
	@Test
    public void testSearchInvalidEmpty() throws Exception {
		when(request.getParameter("token")).thenReturn( token );
        when(request.getParameter("action")).thenReturn( "search" );
        when(request.getParameter("term")).thenReturn( "" );
        when(request.getParameter("limit")).thenReturn( "5" );
        when(request.getRequestDispatcher("SearchPageView.jsp")).thenReturn(rd);
        new SearchPageController().service(request, response);
        verify(rd).forward(request, response);
    }
	
	@Test
    public void testSearchInvalidLimit() throws Exception {
        when(request.getParameter("token")).thenReturn( token );
        when(request.getParameter("action")).thenReturn( "search" );
        when(request.getParameter("term")).thenReturn( "chicken" );
        when(request.getParameter("limit")).thenReturn( null );
        when(request.getRequestDispatcher("SearchPageView.jsp")).thenReturn(rd);
        new SearchPageController().service(request, response);
        verify(rd).forward(request, response);
    }
	
	@Test
    public void testSearchInvalidLimitEmpty() throws Exception {

        when(request.getParameter("token")).thenReturn( token );
        when(request.getParameter("action")).thenReturn( "search" );
        when(request.getParameter("term")).thenReturn( "chicken" );
        when(request.getParameter("limit")).thenReturn( "" );
        when(request.getRequestDispatcher("SearchPageView.jsp")).thenReturn(rd);
        new SearchPageController().service(request, response);
        verify(rd).forward(request, response);
    }
	
	@Test
    public void testSearchValid() throws Exception {

        when(request.getParameter("token")).thenReturn( token );
        when(request.getParameter("action")).thenReturn( "search" );
        when(request.getParameter("term")).thenReturn( "chicken" );
        when(request.getParameter("limit")).thenReturn( "5" );
        when(request.getParameter("radius")).thenReturn( "1000" );
        when(request.getParameter("reci_page")).thenReturn( "1" );
        when(request.getParameter("rest_page")).thenReturn( "1" );
        when(request.getRequestDispatcher("ResultsPageController?action=results&term=chicken&limit=5&radius=1000&rest_page=1&reci_page=1&token=" + token)).thenReturn(rd);
        new SearchPageController().service(request, response);
        verify(rd).forward(request, response);
    }
	
	@Test
	public void testInvalidToken() throws Exception{
		when(request.getParameter("token")).thenReturn( "dfsdfsd" );
        when(request.getParameter("action")).thenReturn( "search" );
        when(request.getParameter("term")).thenReturn( "chicken" );
        when(request.getParameter("limit")).thenReturn( "5" );
        when(request.getRequestDispatcher("SignInView.jsp")).thenReturn(rd);
        new SearchPageController().service(request, response);
        verify(rd).forward(request, response);
	}
	
	@Test
	public void testNullToken() throws Exception{
		when(request.getParameter("token")).thenReturn( null );
        when(request.getParameter("action")).thenReturn( "search" );
        when(request.getParameter("term")).thenReturn( "chicken" );
        when(request.getParameter("limit")).thenReturn( "5" );
        when(request.getRequestDispatcher("SignInView.jsp")).thenReturn(rd);
        new SearchPageController().service(request, response);
        verify(rd).forward(request, response);
	}
	
	@Test
	public void testInvalidActionParameter() throws Exception{
		when(request.getParameter("token")).thenReturn( token );
        when(request.getParameter("action")).thenReturn( "sdfsdf" );
        when(request.getParameter("term")).thenReturn( "chicken" );
        when(request.getParameter("limit")).thenReturn( "5" );
        when(request.getRequestDispatcher("SearchPageView.jsp")).thenReturn(rd);
        new SearchPageController().service(request, response);
        verify(rd).forward(request, response);	
	}
	
	@Test
	public void testExceptionInAdding() throws Exception{
		when(request.getParameter("token")).thenReturn( token );
        when(request.getParameter("action")).thenReturn( "search" );
        when(request.getParameter("term")).thenReturn( "chicken" );
        when(request.getParameter("limit")).thenReturn( "5asdf" );
        when(request.getRequestDispatcher("SearchPageView.jsp")).thenReturn(rd);
        new SearchPageController().service(request, response);
        verify(rd).forward(request, response);	
	}
	
	@Test
	public void testExceptionInGetting() throws Exception{
		RedirectionController.tokens.put("123123123", -1);
		when(request.getParameter("token")).thenReturn( "123123123" );
        when(request.getParameter("action")).thenReturn( "search" );
        when(request.getParameter("term")).thenReturn( "chicken" );
        when(request.getParameter("limit")).thenReturn( "5" );
        when(request.getRequestDispatcher("SearchPageView.jsp")).thenReturn(rd);
        new SearchPageController().service(request, response);
        verify(rd).forward(request, response);	
	}
	

	
	
	

}
