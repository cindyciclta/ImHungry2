package test.java;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import models.GoogleMapsRequestModel;

public class TestGoogleDistanceRequestModel {

	
	@Test
	public void testGoogleDistanceRequestModel() {
		GoogleMapsRequestModel gm = new GoogleMapsRequestModel();
		assertTrue(gm.checkParameters(34.1376576, -118.1274577));
		assertEquals(27, gm.getDrivingTime());
	}
	
	@Test
	public void testGoogleDistanceRequestModelBadGps() {
		GoogleMapsRequestModel gm = new GoogleMapsRequestModel();
		assertTrue(gm.checkParameters(0, -118.1274577));
		assertEquals(-1, gm.getDrivingTime());
	}
}
