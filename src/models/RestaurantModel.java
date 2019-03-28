package models;

import java.util.HashMap;
import java.util.Map;

public class RestaurantModel extends ListItemModel implements Comparable<RestaurantModel>{
	
	private String name;
	private int stars;
	private double lowEndPrice;
	private double highEndPrice;
	private double lat;
	private double lon;
	private int drivingTime;
	private String phoneNumber;
	private String linkToPage;
	private String address;
	
	public RestaurantModel(){
		
	}
	
	public boolean setName(String name) {
		
		if(name == null) {
			return false;
		}
		name = name.trim();
		if(name.isEmpty()) {
			return false;
		}
		this.name = name;
		return true;
	}
	
	public boolean setAddress(String address) {
		this.address = address;
		return true;
	}
	
	public boolean setStars(int stars) {
		if(stars < 0) {
			return false;
		}
		if(stars > 5) {
			return false;
		}
		this.stars = stars;
		return true;
	}
	
	public Map<String, String> getFormattedFieldsForResultsPage(){
		Map<String, String> toReturn = new HashMap<>();
		
		toReturn.put("name", name);
		toReturn.put("address", address);
		
		String stars = "";
		for(int i = 0 ; i < this.stars ; i++) {
			stars += "&#9733";
		}
		toReturn.put("stars", stars);
		toReturn.put("drivingTime", this.drivingTime + " minutes of driving");
		toReturn.put("priceRange", String.format( "%.2f", lowEndPrice ) + " to " + String.format( "%.2f", highEndPrice ));
		
		String modifier = "";
		if(isInDoNotShow()) {
			modifier = "donotshow";
		} else if(isInFavorites()) {
			modifier = "favorites";
		} else if(isInToExplore()) {
			modifier = "toexplore";
		}
		toReturn.put("modifier", modifier);
		
		return toReturn;
	}
	
	public Map<String, String> getFormattedFieldsForDetailsPage(){
		Map<String, String> toReturn = new HashMap<>();
		
		toReturn.put("name", name);
		toReturn.put("address", address);
		toReturn.put("mapsLink", "https://www.google.com/maps/search/?api=1&query=" + lat + "," + lon);
		toReturn.put("websiteLink", linkToPage);
		toReturn.put("phoneNumber", phoneNumber);
		return toReturn;
	}
	
	public boolean setPhone(String phone) {
		
		if(phone == null) {
			return false;
		}
		phone = phone.trim();
		if(phone.isEmpty()) {
			return false;
		}
		this.phoneNumber = phone;
		return true;
	}
	
	public boolean setLinkToPage(String link) {
		this.linkToPage = link;
		return true;
	}
	
	public boolean setDrivingTime(int drivingTime) {
		if(drivingTime < 0) {
			return false;
		}
		this.drivingTime = drivingTime;
		return true;
	}
	
	public boolean setPriceRange(double lowEnd, double highEnd) {
		if(lowEnd < 0) {
			return false;
		}
		if(highEnd < lowEnd) {
			return false;
		}
		this.lowEndPrice = lowEnd;
		this.highEndPrice = highEnd;
		return true;
	}
	
	public boolean setLatLong(double lat, double lon) {
		this.lat = lat;
		this.lon = lon;
		
		GoogleMapsRequestModel gm = new GoogleMapsRequestModel();
		gm.checkParameters(lat, lon);
		this.drivingTime = gm.getDrivingTime();
		return true;
	}

	@Override
	public int compareTo(RestaurantModel arg0) {
		// Puts favorites first
		if(isInFavorites() && !arg0.isInFavorites()) {
			return -1;
		}
		if(!isInFavorites() && arg0.isInFavorites()) {
			return 1;
		}
		
		return this.drivingTime - arg0.drivingTime;
	}
}