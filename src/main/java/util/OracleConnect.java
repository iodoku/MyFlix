package util;

import java.sql.Connection;
import java.sql.DriverManager;

public class OracleConnect {
	public static Connection getConnection() {
		String driver = "oracle.jdbc.OracleDriver";
		
		String url = "jdbc:oracle:thin:@localhost:1521/XEPDB1";
		
		String id = "ezen";
		String pw = "ezen123!";
		
		Connection conn = null;
		
		try {
			Class.forName(driver);
			
			conn = DriverManager.getConnection(url, id, pw);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return conn;
	}
}
