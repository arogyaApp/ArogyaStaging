package com.arogya.stage.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.arogya.stage.db.ArogyaConnectionFactory;
import com.arogya.stage.db.ArogyaDBUtil;
import com.arogya.stage.dbstruct.ArogyaStageDB;

public class ArogyaDBdao {

	private Connection connection;

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	private Statement statement;

	public ArogyaDBdao() {
		connection = ArogyaConnectionFactory.getConnection();
	}

	public List<ArogyaStageDB> getFileLocationDtl() throws SQLException {

		String query = "SELECT * FROM ArogyaStage where Processed = 'Y' ";
		ResultSet rs = null;

		List<ArogyaStageDB> fileLocationlist = new ArrayList<ArogyaStageDB>();

		ArogyaStageDB arogyaStage = null;

		try {

			statement = connection.createStatement();
			rs = statement.executeQuery(query);

			while (rs.next()) {
				arogyaStage = new ArogyaStageDB();
				arogyaStage.setCustomer_Id(rs.getString("Customer_Id"));
				arogyaStage.setFile_Location(rs.getString("File_Location"));

				// arogyaStage.setProcessed(rs.getCharacterStream("processed"));
				arogyaStage.setFile_Timestamp(rs.getTimestamp("File_Timestamp"));
				// arogyaStage.setTransmitted(rs.getString("transmitted"));

				fileLocationlist.add(arogyaStage);
			}

		} finally {
			ArogyaDBUtil.close(rs);
			ArogyaDBUtil.close(statement);
		}

		return fileLocationlist;

	}

	public void dbclose() {

		ArogyaDBUtil.close(connection);

	}

}