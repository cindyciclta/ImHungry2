package models;

import java.util.Scanner;

import org.json.JSONObject;
import java.net.URL;
import java.io.IOException;
import java.net.*;

/**
 * Get calls a distance from google distance API
 *
 */
public class GoogleMapsRequestModel {
	
	
	// Deviation
	private final static String URL = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=34.0218973,-118.2895904";
	
	private final String API_KEY = "AIzaSyCjNWygHe9tBxPhVtCR7SrCOn6_hUw5H2Y";
	
	// for storing lat and lon of destination restaurant
	private double lat;
	private double lon;
	
	public GoogleMapsRequestModel() {
		
	}
	
	public boolean checkParameters(double lattitude, double longitude) {
		this.lat = lattitude;
		this.lon = longitude;
		return true;
	}
	
	public int getDrivingTime() {
		int drivingTime = -1;
		try {
			URL wikiRequest = new URL(URL + "&key=" + API_KEY + "&destinations=" + lat + "," + lon);
			Scanner scanner = new Scanner(wikiRequest.openStream());
			String response = scanner.useDelimiter("\\Z").next();
			JSONObject json = new JSONObject(response);
			
			if(json.getJSONArray("rows").getJSONObject(0).getJSONArray("elements").getJSONObject(0).getString("status").equals("OK")) {
				int seconds = json.getJSONArray("rows").getJSONObject(0).getJSONArray("elements").getJSONObject(0).getJSONObject("duration").getInt("value");
				drivingTime = seconds / 60;
			}
			
			
			scanner.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
		return drivingTime;
	}
	

}
