package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * SQL utilities.
 */
public class SQLHelper {

	/**
	 * Get a MySQL connection.
	 *
	 * @return A MySQL connection.
	 */
	private static Connection getConnection() {
		Connection connection = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/grading_db?" + "user=root");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return connection;
	}

	/**
	 * Perform read operation.
	 *
	 * @param sql SQL.
	 * @return Result.
	 */
	public static ResultSet performRead(String sql) {
		ResultSet resultSet = null;
		try {
			Connection connection = getConnection();
			Statement statement = connection.createStatement();
			resultSet = statement.executeQuery(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultSet;
	}

	/**
	 * Perform query  operation.
	 *
	 * @param sql SQL.
	 * @return Result.
	 */
	public static boolean performQuery(String sql) {
		Connection connection = getConnection();
		try {
			Statement statement = connection.createStatement();
			int executeUpdateResult = statement.executeUpdate(sql);
			if (executeUpdateResult == 1) {
				System.out.println("Insert successful");
				connection.close();
				return true;
			} else {
				System.out.println("Insert unsuccessful");
			}
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}