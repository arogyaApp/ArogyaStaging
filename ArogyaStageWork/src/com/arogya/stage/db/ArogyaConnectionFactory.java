package com.arogya.stage.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ArogyaConnectionFactory {

	// static reference to itself
	private static ArogyaConnectionFactory instance = new ArogyaConnectionFactory();
	public static final String URL = "jdbc:mysql://localhost/ArogyaStagingDB";
	public static final String USER = "root";
	public static final String PASSWORD = "root";
	public static final String DRIVER_CLASS = "com.mysql.jdbc.Driver";

	private ArogyaConnectionFactory() {

		try {
			Class.forName(DRIVER_CLASS);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private Connection createConnection() {

		Connection connection = null;

		try {
			connection = DriverManager.getConnection(URL, USER, PASSWORD);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return connection;

	}

	public static Connection getConnection() {
		return instance.createConnection();
	}

}