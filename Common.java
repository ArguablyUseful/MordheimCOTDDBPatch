


public class Common {

	public static java.sql.Connection Connect(String path)
	{
		java.sql.Connection conn = null;
		try {
			conn = java.sql.DriverManager.getConnection(path);
		} catch (java.sql.SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	public static java.sql.ResultSet Query(java.sql.Connection conn, String query)
	{
		java.sql.Statement stmt;
		java.sql.ResultSet res = null;
		try {
			stmt = conn.createStatement();
			res = stmt.executeQuery(query);
		} catch (java.sql.SQLException e) {
			e.printStackTrace();
		}
		return res;
	}
}
