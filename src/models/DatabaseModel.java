package models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class DatabaseModel {
	static final String SQL_PASSWORD = "root"; // SET YOUR MYSQL PASSWORD HERE TO GET DATABASE WORKING!!!!!!!
	
	private static Connection conn;
	
	public static SignInResponseEnum signInUser(String username, char[] password) throws Exception {
		
		// Get from SQL
		Connection conn = null;
		ResultSet rs = null;
		int returnVal = -1;
		SignInResponseEnum signIn = SignInResponseEnum.DOES_NOT_EXIST;
		
		conn = getConnection();
		
		// the mysql insert statement to have date of upload
		String query = "SELECT (user_id) from users where user_name = (?) and user_password = SHA1(?)";
		PreparedStatement preparedStmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		preparedStmt.setString (1, username);
		preparedStmt.setString(2, String.valueOf(password));
		rs = preparedStmt.executeQuery();
		if(rs.next()) {
			returnVal = rs.getInt("user_id");
			signIn = SignInResponseEnum.VALID;
			signIn.setId(returnVal);
		}else {
			query = "SELECT (user_id) from users where user_name = (?)";
			preparedStmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			preparedStmt.setString (1, username);
			ResultSet rs2 = preparedStmt.executeQuery();
			if(rs2.next()) {
				signIn = SignInResponseEnum.INVALID_CREDENTIALS;
			}
		}
		return signIn;
	}
	
	public static Connection getConnection() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		//replace 68.183.168.73 with localhost to use local DB
		if(conn == null) {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ImHungry?user=root&password=" + SQL_PASSWORD + "&useSSL=false&allowPublicKeyRetrieval=true");
		}
		return conn;
	}
	
	public static void closeConnection() throws SQLException {
		conn.close();
		conn = null;
	}
	
	public static boolean userExists(String username) throws Exception {
		// Get from SQL
		Connection conn = null;
		ResultSet rs = null;
		boolean returnVal = false;
		
		conn = getConnection();
		
		// the mysql insert statement to have date of upload
		String query = "SELECT * from users where user_name = (?)";
		PreparedStatement preparedStmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
	    	preparedStmt.setString (1, username);
	   	rs = preparedStmt.executeQuery();
	    	if(rs.next()) {
	    		returnVal = true;
	    	}
		return returnVal;
	}
	public static boolean insertUser(String username, char[] password) throws Exception {
		// Get from SQL
		Connection conn = null;
		boolean returnVal = true;
		
		conn = getConnection();
		
		if(DatabaseModel.userExists(username)) {
			returnVal = false;
		}
		else {
			username = username.trim();
			
			// the mysql insert statement to have date of upload
			String sql = "INSERT INTO users (user_name, user_password) VALUES (?, SHA1(?))";
			PreparedStatement preparedStmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			preparedStmt = conn.prepareStatement(sql);
			preparedStmt.setString(1, username);
			preparedStmt.setString(2, String.valueOf(password)); 
			preparedStmt.executeUpdate();
				
		}
		return returnVal;
	}
	
	
	public static int AddSearchTermToHistory(int user_id, String term, int limit, int radius) throws Exception{
		int added_id = -1;

		if(user_id < 0) {
			return added_id;
		}
		
		term = term.trim();
		Connection conn =null;
		PreparedStatement preparedStmt = null;
		
		conn = getConnection();
		String sql = "INSERT INTO searches (user_id, term, limit_search, radius, insert_time) VALUES (?, ?, ?, ?, ?)";
		
		String generatedColumns[] = { "search_id" };
		preparedStmt = conn.prepareStatement(sql, generatedColumns);
		ResultSet rs;
		preparedStmt.setInt(1, user_id);
		preparedStmt.setString(2, term);
		preparedStmt.setInt(3, limit);
		preparedStmt.setInt(4, radius);
		preparedStmt.setLong(5, System.currentTimeMillis());
		preparedStmt.executeUpdate();
		rs = preparedStmt.getGeneratedKeys();
		
		rs.next();
		added_id = rs.getInt(1);
		
		return added_id;
		
	}
	
	// Gets in user-insensitive manner
	public static int getSearchId(String term, int limit, int radius) throws Exception{
		int id = -1;
		Connection conn = getConnection();
		PreparedStatement ps = conn.prepareStatement("SELECT * FROM searches WHERE term=(?) and limit_search=(?) and radius=(?)");

		ps.setString(1, term);
		ps.setInt(2, limit);
		ps.setInt(3, radius);
		
		ResultSet rs = null;

		rs = ps.executeQuery();
		
		if(rs.next()) {
			id = rs.getInt("search_id");
		}
		return id;
	}
	
	// Gets in user-insensitive manner
	public static int getSearchId(String term, int limit) throws Exception{
		int id = -1;
		Connection conn = getConnection();
		PreparedStatement ps = conn.prepareStatement("SELECT * FROM searches WHERE term=(?) and limit_search=(?)");

		ps.setString(1, term);
		ps.setInt(2, limit);
		
		ResultSet rs = null;

		rs = ps.executeQuery();
		
		if(rs.next()) {
			id = rs.getInt("search_id");
		}
		 
		return id;
	}
	
	
	
	// Gets in user-sensitive manner
	public static int getSearchIdUser(int userId, String term, int limit, int radius) throws Exception{
		int id = -1;
		Connection conn = getConnection();
		PreparedStatement ps = conn.prepareStatement("SELECT * FROM searches WHERE term=(?) and limit_search=(?) and radius=(?)");

		ps.setString(1, term);
		ps.setInt(2, limit);
		ps.setInt(3, radius);
		
		ResultSet rs = null;

		rs = ps.executeQuery();
		
		while(rs.next()) {
			if(rs.getInt("user_id") == userId) {
				id = rs.getInt("search_id");
			}
		}
		 
		return id;
	}
	
	
	public static boolean AddSearchToHistory(int added_id, String term, int limit, int radius, ArrayList<String> urllist)
			throws Exception {
		
		String imgsql = "INSERT IGNORE INTO images (term, url) VALUES (?, ?)";
		PreparedStatement ps = null;
		Connection conn = getConnection();
		for (int i = 0; i < urllist.size(); i++) {
			ps = conn.prepareStatement(imgsql);
			ps.setString(1, term);
			ps.setString(2, urllist.get(i));
			ps.executeUpdate();
		}
		if (ps != null ) {ps.close();}
		 
		
		return true;
	}
	public static int GetUserID(String username) throws Exception {
		
		Connection conn =null;
		PreparedStatement preparedStmt = null;
		ResultSet rs = null;

		conn = getConnection();
		String query = "SELECT * from users where user_name = (?)";
		preparedStmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
    	preparedStmt.setString (1, username);
    	rs = preparedStmt.executeQuery();
    	if(rs.next()) {
    		return rs.getInt("user_id");
    	}
		return -1;
	}
	public static Vector<SearchTermModel> GetSearchHistory(int userid) throws Exception {
		Connection conn = getConnection();
		Vector<SearchTermModel> results = new Vector<>();
		Statement st = conn.createStatement();

		ResultSet rs = null;

		rs = st.executeQuery("SELECT * FROM searches WHERE user_id = " + Integer.toString(userid));
		while (rs.next()) {
			SearchTermModel s = new SearchTermModel();
			s.term = rs.getString("term");
			s.limit = rs.getInt("limit_search");
			s.radius = rs.getInt("radius");
			s.search_id = rs.getInt("search_id");
			s.time = rs.getLong("insert_time");
			
			s.images = new ArrayList<String>();
			PreparedStatement ps = conn.prepareStatement("SELECT url FROM images WHERE term = ?");
			ps.setString(1, s.term);
			ResultSet imgrs = ps.executeQuery();
			while (imgrs.next() && s.images.size() < 11) {
				s.images.add(imgrs.getString("url"));
			}
			imgrs.close();
			results.add(s);
		}
		removeAdjacentDuplicates(results);
		Collections.reverse(results);
		 
		rs.close();
		st.close();
		return results;
	}
	public static void removeAdjacentDuplicates(Vector<SearchTermModel> results) {
		if (results == null || results.size() <= 1) {
			return;
		}
		Vector<SearchTermModel> dupl = new Vector<SearchTermModel>();
		SearchTermModel prev = results.get(0);
		for (int i = 1; i < results.size(); i++) {
			SearchTermModel curr = results.get(i);
			if (curr.term.equals(prev.term)) {
				dupl.add(curr);
			}
			prev = curr;
		}
		results.removeAll(dupl);
	}
	
	public static boolean insertRestaurant(RestaurantModel restaurant, int searchId) throws Exception{
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		String json = gson.toJson(restaurant);
		String sql = "INSERT INTO restaurants (search_id, json_string) VALUES (?, ?)";
		Connection conn = getConnection();
		PreparedStatement ps = conn.prepareStatement(sql);
		
		ps.setInt(1, searchId);
		ps.setString(2, json);
		
		ps.executeUpdate();
		
		 
		return true;
	}
	
	public static boolean insertRecipe(RecipeModel recipe, int searchId) throws Exception{
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		String json = gson.toJson(recipe);
		String sql = "INSERT INTO recipes (search_id, json_string) VALUES (?, ?)";
		Connection conn = getConnection();
		PreparedStatement ps = conn.prepareStatement(sql);
		
		ps.setInt(1, searchId);
		ps.setString(2, json);
		
		ps.executeUpdate();
		
		 
		return true;
	}
	
	public static List<RestaurantModel> getRestaurantsFromSearchTerm(String term, int limit, int radius) throws Exception{
		List<RestaurantModel> restaurants = new ArrayList<>();
		
		int id = getSearchId(term, limit, radius);
		Connection conn = getConnection();
		
		if(id != -1) {
			System.out.println("using cached restaurants");
			PreparedStatement ps = conn.prepareStatement("SELECT * from restaurants where search_id=(?)");
			ps.setInt(1, id);
			ResultSet rsRestaurant = ps.executeQuery();
			
			while(rsRestaurant.next()) {
				String json = rsRestaurant.getString("json_string");
				Gson gson = new Gson();
				RestaurantModel restaurant = gson.fromJson(json, RestaurantModel.class);
				
				restaurants.add(restaurant);
			}
		}
		return restaurants;
	}
	
	public static List<RecipeModel> getRecipesFromSearchTerm(String term, int limit) throws Exception{
		List<RecipeModel> recipes = new ArrayList<>();
		
		int id = getSearchId(term, limit);
		Connection conn = getConnection();
		
		if(id != -1) {
			PreparedStatement ps = conn.prepareStatement("SELECT * from recipes where search_id=(?)");
			ps.setInt(1, id);
			ResultSet rsRestaurant = ps.executeQuery();
			
			while(rsRestaurant.next()) {
				String json = rsRestaurant.getString("json_string");
				Gson gson = new Gson();
				RecipeModel recipe = gson.fromJson(json, RecipeModel.class);
				recipes.add(recipe);
			}
		}
		return recipes;
	}
	public static boolean InsertIntoGroceryList(int userid,  String groceryitem) throws Exception {

		String sql = "INSERT INTO grocery_list (selected_item, user_id, ordering) VALUES (?, ?, ?)";
		//check if the grocery item already exist
		GroceryListModel grocerylist = getGroceryListFromUser(userid);
		for (int i = 0; i < grocerylist.getSize(); i++) {
			if (grocerylist.getItem(i).equals(groceryitem)) {
				System.out.println("duplicated grocery list!");
				return false;
			}
		}
		Connection conn = getConnection();
		PreparedStatement ps = conn.prepareStatement(sql);
		
		ps.setString(1, groceryitem);
		ps.setInt(2, userid);
		ps.setInt(3, 1); //ordering fix later
		
		ps.executeUpdate();
		return true;
	}
	public static boolean InsertIntoGroceryList(String username, String groceryitem) throws Exception {
		int userid = GetUserID(username);
		String sql = "INSERT INTO grocery_list (selected_item, user_id, ordering) VALUES (?, ?, ?)";
		if (userid != -1) {
			return InsertIntoGroceryList(userid, groceryitem);
			 
		}
		return true;
	}

	
	public static boolean deleteFromGroceryList(int userId, String groceryItem)  throws Exception{
		
		String sql = "DELETE from grocery_list where selected_item = (?) and user_id = (?)";
		
		Connection conn = getConnection();
		PreparedStatement ps = conn.prepareStatement(sql);
		
		ps.setString(1, groceryItem);
		ps.setInt(2, userId);
		
		ps.executeUpdate();
		return true;
	}
	
	public static GroceryListModel getGroceryListFromUser(String username) throws Exception{		
		int userid = GetUserID(username);
		return getGroceryListFromUser(userid);
	}
	
	public static GroceryListModel getGroceryListFromUser(int userid) throws Exception{
		GroceryListModel gl = new GroceryListModel();
		
		Connection conn = getConnection();

		PreparedStatement ps = conn.prepareStatement("SELECT * from grocery_list where user_id=(?)");
		ps.setInt(1, userid);
		ResultSet rsGroceryList = ps.executeQuery();
		
		while(rsGroceryList.next()) {
			gl.additem(rsGroceryList.getString("selected_item"));
		}
		
		return gl;
	}
	
	
	/**
	 * All searches have a user. This gets the user id associated with a given search.
	 * @param searchId
	 * @return
	 */
	private static int getUserIdFromSearchId(int searchId) throws Exception{
		int userId = -1;
		Connection conn = getConnection();
		String query = "SELECT user_id from searches where search_id = (?)";
		PreparedStatement preparedStmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
    	preparedStmt.setInt (1, searchId);
    	ResultSet rs = preparedStmt.executeQuery();
		if(rs.next()) {
			userId = rs.getInt("user_id");
		}
	
		 
		return userId;
	}
	
	public static List<RestaurantModel> getRestaurantsInList(int searchId) throws Exception{
		int userId = DatabaseModel.getUserIdFromSearchId(searchId);
		List<RestaurantModel> restaurants = new ArrayList<>();
		Connection conn = getConnection();
		String query = "SELECT * from list_restaurants where user_id = (?)";
		PreparedStatement preparedStmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
    	preparedStmt.setInt (1, userId);
    	ResultSet rs = preparedStmt.executeQuery();
		while(rs.next()) {
			String restaurantQuery = "SELECT * from restaurants where item_id=(?)";
			PreparedStatement prep = conn.prepareStatement(restaurantQuery, Statement.RETURN_GENERATED_KEYS);
	    	int itemId = rs.getInt("item_id");
			prep.setInt (1, itemId);
	    	ResultSet restaurant = prep.executeQuery();
	    	restaurant.next();
	    	Gson g = new Gson();
	    	RestaurantModel rm = g.fromJson(restaurant.getString("json_string"), RestaurantModel.class);
	    	
	    	for(ListTypeEnum enumVal : ListTypeEnum.values()) {
	    		if(rs.getString("name").equals(enumVal.type)){
	    			rm.setInList(enumVal, true);
	    		}
	    	}
	    	
	    	// Get order in list
	    	restaurantQuery = "SELECT * from places where item_id=(?) and user_id=(?) and restaurant_or_recipe=(?)";
			prep = conn.prepareStatement(restaurantQuery, Statement.RETURN_GENERATED_KEYS);
			prep.setInt (1, itemId);
			prep.setInt(2, userId);
			prep.setString(3, "restaurant");
			restaurant = prep.executeQuery();
			restaurant.next();
			
			int place = restaurant.getInt("place");
			rm.setOrder(place);
		
	    	restaurants.add(rm);
		}
		return restaurants;
	}
	
	public static int getItemIdFromRestaurant(RestaurantModel rm) throws Exception{
		// Get Item ID
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		String json = gson.toJson(rm);
		Connection conn = getConnection();
		String query = "SELECT item_id from restaurants where json_string = (?)";
		PreparedStatement preparedStmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
    	preparedStmt.setString (1, json);
    	ResultSet rs = preparedStmt.executeQuery();
    	rs.next();
    	int item_id = rs.getInt("item_id");
    	 
    	return item_id;
	}
	
	public static boolean insertRestaurantIntoList(int searchId, RestaurantModel rm) throws Exception{
		boolean ret = true;
		
		// Get the user id
		int userId = getUserIdFromSearchId(searchId);
		int item_id = getItemIdFromRestaurant(rm);
		
    	// Get Number of Items in list
    	String name = "";
    	for(ListTypeEnum enumVal : ListTypeEnum.values()) {
    		if(rm.isInList(enumVal)){
    			name = enumVal.type;
    		}
    	}
    	// Stop adding something that is already in list
    	Connection conn = getConnection();
    	String query = "SELECT * from list_restaurants where item_id=(?) and user_id=(?)";
    	PreparedStatement preparedStmt = conn.prepareStatement(query);
    	preparedStmt.setInt(1, item_id);
    	preparedStmt.setInt(2, userId);
    	ResultSet exists =  preparedStmt.executeQuery();
    	if(exists.next()) {
    		query = "UPDATE list_restaurants SET name=(?) WHERE item_id=(?) and user_id=(?)";
    		preparedStmt = conn.prepareStatement(query);
    		preparedStmt.setString(1, name);
        	preparedStmt.setInt(2, item_id);
        	preparedStmt.setInt(3, userId);
        	preparedStmt.executeUpdate();
        	
        	// TODO: Update place on move
        	if(!name.equals(exists.getString("name"))) {
        		int place = updatePlaceOnMoveList(item_id, userId, name, exists.getString("name"), "restaurant");
        		rm.setOrder(place);
        	}
    	}else {
    		int count = countItemsInList(name, userId);
        	query = "INSERT INTO list_restaurants (item_id, user_id, name) values (?, ?, ?)";
        	preparedStmt = conn.prepareStatement(query);
        	preparedStmt.setInt(1, item_id);
        	preparedStmt.setInt(2, userId);
        	preparedStmt.setString(3, name);
        	preparedStmt.executeUpdate();
        	
        	// add the place
        	query = "INSERT INTO places (item_id, user_id, name, place, restaurant_or_recipe) values (?, ?, ?, ?, ?)";
        	preparedStmt = conn.prepareStatement(query);
        	preparedStmt.setInt(1, item_id);
        	preparedStmt.setInt(2, userId);
        	preparedStmt.setString(3, name);
        	preparedStmt.setInt(4, count+1);
        	preparedStmt.setString(5, "restaurant");
        	preparedStmt.executeUpdate();
        	rm.setOrder(count+1);
    	}
    	
    	 
		return ret;
	}
	
	private static int countItemsInList(String name, int userId) throws Exception{
		String query = "SELECT count(*) from places where user_id=(?) and name=(?)";
		Connection conn = getConnection();
		PreparedStatement preparedStmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
    	preparedStmt.setInt(1, userId);
    	preparedStmt.setString(2, name);
    	ResultSet rs = preparedStmt.executeQuery();
    	rs.next();
    	int rowCount = rs.getInt(1);
    	 
		return rowCount;
	}
	
	public static List<RecipeModel> getRecipesInList(int searchId) throws Exception{
		int userId = DatabaseModel.getUserIdFromSearchId(searchId);
		List<RecipeModel> recipes = new ArrayList<>();
		Connection conn = getConnection();
		String query = "SELECT * from list_recipes where user_id = (?)";
		PreparedStatement preparedStmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
    	preparedStmt.setInt (1, userId);
    	ResultSet rs = preparedStmt.executeQuery();
		while(rs.next()) {
			String restaurantQuery = "SELECT * from recipes where item_id=(?)";
			PreparedStatement prep = conn.prepareStatement(restaurantQuery, Statement.RETURN_GENERATED_KEYS);
			int itemId = rs.getInt("item_id");
	    	prep.setInt (1, itemId);
	    	ResultSet restaurant = prep.executeQuery();
	    	restaurant.next();
	    	Gson g = new Gson();
	    	RecipeModel rm = g.fromJson(restaurant.getString("json_string"), RecipeModel.class);
	    	for(ListTypeEnum enumVal : ListTypeEnum.values()) {
	    		if(rs.getString("name").equals(enumVal.type)){
	    			rm.setInList(enumVal, true);
	    		}
	    	}
	    	
	    	// Get order in list
	    	restaurantQuery = "SELECT * from places where item_id=(?) and user_id=(?) and restaurant_or_recipe=(?)";
			prep = conn.prepareStatement(restaurantQuery, Statement.RETURN_GENERATED_KEYS);
			prep.setInt (1, itemId);
			prep.setInt(2, userId);
			prep.setString(3, "recipe");
			restaurant = prep.executeQuery();
			restaurant.next();
			
			int place = restaurant.getInt("place");
			rm.setOrder(place);
			
			
	    	recipes.add(rm);
		}
		return recipes;
	}
	
	public static int getItemIdFromRecipe(RecipeModel rm) throws Exception{
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		String json = gson.toJson(rm);
		Connection conn = getConnection();
		String query = "SELECT item_id from recipes where json_string = (?)";
		PreparedStatement preparedStmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
    	preparedStmt.setString (1, json);
    	ResultSet rs = preparedStmt.executeQuery();
    	rs.next();
    	int item_id = rs.getInt("item_id");
    	 
    	return item_id;
	}
	
	public static boolean insertRecipeIntoList(int searchId, RecipeModel rm) throws Exception{
		boolean ret = true;
		
		// Get the user id
		int userId = getUserIdFromSearchId(searchId);
		
		// Get Item ID
		int item_id = getItemIdFromRecipe(rm);
		
    	
    	// Get Number of Items in list
    	String name = "";
    	for(ListTypeEnum enumVal : ListTypeEnum.values()) {
    		if(rm.isInList(enumVal)){
    			name = enumVal.type;
    		}
    	}
    	
    	// Stop adding something that is already in list
    	Connection conn = getConnection();
    	String query = "SELECT * from list_recipes where item_id=(?) and user_id=(?)";
    	PreparedStatement preparedStmt = conn.prepareStatement(query);
    	preparedStmt.setInt(1, item_id);
    	preparedStmt.setInt(2, userId);
    	ResultSet exists =  preparedStmt.executeQuery();
    	if(exists.next()) {
    		query = "UPDATE list_recipes SET name=(?) WHERE item_id=(?) and user_id=(?)";
    		preparedStmt = conn.prepareStatement(query);
    		preparedStmt.setString(1, name);
        	preparedStmt.setInt(2, item_id);
        	preparedStmt.setInt(3, userId);
        	preparedStmt.executeUpdate();
        	
        	// TODO: Update place on move
        	if(!name.equals(exists.getString("name"))) {
        		int place = updatePlaceOnMoveList(item_id, userId, name, exists.getString("name"), "recipe");
        		rm.setOrder(place);
        	}
        	
        	 
    	}else {
    		int count = countItemsInList(name, userId);
        	query = "INSERT INTO list_recipes (item_id, user_id, name) values (?, ?, ?)";
        	preparedStmt = conn.prepareStatement(query);
        	preparedStmt.setInt(1, item_id);
        	preparedStmt.setInt(2, userId);
        	preparedStmt.setString(3, name);
        	preparedStmt.executeUpdate();
        	preparedStmt.close();
        	
        	// add the place
        	query = "INSERT INTO places (item_id, user_id, name, place, restaurant_or_recipe) values (?, ?, ?, ?, ?)";
        	preparedStmt = conn.prepareStatement(query);
        	preparedStmt.setInt(1, item_id);
        	preparedStmt.setInt(2, userId);
        	preparedStmt.setString(3, name);
        	preparedStmt.setInt(4, count+1);
        	preparedStmt.setString(5, "recipe");
        	preparedStmt.executeUpdate();
        	rm.setOrder(count+1);
    	}
    	
    	preparedStmt.close();
    	 
		return ret;
	}
	
	public static int updatePlaceOnMoveList(int itemId, int userId, String newName, String oldName, String restaurant) throws Exception{

		int oldPlace = -1;
		
		Connection conn = getConnection();
		String query = "SELECT * from places where user_id=(?) and name=(?) and item_id=(?)";
		PreparedStatement preparedStmt = conn.prepareStatement(query);
		preparedStmt.setInt(1, userId);
    	preparedStmt.setString(2, oldName);
    	preparedStmt.setInt(3, itemId);
    	ResultSet rs = preparedStmt.executeQuery();
    	rs.next();
    	oldPlace = rs.getInt("place");
		
		// Up everything after the number on the old list by 1
		updatePlaceOnDelete(userId, oldName, oldPlace, restaurant);
		
		// Add to the end of the new list
		int count = countItemsInList(newName, userId);
		String update = "UPDATE places SET place=(?) , name=(?) where user_id=(?) and name=(?) and item_id=(?) and restaurant_or_recipe=(?)";
		preparedStmt = conn.prepareStatement(update);
		preparedStmt.setInt(1, count+1);
		preparedStmt.setString(2, newName);
    	preparedStmt.setInt(3, userId);
    	preparedStmt.setString(4, oldName);
    	preparedStmt.setInt(5, itemId);
    	preparedStmt.setString(6, restaurant);
    	preparedStmt.executeUpdate();
		
    	 
		return count+1;
	}
	
	
	private static boolean updatePlaceOnDelete(int userId, String oldName, int oldPlace, String restaurant) throws Exception{
		boolean ret = true;
		
		// Up everything after the number on the old list by 1
		String query = "SELECT * from places where user_id=(?) and name=(?)";
		Connection conn = getConnection();
		PreparedStatement preparedStmt = conn.prepareStatement(query);
    	preparedStmt.setInt(1, userId);
    	preparedStmt.setString(2, oldName);
    	ResultSet rs = preparedStmt.executeQuery();
    	while(rs.next()) {
    		if(rs.getInt("place") > oldPlace) {
    			int place = rs.getInt("place") - 1;
    			
    			String update = "UPDATE places SET place=(?) where user_id=(?) and name=(?) and item_id=(?)";
    			preparedStmt = conn.prepareStatement(update);
    			preparedStmt.setInt(1, place);
    	    	preparedStmt.setInt(2, userId);
    	    	preparedStmt.setString(3, oldName);
    	    	preparedStmt.setInt(4, rs.getInt("item_id"));
    	    	preparedStmt.executeUpdate();
    		}
    	}
		 
		return ret;
	}
	
	public static boolean deleteRecipe(RecipeModel rm, int searchId) throws Exception {
		int userId = getUserIdFromSearchId(searchId);
		int itemId = getItemIdFromRecipe(rm);
		
		Connection conn = getConnection();
		String query = "SELECT * from list_recipes where user_id=(?) and item_id=(?)";
		PreparedStatement preparedStmt = conn.prepareStatement(query);
    	preparedStmt.setInt(1, userId);
    	preparedStmt.setInt(2, itemId);
    	ResultSet rsNext = preparedStmt.executeQuery();
		
    	if(rsNext.next()) {
    		// Delete from list
    		query = "DELETE from list_recipes where user_id=(?) and item_id=(?)";
    		preparedStmt = conn.prepareStatement(query);
        	preparedStmt.setInt(1, userId);
        	preparedStmt.setInt(2, itemId);
        	preparedStmt.executeUpdate();
    		
    		// Get old name and place
    		query = "SELECT * from places where user_id=(?) and item_id=(?) and restaurant_or_recipe=(?)";
    		preparedStmt = conn.prepareStatement(query);
        	preparedStmt.setInt(1, userId);
        	preparedStmt.setInt(2, itemId);
        	preparedStmt.setString(3, "recipe");
        	ResultSet rs = preparedStmt.executeQuery();
    		rs.next();
    		int place = rs.getInt("place");
    		String name = rs.getString("name");
    		
    		query = "DELETE from places where user_id=(?) and item_id=(?) and restaurant_or_recipe=(?)";
    		preparedStmt = conn.prepareStatement(query);
        	preparedStmt.setInt(1, userId);
        	preparedStmt.setInt(2, itemId);
        	preparedStmt.setString(3, "recipe");
        	preparedStmt.executeUpdate();
        	
    		updatePlaceOnDelete(userId, name, place, "recipe");
    	}
		
		
		 
		return true;
	}
	
	public static boolean deleteRestaurant(RestaurantModel rm, int searchId) throws Exception {
		int userId = getUserIdFromSearchId(searchId);
		int itemId = getItemIdFromRestaurant(rm);
		
		String query = "SELECT * from list_restaurants where user_id=(?) and item_id=(?)";
		Connection conn = getConnection();
		PreparedStatement preparedStmt = conn.prepareStatement(query);
    	preparedStmt.setInt(1, userId);
    	preparedStmt.setInt(2, itemId);
    	ResultSet rs = preparedStmt.executeQuery();
    	
    	if(rs.next()) {
    		// Delete from list
    		query = "DELETE from list_restaurants where user_id=(?) and item_id=(?)";
    		preparedStmt = conn.prepareStatement(query);
        	preparedStmt.setInt(1, userId);
        	preparedStmt.setInt(2, itemId);
        	preparedStmt.executeUpdate();
    		
    		// Get old name and place
    		query = "SELECT * from places where user_id=(?) and item_id=(?) and restaurant_or_recipe=(?)";
    		preparedStmt = conn.prepareStatement(query);
        	preparedStmt.setInt(1, userId);
        	preparedStmt.setInt(2, itemId);
        	preparedStmt.setString(3, "restaurant");
        	rs = preparedStmt.executeQuery();
    		rs.next();
    		int place = rs.getInt("place");
    		String name = rs.getString("name");

    		
    		query = "DELETE from places where user_id=(?) and item_id=(?) and restaurant_or_recipe=(?)";
    		preparedStmt = conn.prepareStatement(query);
        	preparedStmt.setInt(1, userId);
        	preparedStmt.setInt(2, itemId);
        	preparedStmt.setString(3, "restaurant");
        	preparedStmt.executeUpdate();
        	updatePlaceOnDelete(userId, name, place, "restaurant");
    	}
    	 
		return true;
	}
	
	public static boolean updatePlaceOnMoveUpDown(int itemId, int searchId, String name, int oldPlace, int newPlace,
			String restaurant) throws Exception{
		
		int userId = getUserIdFromSearchId(searchId);
		boolean ret = true;
		
		String query = "SELECT * from places where user_id=(?) and name=(?)";
		Connection conn = getConnection();
		PreparedStatement preparedStmt = conn.prepareStatement(query);
		preparedStmt.setInt(1, userId);
    	preparedStmt.setString(2, name);
    	ResultSet rs = preparedStmt.executeQuery();
		
		// Move Up
		if(newPlace < oldPlace) {
			// down the places of all items in between [newPlace, oldPlace)
			while(rs.next()) {
				int place = rs.getInt("place");
				if(place < oldPlace && place >= newPlace) {
					query = "UPDATE places set place = (?) where item_id = (?) and name = (?) and user_id = (?)";
					preparedStmt = conn.prepareStatement(query);
					preparedStmt.setInt(1, place+1);
					preparedStmt.setInt(2, rs.getInt("item_id"));
			    	preparedStmt.setString(3, name);
			    	preparedStmt.setInt(4, userId);
			    	preparedStmt.executeUpdate();
				}
			}
			
		}else {
			// up all the places of the items in between (oldPlace, newPlace]
			while(rs.next()) {
				int place = rs.getInt("place");
				if(place > oldPlace && place <= newPlace) {
					query = "UPDATE places set place = (?) where item_id = (?) and name = (?) and user_id = (?)";
					preparedStmt = conn.prepareStatement(query);
					preparedStmt.setInt(1, place-1);
					preparedStmt.setInt(2, rs.getInt("item_id"));
			    	preparedStmt.setString(3, name);
			    	preparedStmt.setInt(4, userId);
			    	preparedStmt.executeUpdate();
				}
			}
		}
		
		// update the place of the item
		query = "UPDATE places set place = (?) where item_id = (?) and name = (?) and user_id = (?) and restaurant_or_recipe = (?)";
		preparedStmt = conn.prepareStatement(query);
		preparedStmt.setInt(1, newPlace);
		preparedStmt.setInt(2, itemId);
    	preparedStmt.setString(3, name);
    	preparedStmt.setInt(4, userId);
    	preparedStmt.setString(5, restaurant);
    	preparedStmt.executeUpdate();
		 
		return ret;
	}
}

