package com.exam.code.refactoring;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

public class ConnectionBd {

	private static Properties connectionProps = new Properties();
	
	private ConnectionBd() {}
	
	public static Connection getConnection(Map<String, String> dbParams) throws SQLException{
		
		connectionProps.put("user", dbParams.get("userName"));
		connectionProps.put("password", dbParams.get("password"));

		return DriverManager.getConnection("jdbc:" + dbParams.get("dbms") + "://" + dbParams.get("serverName")
				+ ":" + dbParams.get("portNumber") + "/", connectionProps);
	}
	
}
