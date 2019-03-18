package models;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class YelpRequestModel implements ApiCallInterface<RestaurantModel>{

	
	private static final String API_KEY = "XV6c8H4T5PriBv2QO0smCcFhMU3d3axPXDY6yEWAekPe9ErQZI70EFyipPyig8g1J-1RozjFY14vs14_ZC3o9_3pAlhqDw74zA7iTg-u9OkWNlcQ7n2HmKPOKht6XHYx";
	private String term;
	private int limit;
	private List<RestaurantModel> results;
	public int responseCode;
	public YelpRequestModel() {
		results = new ArrayList<>();
	}
	
	public List<RestaurantModel> getResults(){
		return results;
	}
	
	
	public boolean checkParameters(String term, int limit) {
		if(limit < 0) {
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
		return true;
	}
	
	@Override
	public ResponseCodeModel completeTask() {
		try {
			String url = "https://api.yelp.com/v3/businesses/search?term="+term+"&latitude=34.021217&longitude=-118.287093";
			URL obj = new URL(url);
			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("grant_type", "client_credentials");
			con.setRequestProperty ("Authorization", "Bearer " + API_KEY);
			con.setDoOutput(true);
			responseCode = con.getResponseCode();
			if(responseCode  < 200 || responseCode > 299) {
				return ResponseCodeModel.AUTH_UNAUTHORIZED;
			}
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line;
			String response = "";
			while((line = br.readLine()) != null) {
				response += line;
				//System.out.println(line);
			}
			if(!response.isEmpty())
			{
				JSONObject json = new JSONObject(response);
				
				// Parse out all restaurant fields
				JSONArray businesses = json.getJSONArray("businesses");
				for(int i = 0 ; i < Math.min(limit, businesses.length()) ; i++) {
					
					RestaurantModel restaurant = new RestaurantModel();
					
					JSONObject business = businesses.getJSONObject(i);
					String name = business.getString("name");
					double rating = business.getDouble("rating");
					
					restaurant.setName(name);
					restaurant.setStars((int)rating);
					
					try {
						String dollarSigns = business.getString("price").trim();
						double lowEndPrice = dollarSigns.length() * 10;
						double highEndPrice = dollarSigns.length() * 30;
						restaurant.setPriceRange(lowEndPrice, highEndPrice);	
					}catch(JSONException e){
						double lowEndPrice = 1;
						double highEndPrice = 100;
						restaurant.setPriceRange(lowEndPrice, highEndPrice);
					}
					
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
			}
			
			Collections.sort(results);
		     
		} catch (Exception e) {
			e.printStackTrace();
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
		
		results.get(i).setInFavorites(value);
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
		
		results.get(i).setInToExplore(value);
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
		
		results.get(i).setInDoNotShow(value);
		return true;
	}
	
	@Override
	public void sort() {
		Collections.sort(results);
		
	}
}
