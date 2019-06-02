

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Common {

	public static Connection Connect(String path)
	{
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(path);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	public static ResultSet Query(Connection conn, String query)
	{
		Statement stmt;
		ResultSet res = null;
		try {
			stmt = conn.createStatement();
			res = stmt.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}
}
