package models;

import com.google.gson.annotations.Expose;

public class IngredientModel {
	
	@Expose
	private String name;
	
	@Expose
	private String amount;
	
	public IngredientModel(){
		
	}
	
	/**
	 * Format ingredients, ensure all amounts are valid. False if invalid
	 * @param name
	 * @param valueAmount
	 * @param units
	 * @return
	 */
	public boolean validateIngredients(String name, double valueAmount, String units) {
		this.name = name.trim();
		this.amount = valueAmount + " " + units.trim();
		return true;
	}
	
	// Format ingredient into string for display on the screen
	public String formatIngredients() {
		return (this.name).trim();
	}
	public String getIngredientName(){
		return name;
	}
}
