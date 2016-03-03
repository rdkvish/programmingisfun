package com.rdkv.jwt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteTest {
	private static Connection con;

	public void run() throws Exception {

		// sqlite driver
		Class.forName("org.sqlite.JDBC");
		// database path, if it's new database,
		// it will be created in the project folder
		con = DriverManager.getConnection("jdbc:sqlite:user.db");
		// createTable(con);
		// insertUser(con, "Ram", "Kumar", "rkumar", "4567", "newRel");

		isUserExists(con, "rkumar");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			new SQLiteTest().run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void addUser(Connection connection) throws SQLException {
		// inserting data
		PreparedStatement prep = connection
				.prepareStatement("insert into user values(?,?,?,?,?,?);");
		prep.setString(2, "Deepak");
		prep.setString(3, "Kumar");
		prep.setString(4, "rdkv");
		prep.setString(5, "1234");
		prep.setString(6, "relationship");
		prep.execute();
	}

	public static void insertUser(Connection connection, String fname,
			String lname, String uname, String pass, String relName)
			throws SQLException {
		// inserting data
		PreparedStatement prep = connection
				.prepareStatement("insert into user values(?,?,?,?,?,?);");
		prep.setString(2, fname);
		prep.setString(3, lname);
		prep.setString(4, uname);
		prep.setString(5, pass);
		prep.setString(6, relName);
		prep.execute();
	}

	public static void createTable(Connection connection) throws SQLException {
		Statement stat = connection.createStatement();
		stat.executeUpdate("drop table if exists user;");

		// creating table
		stat.executeUpdate("create table user(id integer,"
				+ "firstName varchar(30)," + "lastName varchar(30),"
				+ "userName varchar(30) UNIQUE," + "password varchar(30),"
				+ "relationshipName varchar(30)," + "primary key (id));");
		stat.close();
	}

	public static void getAllTableData(Connection connection)
			throws SQLException {
		// getting data
		Statement stat = connection.createStatement();
		ResultSet res = stat.executeQuery("select * from user");
		while (res.next()) {
			System.out.println(res.getString("id") + " "
					+ res.getString("firstName") + " "
					+ res.getString("lastName") + " "
					+ res.getString("userName") + " "
					+ res.getString("password") + " "
					+ res.getString("relationshipName"));
		}
	}

	public static void getUserData(Connection connection) throws SQLException {
		// getting data
		Statement stat = connection.createStatement();
		ResultSet res = stat
				.executeQuery("select * from user where userName='rdkv';");

		System.out.println(res.getString("id") + " "
				+ res.getString("firstName") + " " + res.getString("lastName")
				+ " " + res.getString("userName") + " "
				+ res.getString("password") + " "
				+ res.getString("relationshipName"));

	}

	public static ResultSet getUserData(Connection connection, String dbQuery)
			throws SQLException {
		// getting data
		Statement stat = connection.createStatement();
		ResultSet res = stat.executeQuery(dbQuery);

		return res;

	}

	public static boolean isUserExists(Connection connection, String userName)
			throws SQLException {
		String query = "select * from user where userName='" + userName + "';";

		ResultSet res = getUserData(connection, query);

		if (res.isClosed()) {
			System.out.println("User not found");
			return false;
		} else {
			System.out.println(res.getString("id") + " "
					+ res.getString("firstName") + " "
					+ res.getString("lastName") + " "
					+ res.getString("userName") + " "
					+ res.getString("password") + " "
					+ res.getString("relationshipName"));
			return true;
		}

	}

}