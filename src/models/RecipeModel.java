package models;

import java.util.*;

/**
 * Stores recipe information
 */
public class RecipeModel extends ListItemModel implements Comparable<RecipeModel>{
	
	private String name; // Deviation from plan
	private String imageURL;
	private List<IngredientModel> ingredients;
	private java.util.List<String> instructions;
	private int cookTime;
	private int prepTime;
	private int stars; // Deviation
	
	public RecipeModel(){
		ingredients = new ArrayList<>();
		instructions = new ArrayList<>();
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
	
	public Map<String, String> getFormattedFieldsForResults(){
		Map<String, String> toReturn = new HashMap<>();
		toReturn.put("name", name);
		String stars = "";
		for(int i = 0 ; i < this.stars ; i++) {
			stars += "&#9733";
		}
		toReturn.put("stars", stars);
		toReturn.put("prepTime", prepTime + "");
		toReturn.put("cookTime", cookTime + "");
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
		toReturn.put("prepTime", prepTime + " minutes");
		toReturn.put("cookTime", cookTime + " minutes");
		toReturn.put("imageUrl", imageURL);
		
		String ing = "";
		for(int i = 0 ; i < ingredients.size() ; i++) {
			ing += ingredients.get(i).formatIngredients() + "SPLIT";
		}
		toReturn.put("ingredients", ing);
		
		String ins = "";
		for(int i = 0 ; i < instructions.size() ; i++) {
			ins += instructions.get(i) + "SPLIT";
		}
		toReturn.put("instructions", ins);
		return toReturn;
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
	
	public boolean setPrepTime(int prep) {
		if(prep < 0) {
			return false;
		}
		this.prepTime = prep;
		return true;
	}
	
	public boolean setCookTime(int prep) {
		if(prep < 0) {
			return false;
		}
		this.cookTime = prep;
		return true;
	}
	
	public boolean addInstruction(String instruction) {
		if(instruction == null) {
			return false;
		}
		instruction = instruction.trim();
		if(instruction.isEmpty()) {
			return false;
		}
		instructions.add(instruction);
		return true;
	}
	
	public boolean addIngredient(IngredientModel i) {
		if(i == null) {
			return false;
		}
		ingredients.add(i);
		return true;
	}
	
	public boolean setImageUrl(String link) {
		if(link == null) {
			return false;
		}
		link = link.trim();
		if(link.isEmpty()) {
			return false;
		}
		this.imageURL = link;
		return true;
	}

	@Override
	public int compareTo(RecipeModel arg0) {
		
		// Puts favorites first
		if(isInFavorites() && !arg0.isInFavorites()) {
			return -1;
		}
		if(!isInFavorites() && arg0.isInFavorites()) {
			return 1;
		}
		
		
		if(prepTime < arg0.prepTime) {
			return -1;
		} else if(prepTime == arg0.prepTime) {
			return 0;
		}
		return 1;
	}
}