package models;

import java.util.ArrayList;

public class SearchTermModel implements Comparable<SearchTermModel>{
	public String term;
	public int radius;
	public int limit;
	public int search_id;
	public long time;
	public ArrayList<String> images;
	
	@Override
	public int compareTo(SearchTermModel other) {
		if(time < other.time) {
			return -1;
		}else if(other.time < time) {
			return 1;
		}
		return 0;
	}
}
