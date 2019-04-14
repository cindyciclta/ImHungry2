package models;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.annotations.Expose;


public class RestaurantModel extends ListItemModel implements Comparable<RestaurantModel>{
	
	@Expose
	private int item_id;
	
	@Expose
	private String name;
	
	@Expose
	private int stars;
	
	@Expose
	private double lowEndPrice;
	
	@Expose
	private double highEndPrice;
	
	@Expose
	private double lat;
	
	@Expose
	private double lon;
	
	@Expose
	private int drivingTime;
	
	@Expose
	private String phoneNumber;
	
	@Expose
	private String linkToPage;
	
	@Expose
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
			modifier = ListTypeEnum.DONOTSHOW.type;
		} else if(isInFavorites()) {
			modifier = ListTypeEnum.FAVORITES.type;
		} else if(isInToExplore()) {
			modifier = ListTypeEnum.TOEXPLORE.type;
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
	
	@Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!RestaurantModel.class.isAssignableFrom(obj.getClass())) {
            return false;
        }

        final RestaurantModel other = (RestaurantModel) obj;
        if(this.name == null || other.name == null) {
        	return false;
        }
        if(!this.name.equalsIgnoreCase(other.name)) {
        	return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = (int)(53 * this.drivingTime + 10000 * this.lat + 13412312 * this.lon);
        return hash;
    }
}