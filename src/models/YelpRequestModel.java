package models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONArray;
import org.json.JSONObject;

// Moved to abstract due to lack of support for adding list items from db
public abstract class YelpRequestModel implements ApiCallInterface<RestaurantModel>{

	private static final String API_KEY = "XV6c8H4T5PriBv2QO0smCcFhMU3d3axPXDY6yEWAekPe9ErQZI70EFyipPyig8g1J-1RozjFY14vs14_ZC3o9_3pAlhqDw74zA7iTg-u9OkWNlcQ7n2HmKPOKht6XHYx";
	protected String term;
	protected int limit;
	protected int radius = 5;
	private int radius_meter;
	protected List<RestaurantModel> results;
	public int responseCode;
	protected List<RestaurantModel> listItems = new ArrayList<>();
	
	public YelpRequestModel() {
		results = new ArrayList<>();
	}
	
	public List<RestaurantModel> getResults(){
		return results;
	}
	
	public boolean checkParameters(String term, int limit, double radius) {
		if(limit < 0) {
			radius_meter = convertMilesToMeters(radius);
			return false;
		}
		if(term == null) {
			return false;
		}
		term = term.trim();
		if(term.isEmpty()) {
			return false;
		}
		this.term = term;
		this.limit = limit;
		this.radius = (int)radius;
		return true;
	}
	private int convertMilesToMeters(double miles) {
		if (miles < 0) {
			return 1000;
		}
		int meters = (int) (miles * 1609.344);
		return meters;
	}
	
	@Override
	public ResponseCodeModel completeTask() {
		
		try {
			if(this.radius == 0) {
				throw new IOException();
			}
			
			String url = "https://api.yelp.com/v3/businesses/search?term="+term+"&latitude=34.02056373251961&longitude=-118.28544706106186&radius="+ radius_meter+"&offset="+limit + "&limit="+limit;
			URL obj = new URL(url);
			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("grant_type", "client_credentials");
			con.setRequestProperty ("Authorization", "Bearer " + API_KEY);
			con.setDoOutput(true);
			responseCode = con.getResponseCode();
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line;
			String response = "";
			while((line = br.readLine()) != null) {
				response += line;
			}
			
			JSONObject json = new JSONObject(response);
			
			// Parse out all restaurant fields
			JSONArray businesses = json.getJSONArray("businesses");
			System.out.println("__________________"+ businesses.length() + " ___ "+ limit);
			for(int i = 0 ; i < Math.min(limit, businesses.length()) ; i++) {
				
				RestaurantModel restaurant = new RestaurantModel();
				
				JSONObject business = businesses.getJSONObject(i);
				String name = business.getString("name");
				double rating = business.getDouble("rating");
				
				restaurant.setName(name);
				restaurant.setStars((int)rating);
				
				String dollarSigns;
				try {
					dollarSigns = business.getString("price").trim();
				} catch (Exception e) {
					dollarSigns = "";
				}
				double lowEndPrice = dollarSigns.length() * 10;
				double highEndPrice = dollarSigns.length() * 30;
				restaurant.setPriceRange(lowEndPrice, highEndPrice);
				
				JSONObject coordinates = business.getJSONObject("coordinates");
				double lat = coordinates.getDouble("latitude");
				double lon = coordinates.getDouble("longitude");
				restaurant.setLatLong(lat, lon);
				
				String phone = business.getString("phone");
				restaurant.setPhone(phone);
				
				String link = business.getString("url");
				restaurant.setLinkToPage(link);
				
				JSONObject location = business.getJSONObject("location");
				JSONArray display = location.getJSONArray("display_address");
				
				String address = "";
				for(int j = 0 ; j < display.length() ; j++) {
					address += display.getString(j) + " ";
				}
				address = address.trim();
				restaurant.setAddress(address);
				
				results.add(restaurant);
			}
			Collections.sort(results);
		     
		} catch (IOException e) {
	        return ResponseCodeModel.INTERNAL_ERROR;
	    }  
		return ResponseCodeModel.OK;
	}

	@Override
	public int getResultsSize() {
		return results.size();
	}

	@Override
	public Map<String, String> getFormattedResultsFieldsAt(int i) {
		if(i < 0) {
			return null;
		}
		if(i >= results.size()) {
			return null;
		}
		
		return results.get(i).getFormattedFieldsForResultsPage();
	}

	@Override
	public Map<String, String> getFormattedDetailsFieldsAt(int i) {
		if(i < 0) {
			return null;
		}
		if(i >= results.size()) {
			return null;
		}
		return results.get(i).getFormattedFieldsForDetailsPage();
	}

	@Override
	public boolean setFavoriteResult(int i, boolean value) {
		if(i < 0) {
			return false;
		}
		if(i >= results.size()) {
			return false;
		}
		boolean removed = !value && results.get(i).isInFavorites();
		results.get(i).setInFavorites(value);
		addOrRemoveValue(results.get(i), value, removed);
		return true;
	}

	@Override
	public boolean setToExploreResult(int i, boolean value) {
		if(i < 0) {
			return false;
		}
		if(i >= results.size()) {
			return false;
		}
		boolean removed = !value && results.get(i).isInToExplore();
		results.get(i).setInToExplore(value);
		addOrRemoveValue(results.get(i), value, removed);
		return true;
	}

	@Override
	public boolean setDoNotShowResult(int i, boolean value) {
		if(i < 0) {
			return false;
		}
		if(i >= results.size()) {
			return false;
		}
		boolean removed = !value && results.get(i).isInDoNotShow();
		results.get(i).setInDoNotShow(value);
		
		addOrRemoveValue(results.get(i), value, removed);
		return true;
	}
	
	private void addOrRemoveValue(RestaurantModel restauarant, boolean value, boolean removed) {
		if(!listItems.contains(restauarant) && value) {
			listItems.add(restauarant);
		}else if(listItems.contains(restauarant) && !removed) {
			listItems.remove(restauarant);
		}
	}
	
	@Override
	public void sort() {
		Collections.sort(results);
	}

	@Override
	public int getListSize() {
		return listItems.size();
	}

	@Override
	public Map<String, String> getFormattedResultsFieldsListAt(int i) {
		if(i < 0) {
			return null;
		}
		if(i >= listItems.size()) {
			return null;
		}
		
		return listItems.get(i).getFormattedFieldsForResultsPage();
	}
}
