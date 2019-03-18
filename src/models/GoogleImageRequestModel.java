package models;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * GETs a set of 10 google image links
 * using HttpRequest
 */
public class GoogleImageRequestModel {
	private CollageGenerationModel results;
	private String term;
	private int limit;
	ArrayList <String> allimageurl;
	public GoogleImageRequestModel(CollageGenerationModel collageModel) {
		this.results = collageModel;
	}
	
	/**
	 * performs url hit against Google Images
	 * @param imagesearch input search term, assumed correct
	 * @return String json response
	 * @throws Exception
	 */
	public String getResponse(String imagesearch) throws Exception{
		
		imagesearch.trim();
		String trimmed = imagesearch.replaceAll("\\_", "+");
		//The URL to Google Image API - API key and Custom Control key included, Add the imageSearch at the end.
		URL url = new URL("https://www.googleapis.com/customsearch/v1?key=AIzaSyAqAClJ63YQN5UtnDUNED9c2qZf6op5yow" + 
				"&cx=018271258977134758236:ifnf_dla9yk&q=" + trimmed + "&searchType=image");
		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
	    con.setRequestProperty("User-Agent", "Chrome");
		con.addRequestProperty("Referer", "localhost:8080");
		
		
		//Parse JSON file and Serialize the Objects
		String line;
		BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String response = "";
		while((line = reader.readLine()) != null) {
			response += line;
			//System.out.println(line);
		}
		return response;
	}
	
	public ResponseCodeModel APIImageSearch(String imagesearch) {	
		try {
			String response = getResponse(imagesearch);

		    if (!response.isEmpty()) {
		    	//Parses the JSON object to get the image and store the url link to an ArrayList
		    	JSONObject json = new JSONObject(response);
		    	JSONArray items = json.getJSONArray("items");
		    	ArrayList <String> allimageurl = new ArrayList<String>();
				int actualsize = 0;
		    	for (int i = 0; i < 10 ; i ++) {
		    		JSONObject item_i = items.getJSONObject(i);
			    	JSONObject imageobj = item_i.getJSONObject("image");
			    	String string_urlimage = imageobj.getString("thumbnailLink");
			    	if (string_urlimage != null || string_urlimage != "") {
				    	allimageurl.add(string_urlimage);

				    	actualsize++;
		    		}
		    	}
		    	this.results.setListofImages(allimageurl, actualsize);
		    }
		} catch (Exception e) {
			return ResponseCodeModel.INTERNAL_ERROR;
	    }  
		return ResponseCodeModel.OK;
	}

}
