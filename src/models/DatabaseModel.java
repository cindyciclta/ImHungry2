package models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

public class DatabaseModel {
	static final String SQL_PASSWORD = "root"; // SET YOUR MYSQL PASSWORD HERE TO GET DATABASE WORKING!!!!!!!!

	
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
		return DriverManager.getConnection("jdbc:mysql://68.183.168.73:3306/ImHungry?user=root&password=" + SQL_PASSWORD + "&useSSL=false&allowPublicKeyRetrieval=true");
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
	public static boolean AddSearchToHistory(int userid, String term, int limit, int radius, ArrayList<String> urllist)
			throws Exception {

		if(userid < 0) {
			return false;
		}
		term = term.trim();
		Connection conn =null;
		PreparedStatement preparedStmt = null;
		
		conn = getConnection();
		String sql = "INSERT INTO searches (user_id, term, limit_search, radius) VALUES (?, ?, ?, ?)";
		
		preparedStmt = conn.prepareStatement(sql);
		preparedStmt.setInt(1, userid);
		preparedStmt.setString(2, term);
		preparedStmt.setInt(3, limit);
		preparedStmt.setInt(4, radius);
		preparedStmt.executeUpdate();
		
		String imgsql = "INSERT IGNORE INTO images (term, url) VALUES (?, ?)";
		PreparedStatement ps = null;
		for (int i = 0; i < urllist.size(); i++) {
			ps = conn.prepareStatement(imgsql);
			ps.setString(1, term);
			ps.setString(2, urllist.get(i));
			ps.executeUpdate();
		}
		if (ps != null ) {ps.close();}

		close(conn, preparedStmt, null);
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
}
