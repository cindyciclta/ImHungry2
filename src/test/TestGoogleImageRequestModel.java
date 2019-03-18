package test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import models.CollageGenerationModel;
import models.GoogleImageRequestModel;
import models.ResponseCodeModel;

public class TestGoogleImageRequestModel extends Mockito{

	private static GoogleImageRequestModel googleImage;
	private static CollageGenerationModel collage;
	
	@Mock
	GoogleImageRequestModel googleMock;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		collage = new CollageGenerationModel();
		googleImage = new GoogleImageRequestModel(collage);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() {
		googleImage.APIImageSearch("pasta");
		MockitoAnnotations.initMocks(this);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void invalidSearchTest() {
		googleImage.APIImageSearch(" ");
	}
	
	@Test
	public void testBadResponse() throws Exception{
		when(googleMock.getResponse("pasta")).thenThrow(new IOException());
		assertTrue(collage.checkParameters("pasta", 5));
		assertEquals(null, googleMock.APIImageSearch("pasta"));
	}
	
	@Test
	public void testEmptyResponse() throws Exception{
		when(googleMock.getResponse("pasta")).thenReturn("");
		assertEquals(null, googleMock.APIImageSearch("pasta"));
	}
	
	@Test
	public void testEmptyTermCollageParm() throws Exception{
		assertFalse(collage.checkParameters("", 5));
	}
	
	
	@Test
	public void testNegativeLimitCollageParm() throws Exception{
		assertFalse(collage.checkParameters("Pasta", -1));
	}
	
	@Test
	public void testNulltermCollageParm() throws Exception{
		assertFalse(collage.checkParameters(null, 3));
	}
	
	@Test
	public void testCollageGetArray() throws Exception{
		ArrayList<String> s = collage.getList();
		assertNotEquals(null, s);
	}
	
	@Test
	public void testCollageGetSize() throws Exception{
		int size = collage.getActualSize();
		assertEquals(10, size);
	}
	
	@Test
	public void testSetListOfImages() throws Exception{
		collage.setListofImages(new ArrayList<String>(), 0);
		int size = collage.getActualSize();
		assertEquals(0, size);
	}

}
