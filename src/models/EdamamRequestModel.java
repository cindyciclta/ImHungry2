package models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Scrapes AllRecipes for information on the recipes
 */
public class EdamamRequestModel implements ApiCallInterface<RecipeModel> {
	
	public final String URL_LINK = "https://www.allrecipes.com/search/results/?wt=";
	
	protected List<RecipeModel> results;
	
	protected String term;
	
	protected int limit;
	
	public EdamamRequestModel() {
		results = new ArrayList<>();
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
	
	public List<RecipeModel> getResults(){
		return results;
	}
	
	public String getHTMLLinks() throws IOException{
		// Parse the links
		StringBuilder contentBuilder = new StringBuilder();
		term.trim();
		System.out.println("term in recipe "+ term);
		String trimmed = term.replaceAll("\\_", "+");
		URL url = new URL(URL_LINK + trimmed + "&sort=re");
	    BufferedReader in = new BufferedReader(
	            new InputStreamReader(url.openStream()));
	    String str;
	    while ((str = in.readLine()) != null) {
	        contentBuilder.append(str);
	    }
	    in.close();
		String content = contentBuilder.toString();
		return content;
	}
	
	public String getParseRecipe(String link) throws IOException{
		
		StringBuilder contentBuilder = new StringBuilder();

		URL url = new URL(link);
	    BufferedReader in = new BufferedReader(
	            new InputStreamReader(url.openStream()));
	    String str = "";
	    while ((str = in.readLine()) != null) {
	        contentBuilder.append(str);
	    }
	    in.close();
		String content = contentBuilder.toString();
		return content;
	}
	
	@Override
	public ResponseCodeModel completeTask() {
 		ResponseCodeModel responseResult = ResponseCodeModel.OK;
		try {
			
			String content = getHTMLLinks();
			
			Set<String> links = new HashSet<>();
			int index = content.indexOf("://www.allrecipes.com/recipe/");
			while(index != -1) {
				content = content.substring(index);
				int end_index = content.indexOf("\"");
				if(end_index != -1) {
					links.add("https" + content.substring(0, end_index));
					content = content.substring(end_index);
				}
				index = content.indexOf("://www.allrecipes.com/recipe/");
			}
			List<String> linkReal = new ArrayList<>(links);
			// Parse DA RECIPES from each of their links
			for(int i = 0 ; i < Math.min(links.size(), limit) ; i++) {
				Thread.sleep(500);
				
				String link = linkReal.get(i);
				content = getParseRecipe(link);
				RecipeModel added = new RecipeModel();
				
				// Get name
				String tag = " <meta property=\"og:title\" content=";
				index = content.indexOf(tag);
				if(index != -1) {
					content = content.substring(index);
					int end_index = content.indexOf("/>");
					String title = content.substring(tag.length(), end_index);
					title = title.replace("Recipe", "");
					title = title.replace("\"", "");
					content = content.substring(end_index);
					added.setName(title);
				} else {
					added.setName("unknown");
				}
				
				// Get link
				tag = "<section class=\"hero-photo hero-photo--downsized \">";
				index = content.indexOf(tag);
				if(index != -1) {
					content = content.substring(index);
					tag = "src=\"";
					index = content.indexOf(tag);
					if(index != -1) {
						content = content.substring(index + tag.length());
						int end_index = content.indexOf("\"");
						String linkURL = content.substring(0, end_index);
						content = content.substring(end_index);
						added.setImageUrl(linkURL);
					}
				} else {
					added.setImageUrl("unknown");
				}
				
				// Get rating
				tag = " <meta property=\"og:rating\" content=";
				index = content.indexOf(tag);
				if(index != -1) {
					content = content.substring(index);
					int end_index = content.indexOf("/>");
					String title = content.substring(tag.length(), end_index);
					title = title.replace("Recipe", "");
					title = title.trim();
					if(title.length() > 5) {
						title = title.substring(1, 6);
					}
					double rating = 3;
					try {
						
						rating = Double.parseDouble(title);
					} catch(Exception e) {}
					added.setStars((int)Math.round(rating));
					content = content.substring(end_index);
				} else {
					added.setStars(3);
				}
				
				// Ingredients
				tag = "<ul class=\"checklist dropdownwrapper list-ingredients-1\" ng-hide=\"reloaded\" id=\"lst_ingredients_1\">";
				index = content.indexOf(tag);
				if(index != -1) {
					content = content.substring(index);
					tag = "itemprop=\"recipeIngredient\">";
					index = content.indexOf(tag);
					while(index != -1) {
						content = content.substring(index + tag.length());
						
						int end_index = content.indexOf("</span>");
						if(end_index != -1) {
							String ingredient = content.substring(0, end_index);
							content = content.substring(end_index);
							IngredientModel ing = new IngredientModel();
							ing.validateIngredients(ingredient, 0, "");
							added.addIngredient(ing);
						}
						index = content.indexOf(tag);
					}	
				}
				
				// Prep and cook time
				tag = "<span class=\"prepTime__item--time\">";
				index = content.indexOf(tag);
				if(index != -1) {
					content = content.substring(index);
					int end_index = content.indexOf("</span>");
					String title = content.substring(tag.length(), end_index);
					int time = 30;
					try {
						time = Integer.parseInt(title);
					} catch(Exception e) {}
					added.setPrepTime(time);
					content = content.substring(end_index);
				} else {
					added.setPrepTime(30);
				}
				index = content.indexOf(tag);
				if(index != -1) {
					content = content.substring(index);
					int end_index = content.indexOf("</span>");
					String title = content.substring(tag.length(), end_index);
					int time = 30;
					try {
						time = Integer.parseInt(title);
					} catch(Exception e) {}
					added.setCookTime(time);
					content = content.substring(end_index);
				} else {
					added.setCookTime(30);
				}
				
				// Instructions
				tag = "<ol class=\"list-numbers recipe-directions__list\" itemprop=\"recipeInstructions\">";
				index = content.indexOf(tag);
				if(index != -1) {
					content = content.substring(index);
					tag = "<span class=\"recipe-directions__list--item\">";
					index = content.indexOf(tag);
					while(index != -1) {
						content = content.substring(index + tag.length());
						int end_index = content.indexOf("</span>");
						if(end_index != -1) {
							String instruction = content.substring(0, end_index);
							content = content.substring(end_index);
							added.addInstruction(instruction.trim());
						}
						index = content.indexOf(tag);	
					}	
				}
				results.add(added);
			}
		}catch(IOException | InterruptedException e) {
			responseResult = ResponseCodeModel.INTERNAL_ERROR;
		}
		Collections.sort(results);
		
		return responseResult;	
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
		return results.get(i).getFormattedFieldsForResults();
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