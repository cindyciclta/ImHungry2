package test.java;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import controllers.SignInController;
import models.DatabaseModel;

public class TestSignInController extends Mockito{
	
	@Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @Mock
    RequestDispatcher rd;

    public static final String username = "signin";
	public static final String password = "signin";
    
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		if(!DatabaseModel.userExists(username)) {
			DatabaseModel.insertUser(username, password.toCharArray());
		}
	}
	
	@Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

	@Test
    public void testSignInValid() throws Exception {
        when(request.getParameter("username")).thenReturn( username );
        when(request.getParameter("password")).thenReturn( password );
        when(request.getRequestDispatcher("SearchPageController")).thenReturn(rd);
        new SignInController().service(request, response);
        verify(rd).forward(request, response);
    }
	
	@Test
    public void testSignInValidEmpty() throws Exception {
        when(request.getParameter("username")).thenReturn( username );
        when(request.getParameter("password")).thenReturn( "" );
        when(request.getRequestDispatcher("SignInView.jsp")).thenReturn(rd);
        new SignInController().service(request, response);
        verify(rd).forward(request, response);
    }
	
	@Test
    public void testSignInNoExist() throws Exception {
        when(request.getParameter("username")).thenReturn( "aaaah" );
        when(request.getParameter("password")).thenReturn( password );
        when(request.getRequestDispatcher("SignInView.jsp")).thenReturn(rd);
        new SignInController().service(request, response);
        verify(rd).forward(request, response);
    }
	
	@Test
    public void testSignInError() throws Exception {
        when(request.getParameter("username")).thenReturn( "" );
        when(request.getParameter("password")).thenReturn( "" );
        when(request.getRequestDispatcher("SignInView.jsp")).thenReturn(rd);
        new SignInController().service(request, response);
        verify(rd).forward(request, response);
    }
	
	@Test
    public void testSignInErrorEmpty() throws Exception {
        when(request.getParameter("username")).thenReturn( "" );
        when(request.getParameter("password")).thenReturn( "sdf" );
        when(request.getRequestDispatcher("SignInView.jsp")).thenReturn(rd);
        new SignInController().service(request, response);
        verify(rd).forward(request, response);
    }
	
	@Test
    public void testSignInErrorEmpty2() throws Exception {
        when(request.getParameter("username")).thenReturn( "sdf" );
        when(request.getParameter("password")).thenReturn( "" );
        when(request.getRequestDispatcher("SignInView.jsp")).thenReturn(rd);
        new SignInController().service(request, response);
        verify(rd).forward(request, response);
    }
	
	@Test
    public void testSignInErrorNull() throws Exception {
        when(request.getParameter("username")).thenReturn( null );
        when(request.getParameter("password")).thenReturn( "" );
        when(request.getRequestDispatcher("SignInView.jsp")).thenReturn(rd);
        new SignInController().service(request, response);
        verify(rd).forward(request, response);
    }
}
