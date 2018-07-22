package com.test.playground;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DirectInsert {
	public static void main(String[] args) throws Exception {

		Connection con = null;
		String url = "jdbc:hsqldb:file:testdb";
		Class.forName("org.hsqldb.jdbcDriver");
		con = DriverManager.getConnection(url, "sa", "1");
		Statement statement = con.createStatement();
		Statement stm1 = con.createStatement();

		statement.execute("INSERT INTO EVENTENTRY VALUES('EventEntry','23',FALSE,3,NULL,NULL); ");
		ResultSet rs = stm1.executeQuery("SELECT * FROM \"EVENTENTRY\"");
		while (rs.next()) {
			System.out.println(rs.getString("id"));
		}

		stm1.close();
		statement.close();
		con.close();

	}

}
