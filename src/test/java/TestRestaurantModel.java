package test.java;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

import models.ListTypeEnum;
import models.RestaurantModel;

public class TestRestaurantModel {

	@Test
	public void testSetName() {
		RestaurantModel rm = new RestaurantModel();
		assertTrue(rm.setName("name"));
	}
	
	@Test
	public void testSetNameNull() {
		RestaurantModel rm = new RestaurantModel();
		assertFalse(rm.setName(null));
	}
	
	@Test
	public void testSetNameEmpty() {
		RestaurantModel rm = new RestaurantModel();
		assertFalse(rm.setName("   "));
	}
	
	@Test
	public void testSetStars() {
		RestaurantModel rm = new RestaurantModel();
		assertTrue(rm.setStars(4));
	}
	
	@Test
	public void testSetStarsNegative() {
		RestaurantModel rm = new RestaurantModel();
		assertFalse(rm.setStars(-1));
	}
	
	@Test
	public void testSetStarsOverFive() {
		RestaurantModel rm = new RestaurantModel();
		assertFalse(rm.setStars(6));
	}
	
	
	@Test
	public void testFormattedResultsDetails() {
		RestaurantModel rm = new RestaurantModel();
		rm.setName("nameOfRestaurant");
		Map<String, String> res = rm.getFormattedFieldsForDetailsPage();
		assertEquals(res.get("name"), "nameOfRestaurant");
	}
	
	@Test
	public void testFormattedResults() {
		RestaurantModel rm = new RestaurantModel();
		rm.setStars(3);
		rm.setName("nameOfRestaurant");
		Map<String, String> res = rm.getFormattedFieldsForResultsPage();
		assertEquals(res.get("name"), "nameOfRestaurant");
	}
	
	@Test
	public void testSetPriceRange() {
		RestaurantModel rm = new RestaurantModel();	
		assertTrue(rm.setPriceRange(10, 100));
	}
	
	@Test
	public void testSetPriceRangeNegative() {
		RestaurantModel rm = new RestaurantModel();	
		assertFalse(rm.setPriceRange(-10, 100));
	}
	
	@Test
	public void testSetPriceRangeInvalid() {
		RestaurantModel rm = new RestaurantModel();	
		assertFalse(rm.setPriceRange(10, 1));
	}
	
	@Test
	public void testModifierDoNotShow() {
		RestaurantModel rm = new RestaurantModel();
		rm.setInDoNotShow(true);
		Map<String, String> res = rm.getFormattedFieldsForResultsPage();
		assertEquals(ListTypeEnum.DONOTSHOW.type, res.get("modifier"));
	}
	
	@Test
	public void testModifierFavorites() {
		RestaurantModel rm = new RestaurantModel();
		rm.setInFavorites(true);
		Map<String, String> res = rm.getFormattedFieldsForResultsPage();
		assertEquals(ListTypeEnum.FAVORITES.type, res.get("modifier"));
	}
	
	@Test
	public void testToExplore() {
		RestaurantModel rm = new RestaurantModel();
		rm.setInToExplore(true);
		Map<String, String> res = rm.getFormattedFieldsForResultsPage();
		assertEquals(ListTypeEnum.TOEXPLORE.type, res.get("modifier"));
	}
	
	@Test
	public void testSetLatLong() {
		RestaurantModel rm = new RestaurantModel();	
		assertTrue(rm.setLatLong(10, 10));
	}
	
	@Test
	public void testSetPhoneNull() {
		RestaurantModel rm = new RestaurantModel();	
		assertFalse(rm.setPhone(null));
	}
	
	@Test
	public void testSetPhoneEmpty() {
		RestaurantModel rm = new RestaurantModel();	
		assertFalse(rm.setPhone(" "));
	}
	
	@Test
	public void testSetPhone() {
		RestaurantModel rm = new RestaurantModel();	
		assertTrue(rm.setPhone("17818505023"));
	}
	
	@Test
	public void testSetCaption() {
		RestaurantModel rm = new RestaurantModel();	
		assertTrue(rm.setAddress("123 main st"));
	}
	
	@Test
	public void testSetLinkToPage() {
		RestaurantModel rm = new RestaurantModel();	
		assertTrue(rm.setLinkToPage("google.com"));
	}
	
