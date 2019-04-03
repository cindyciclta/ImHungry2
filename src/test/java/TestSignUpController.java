package test.java;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import controllers.SignUpController;
import models.DatabaseModel;

public class TestSignUpController extends Mockito{
	
	@Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @Mock
    RequestDispatcher rd;
	
	public static final String username = "signup";
	public static final String password = "signin";
	
	@Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }
	
	@After
	public void tearDown() throws Exception{
		
	}
	
	public static void deleteUser() throws Exception{
		// Delete added user
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = DatabaseModel.getConnection();
		
		// the mysql insert statement to have date of upload
		String query = "DELETE from users where user_name = (?)";
		PreparedStatement preparedStmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
	    preparedStmt.setString (1, username);
	    preparedStmt.executeUpdate();
	}
	
	@Test
	public void testNullPassword() throws Exception{
		when(request.getParameter("username")).thenReturn( username );
        when(request.getParameter("password")).thenReturn( null );
        when(request.getParameter("confirm_password")).thenReturn( password );
        when(request.getRequestDispatcher("SignUpView.jsp")).thenReturn(rd);
        new SignUpController().service(request, response);
        verify(rd).forward(request, response);
	}
	
	@Test
	public void testEmptyPassword() throws Exception{
		when(request.getParameter("username")).thenReturn( username );
        when(request.getParameter("password")).thenReturn( "" );
        when(request.getParameter("confirm_password")).thenReturn( "" );
        when(request.getRequestDispatcher("SignUpView.jsp")).thenReturn(rd);
        new SignUpController().service(request, response);
        verify(rd).forward(request, response);
	}
	
	@Test
	public void testEmptyUsername() throws Exception{
		when(request.getParameter("username")).thenReturn( "" );
        when(request.getParameter("password")).thenReturn( password );
        when(request.getParameter("confirm_password")).thenReturn( password );
        when(request.getRequestDispatcher("SignUpView.jsp")).thenReturn(rd);
        new SignUpController().service(request, response);
        verify(rd).forward(request, response);
	}
	
	@Test
	public void testNullConfirm() throws Exception{
		when(request.getParameter("username")).thenReturn( username );
        when(request.getParameter("password")).thenReturn( password );
        when(request.getParameter("confirm_password")).thenReturn( null );
        when(request.getRequestDispatcher("SignUpView.jsp")).thenReturn(rd);
        new SignUpController().service(request, response);
        verify(rd).forward(request, response);
		
	}
	
	@Test
	public void testNotSamePassword() throws Exception{
		when(request.getParameter("username")).thenReturn( username );
        when(request.getParameter("password")).thenReturn( password );
        when(request.getParameter("confirm_password")).thenReturn( "aaaah" );
        when(request.getRequestDispatcher("SignUpView.jsp")).thenReturn(rd);
        new SignUpController().service(request, response);
        verify(rd).forward(request, response);
	}
	
	@Test
	public void testSignUpNewUser() throws Exception{
		when(request.getParameter("username")).thenReturn( username );
        when(request.getParameter("password")).thenReturn( password );
        when(request.getParameter("confirm_password")).thenReturn( password );
        when(request.getRequestDispatcher("SearchPageController")).thenReturn(rd);
        new SignUpController().service(request, response);
        verify(rd).forward(request, response);
		deleteUser();	
	}
	
	@Test
	public void testError() throws Exception{
		when(request.getParameter("username")).thenReturn( null );
        when(request.getParameter("password")).thenReturn( password );
        when(request.getParameter("confirm_password")).thenReturn( password );
        when(request.getRequestDispatcher("SignUpView.jsp")).thenReturn(rd);
        new SignUpController().service(request, response);
        verify(rd).forward(request, response);
		deleteUser();	
	}
	
	@Test
	public void testSignUpExisting() throws Exception{
		
		if(!DatabaseModel.userExists(username)) {
			DatabaseModel.insertUser(username, password.toCharArray());
		}
		
		when(request.getParameter("username")).thenReturn( username );
        when(request.getParameter("password")).thenReturn( password );
        when(request.getParameter("confirm_password")).thenReturn( password );
        when(request.getRequestDispatcher("SignUpView.jsp")).thenReturn(rd);
        new SignUpController().service(request, response);
        verify(rd).forward(request, response);
        
        deleteUser();
	}
	

}
