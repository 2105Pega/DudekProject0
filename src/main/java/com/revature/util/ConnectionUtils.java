package com.revature.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionUtils {

	public static Connection getConnection() throws SQLException {
		try {

			Class.forName("org.postgresql.Driver"); // this will give the app the drivers needed to connect to our db

		} catch (ClassNotFoundException e) { // SQLException is a class that gets SQL
			e.printStackTrace();
		}

		String url = "";
		String username = "";
		String password = "";

		try {
			FileInputStream fileStream = new FileInputStream(
					"../DudekJDBCBank/src/test/resources/DB_Properties.properties");

			Properties prop = new Properties();
			prop.load(fileStream);

			url = prop.getProperty("URL");

			username = prop.getProperty("DB_Username");

			password = prop.getProperty("DB_Password");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		catch (IOException e2) {
			e2.printStackTrace();
		}

		catch (Exception e3) {
			e3.printStackTrace();
		}

		return DriverManager.getConnection(url, username, password);
	}

}
