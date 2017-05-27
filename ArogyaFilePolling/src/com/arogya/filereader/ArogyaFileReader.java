package com.arogya.filereader;

import java.sql.DriverManager;
import org.apache.commons.io.FilenameUtils;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

public class ArogyaFileReader {

	private static final String DB_DRIVER = "com.mysql.jdbc.Driver";
	private static final String DB_CONNECTION = "jdbc:mysql://localhost:3306/ArogyaStagingDB";
	private static final String DB_USER = "root";
	private static final String DB_PASSWORD = "root";
	
    static File directory = null; 
	static Connection dbConnection = null;
	static PreparedStatement preparedStatement1 = null;
	static PreparedStatement preparedStatement = null;


	public static void main(String[] argv) {
		
		directory = new File("/home/hduser/Documents/ServerInput1");

		try {

			insertRecordIntoTable();

		} catch (SQLException e) {

			System.out.println(e.getMessage());

		}

	}

	private static void insertRecordIntoTable() throws SQLException {

		//Connection dbConnection = null;
		//PreparedStatement preparedStatement = null;
		//PreparedStatement preparedStatement1 = null;

		dbConnection = getDBConnection();
		
		 String query = "select * from ArogyaStage where customer_id = ? "
		 	            + "and file_timestamp = ?";
		 
		 String insertTableSQL = "INSERT INTO ArogyaStage"
				+ "(CUSTOMER_ID, FILE_LOCATION, PROCESSED, TRANSMITTED, File_TIMESTAMP) VALUES"
				+ "(?,?,?,?,?)";

		try {
			
			
			String s[] = directory.list();
			System.out.println("total files are " + s.length);
			/*
			for (int i=0; i < s.length; i++) {
			File f = new File(directory + "/" + s[i]);
			if (f.isDirectory()) {
			System.out.println(s[i] + " is a directory");
			} else {
			System.out.println(s[i] + " is a file");
			}
			}
			*/
			
		      File[] fList = directory.listFiles();
		      
		      for (File file : fList){
		      
		    	  if (!file.isDirectory()){
		    		  
		              System.out.println(file.getName());
		           
		              if( FilenameUtils.isExtension(file.getName(),"HL7")) {
 
		              String fname = file.getName();
		              
		              StringTokenizer tok = new StringTokenizer(file.getName(), "_ .");
		              String custid = tok.nextToken();
		              String timestamp = tok.nextToken();
		           
		  //            System.out.println(timestamp);
		              
		              DateFormat formatter;
		              formatter = new SimpleDateFormat("yyyymmddhhmmss");
		              Date date = (Date) formatter.parse(timestamp);
		              
		              Timestamp timeStampDate = new Timestamp(date.getTime());

		    //          System.out.println(timeStampDate);
		              
		              SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		      //        System.out.println(sdf.format(timeStampDate)); //this will print without ms

		              
		              
		              //int customer = Integer.parseInt(custid);
		              
		              preparedStatement1 = dbConnection.prepareStatement(query);
		              
		              System.out.println("custid " + custid);
		              
		              preparedStatement1.setString(1, custid);
		              preparedStatement1.setString(2, sdf.format(timeStampDate));
		              ResultSet rs = preparedStatement1.executeQuery();
		              
		              //preparedStatement1.executeUpdate();
		            
		              if (!rs.next() ) {
		              
		              
					      System.out.println("Inserting Record to table! " + insertTableSQL);
					      
		            	  preparedStatement = dbConnection.prepareStatement(insertTableSQL);
		            	  preparedStatement.setString(1, custid);
		            	  preparedStatement.setString(2, directory+"/"+fname);
		            	  preparedStatement.setString(3, "Y");
		            	  preparedStatement.setString(4, "N");
		            	  preparedStatement.setString(5, sdf.format(timeStampDate));
			
			
		            	  //preparedStatement.setTimestamp(4, getCurrentTimeStamp());

		            	  // execute insert SQL stetement
		            	  preparedStatement.executeUpdate();
			

		            	  System.out.println("Record is inserted into table!");
			       
			       
		              }
		              else {
		            	  System.out.println("Record is already present  into DBUSER table!");
		              }
		             } 
		          }
		      }

		} catch (SQLException | ParseException e) {

			System.out.println(e.getMessage());

		} finally {

			if (preparedStatement != null) {
				preparedStatement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}

		}

	}

	private static Connection getDBConnection() {

		Connection dbConnection = null;

		try {

			Class.forName(DB_DRIVER);

		} catch (ClassNotFoundException e) {

			System.out.println(e.getMessage());

		}

		try {

			dbConnection = DriverManager.getConnection(
                            DB_CONNECTION, DB_USER,DB_PASSWORD);
			return dbConnection;

		} catch (SQLException e) {

			System.out.println(e.getMessage());

		}

		return dbConnection;

	}

	
}