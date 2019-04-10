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

public class DatabaseModel {
	static final String SQL_PASSWORD = "NewPassword"; // SET YOUR MYSQL PASSWORD HERE TO GET DATABASE WORKING!!!!!!!!
	
	public static int signInUser(String username, char[] password) throws Exception {
		
		// Get from SQL
		Connection conn = null;
		ResultSet rs = null;
		int returnVal = -1;
		
		conn = getConnection();
		
		// the mysql insert statement to have date of upload
		String query = "SELECT (user_id) from users where user_name = (?) and user_password = SHA1(?)";
		PreparedStatement preparedStmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		preparedStmt.setString (1, username);
		preparedStmt.setString(2, String.valueOf(password));
		rs = preparedStmt.executeQuery();
		if(rs.next()) {
			returnVal = rs.getInt("user_id");
		}
		close(conn, preparedStmt, rs);
		return returnVal;
	}
	public static Connection getConnection() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		//replace 68.183.168.73 with localhost to use local DB
		return DriverManager.getConnection("jdbc:mysql://localhost:3306/ImHungry?user=root&password=" + SQL_PASSWORD + "&useSSL=false&allowPublicKeyRetrieval=true");
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
		close(conn, preparedStmt, rs);
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
			int affectedRows = preparedStmt.executeUpdate();
			
			close(conn, preparedStmt, null);	
		}
		return returnVal;
	}
	
	private static void close(Connection conn, PreparedStatement st, ResultSet rs) throws Exception {
		// Result sets, statements do not need to be closed, as they are ended by the connectionProxy closing
		conn.close();
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
		conn.close();
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
		conn.close();
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
		conn.close();
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
		conn.close();
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
		conn.close();
		
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
		close(conn, preparedStmt, rs);
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
		conn.close();
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
		Gson gson = new Gson();
		String json = gson.toJson(restaurant);
		String sql = "INSERT INTO restaurants (search_id, json_string) VALUES (?, ?)";
		Connection conn = getConnection();
		PreparedStatement ps = conn.prepareStatement(sql);
		
		ps.setInt(1, searchId);
		ps.setString(2, json);
		
		ps.executeUpdate();
		
		conn.close();
		return true;
	}
	
	public static boolean insertRecipe(RecipeModel recipe, int searchId) throws Exception{
		Gson gson = new Gson();
		String json = gson.toJson(recipe);
		String sql = "INSERT INTO recipes (search_id, json_string) VALUES (?, ?)";
		Connection conn = getConnection();
		PreparedStatement ps = conn.prepareStatement(sql);
		
		ps.setInt(1, searchId);
		ps.setString(2, json);
		
		ps.executeUpdate();
		
		conn.close();
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
	
}