	@Test
	public void testSetDrivingTime() {
		RestaurantModel rm = new RestaurantModel();	
		assertTrue(rm.setDrivingTime(10));
	}
	
	@Test
	public void testSetDrivingTimeInvalid() {
		RestaurantModel rm = new RestaurantModel();	
		assertFalse(rm.setDrivingTime(-10));
	}
	
	@Test
	public void testCompareToLower() {
		RestaurantModel rm = new RestaurantModel();	
		RestaurantModel rmHigher = new RestaurantModel();
		rm.setDrivingTime(1);
		rmHigher.setDrivingTime(2);
		
		assertEquals(-1, rm.compareTo(rmHigher));
	}
	
	@Test
	public void testCompareToEquals() {
		RestaurantModel rm = new RestaurantModel();	
		RestaurantModel rmEqual = new RestaurantModel();
		rm.setDrivingTime(1);
		rmEqual.setDrivingTime(1);
		
		assertEquals(0, rm.compareTo(rmEqual));
	}
	
	@Test
	public void testCompareToHigher() {
		RestaurantModel rm = new RestaurantModel();	
		RestaurantModel rmHigher = new RestaurantModel();
		rm.setDrivingTime(1);
		rmHigher.setDrivingTime(2);
		
		assertEquals(1, rmHigher.compareTo(rm));
	}
	
	@Test
	public void testThisFavorite() {
		RestaurantModel rm = new RestaurantModel();
		rm.setInFavorites(true);
		
		RestaurantModel rmFavorite = new RestaurantModel();
		rmFavorite.setInFavorites(false);
		
		assertEquals(1, rmFavorite.compareTo(rm));
	}
	
	@Test
	public void testOtherFavorite() {
		RestaurantModel rm = new RestaurantModel();
		rm.setInFavorites(true);
		
		RestaurantModel rmFavorite = new RestaurantModel();
		rmFavorite.setInFavorites(false);
		
		assertEquals(-1, rm.compareTo(rmFavorite));
	}
	
	@Test
	public void testBothFavorite() {
		RestaurantModel rm = new RestaurantModel();
		rm.setInFavorites(true);
		rm.setDrivingTime(1);
		
		RestaurantModel rmFavorite = new RestaurantModel();
		rmFavorite.setInFavorites(true);
		rmFavorite.setDrivingTime(2);
		
		assertEquals(-1, rm.compareTo(rmFavorite));
	}
	
	@Test
	public void testNeitherFavorite() {
		RestaurantModel rm = new RestaurantModel();
		rm.setInFavorites(false);
		rm.setDrivingTime(1);
		
		RestaurantModel rmFavorite = new RestaurantModel();
		rmFavorite.setInFavorites(false);
		rmFavorite.setDrivingTime(2);
		
		assertEquals(-1, rm.compareTo(rmFavorite));
	}
	
	@Test
	public void testEqualsNull() {
		RestaurantModel rm = new RestaurantModel();
		assertFalse(rm.equals(null));
	}
	
	@Test
	public void testEqualsWrongClass() {
		RestaurantModel rm = new RestaurantModel();
		assertFalse(rm.equals("restaurant"));
	}
	
	@Test
	public void testEqualsNullName() {
		RestaurantModel rm = new RestaurantModel();
		rm.setName("sdfs");
		assertFalse(rm.equals(new RestaurantModel()));
	}
	
	@Test
	public void testEqualsNullNameOther() {
		RestaurantModel rm = new RestaurantModel();
		RestaurantModel rm2 = new RestaurantModel();
		rm2.setName("sdfs");
		assertFalse(rm.equals(rm2));
	}
	
	@Test
	public void testEqualValid() {
		RestaurantModel rm = new RestaurantModel();
		rm.setName("sdfs");
		rm.hashCode();
		RestaurantModel rm2 = new RestaurantModel();
		rm2.setName("sdfs");
		assertTrue(rm.equals(rm2));
	}
	

	@Test
	public void testEqualWrongName() {
		RestaurantModel rm = new RestaurantModel();
		rm.setName("sdfs");
		rm.hashCode();
		RestaurantModel rm2 = new RestaurantModel();
		rm2.setName("sdfsdfs");
		assertFalse(rm.equals(rm2));
	}
	
	
	
	
	
	
	

}
