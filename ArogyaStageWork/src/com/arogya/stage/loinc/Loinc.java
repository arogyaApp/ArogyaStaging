package com.arogya.stage.loinc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Loinc {

	private String Loinc_num;
	private String Component;

	public String getLoinc_num() {
		return Loinc_num;
	}

	public void setLoinc_num(String loinc_num) {
		Loinc_num = loinc_num;
	}

	public String getComponent() {
		return Component;
	}

	public void setComponent(String component) {
		Component = component;
	}

	public Loinc() {
	}

	public boolean findLoincComponent(String loinc_num, Connection connection)

	{
		boolean found = false;
		Loinc_num = loinc_num;

		String query = "select loinc_num, component from loinc where loinc_num = ? ";

		// String Component;

		System.out.println("Record to be search  in LOINC table " + Loinc_num);
		try {

			PreparedStatement st = (PreparedStatement) connection.prepareStatement(query);

			st.setString(1, Loinc_num);
			ResultSet rs = st.executeQuery();

			if (!rs.next()) {

				System.out.println("Record is not present in LOINC table " + Loinc_num);
				found = false;

			} else {

				Component = rs.getString("component");
				found = true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return found;
	}

}
