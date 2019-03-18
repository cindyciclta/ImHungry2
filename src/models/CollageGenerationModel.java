package models;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.List;

import org.json.JSONArray;


/**
 * Stores links for a given collage
 * does not generate the collage
 */
public class CollageGenerationModel {
	
	
	private String term;
	private final int NUMBER_OF_PICTURES = 10;
	private String[] imagelist;
	private int actualsize = 0; // actual size of collage list
	private ArrayList<String> list; // list of links of collage
	public CollageGenerationModel() {
		list = new ArrayList<String>();
	}
	public int getActualSize() {

		return actualsize;
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
		return true;
	}

	public void setListofImages (ArrayList<String> iList, int size) {
		System.out.println("size is " + iList.size());
		this.list = iList;
		this.actualsize = size;
	}
	
	public ArrayList<String> getList() {
		return this.list;
	}

}
