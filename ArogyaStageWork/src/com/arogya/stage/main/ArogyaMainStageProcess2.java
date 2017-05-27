package com.arogya.stage.main;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.text.WordUtils;

//import io.HL7FileReader;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.util.StringUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;

import com.arogya.stage.common.Constants;
import com.arogya.stage.dao.ArogyaDBdao;
import com.arogya.stage.dbstruct.ArogyaStageDB;
import com.arogya.stage.hashmap.ReferenceMaps;
import com.arogya.stage.loinc.Loinc;
import com.arogya.stage.mail.ArogyaMail;
import com.arogya.stage.mail.RegistredUser;
import com.arogya.stage.obx.OBxFlagdetails;
import com.arogya.stage.prop.ReadProp;
import com.arogya.stage.sms.WaySms;
import com.google.common.base.CaseFormat;
import com.hl7process.HL7ContentException;
//import com.hl7process.ResultSet;
//import com.hl7process.XMLConverter;
import com.hl7process.io.HL7FileReader;
import com.mysql.jdbc.Connection;

public class ArogyaMainStageProcess2 {

	static Configuration config = HBaseConfiguration.create();

	public static String custid;
	public static String timestamp;
	public static HTable hJSONTable, hHL7Table, hHL7FlagTable;

	public static SimpleDateFormat sdf;
	public static Timestamp timeStampDate;
	public static File fname;
	public static String filename;

	public static OBxFlagdetails obx1;
	// public static ArrayList<OBxFlagdetails> OBxFlagRecArray;
	public static ArogyaDBdao arogyafilelocationDAO;
	// public static String OUTPUT_FILE =
	// "/home/hduser/Documents/xmlout/xmlop3.xml";

	public static ReadProp readPropFile;

	private static ReferenceMaps maps = new ReferenceMaps();
	public static HashMap<String, String> map = new HashMap<String, String>();
	public static OBxFlagdetails st;
	public static JSONObject jsonout;
	public static String[] lines;
	public static StringBuilder obx;

	// hbase key

	public static String patientid, patientemailid;
	public static String obrtimestamp;
	public static String year;
	public static String OBXFlag;
	public static String testid;
	public static String obxAbnormalFlag;
	public static String obxValue = null;
	public static String lowRangeValue = null;
	public static String highRangeValue = null;

	public static String obrUniversalID;

	public static String obxJsonHbaseRowKey, obxHL7HbaseRowKey, obxJsonHbaseDelRowKey;
	public static RegistredUser reguser;
	public static HashMap<String, RegistredUser> sendmailusers, readusers;
	public static HashMap<String, OBxFlagdetails> OBxFlaghash;
	public static Loinc loincRead;

	@SuppressWarnings("fallthrough")
	public static void main(String[] args) throws Exception {

		File fname = null;

		config.clear();
		//**this properties for sameer's machine
		config.set("hbase.zookeeper.quorum", "192.168.198.129");
		config.set("hbase.zookeeper.property.clientPort", "2181");
		config.set("hbase.master", "192.168.198.129:60000");

		//this is for my machine
		//config.set("hbase.zookeeper.quorum", "master");
		//config.set("hbase.zookeeper.property.clientPort", "2222");

		SparkConf conf = new SparkConf().setMaster("local[2]").setAppName("Arogya Stage processing App");

		JavaSparkContext sc = new JavaSparkContext(conf);

		SparkSession spark = SparkSession.builder().appName("Arogya Stage processing App")
				.config("spark.some.config.option", "some-value").getOrCreate();

		arogyafilelocationDAO = new ArogyaDBdao();
		readPropFile = new ReadProp();

		reguser = new RegistredUser();

		sendmailusers = new HashMap<String, RegistredUser>();
		OBxFlaghash = new HashMap<String, OBxFlagdetails>();

		loincRead = new Loinc();

		try {
			List<ArogyaStageDB> fileLocationlist = arogyafilelocationDAO.getFileLocationDtl();

			final byte[] report = Bytes.toBytes("report");

			// Instantiating HTable class
			// HTable hTable;

			hJSONTable = new HTable(config, "arogyamain");
			hHL7Table = new HTable(config, "arogyamining");

			hHL7FlagTable = new HTable(config, "customerflags");
		
		//	hJSONTable = new HTable(config, "arogya20");
		//	hHL7Table = new HTable(config, "arogya17");

		//	hHL7FlagTable = new HTable(config, "arogya19");

			// System.out.println("connected to table");

			for (ArogyaStageDB str : fileLocationlist) {
				filename = str.getFile_Location();

				fname = new File(filename);
				System.out.println("fname1 : " + fname);

				StringTokenizer tok = new StringTokenizer(fname.getName(), "_ .");
				custid = tok.nextToken();
				timestamp = tok.nextToken();

				DateFormat formatter;
				formatter = new SimpleDateFormat("yyyymmddhhmmss");
				Date date = (Date) formatter.parse(timestamp);

				// Timestamp timeStampDate = new Timestamp(date.getTime());
				timeStampDate = new Timestamp(date.getTime());

				// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd
				// HH:mm:ss");

				sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

				InputStream in = System.in;
				boolean verbose = false;

				in = new FileInputStream(fname);
				HL7FileReader reader = new HL7FileReader(in);

				Document doc = XMLConverter.createDocument();

				while (true) {

					try {

						XMLConverter.appendMessage(doc.getDocumentElement(), reader.readMessage(), !verbose);

					} catch (EOFException | HL7ContentException e) {
						break;
					}
				}

				reader.close();

				XMLConverter.stream(doc, System.out);

				transform_xml_to_json();

				writeToHL7HbaseTables();

				// prepareToSendMailNotification();
			}

		} catch (SQLException | ParseException | IOException e1) {

			e1.printStackTrace();

		}

		writeToHL7HbaseFlagTable();

	    sendeMailNotification();

		spark.stop();

	}

	public static void sendeMailNotification() {

		System.out.println("Send email notification");

		ArogyaMail email = new ArogyaMail();
		WaySms sendSMS = new WaySms();

		// readusers = sendmailusers.get(key)

		for (Map.Entry<String, RegistredUser> usr : sendmailusers.entrySet()) {
			String custid = usr.getValue().getCustomerid();
			System.out.println("HASH cust id " + custid);

			String to = usr.getValue().getEmailid();

			boolean sentmail = email.sendemail(to);

			if (!sentmail) {
				System.out.println("unable to send mail");
			}

			String phno = usr.getValue().getPhoneno();
			
	     // Register to way2sms first and use same login id /pwd below
		//	
		//	sendSMS.login("9673676894", "xxxx");
			
		//	sendSMS.sendSMS("9673676894", Constants.msgText);
		//  sendSMS.sendSMS(phno, Constants.msgText);

		}

	}

	public static void prepareToSendMailNotification() {

		boolean found = false;

		for (Map.Entry<String, RegistredUser> usr1 : sendmailusers.entrySet()) {

			String tmpcustid = usr1.getValue().getCustomerid();
			// System.out.println("HASH cust id " + tmpcustid);

			if (tmpcustid.equals(patientid)) {
				System.out.println("Customer already added to hashmap");
				found = true;
			}

			// ... You can retrieve other features as well.
		}
		if (!found) {

			System.out.println("Customer needs to be added to hashmap");
			String query = "select customerid, emailid, phoneno from registereduser where customerid = ? ";

			PreparedStatement preparedStatement2;

			// sendmailusers.put("a",new RegistredUser("s@s.com", "White",
			// "123"));
			// sendmailusers.put("b",new RegistredUser("as@as.com", "White",
			// "234"));1

			try {

				preparedStatement2 = arogyafilelocationDAO.getConnection().prepareStatement(query);
				preparedStatement2.setString(1, patientid);
				ResultSet rs2 = preparedStatement2.executeQuery();

				if (!rs2.next()) {

					System.out.println("User  is not registered in registereduser table " + patientid);

				} else {

					patientemailid = rs2.getString("emailid");
					String phno = rs2.getString("phoneno");

					RegistredUser r1 = new RegistredUser(patientid, patientemailid, phno);

					sendmailusers.put(patientid, r1);
					System.out.println("R1 values are  " + r1.getEmailid());

				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

	}

	public static void transform_xml_to_json() {

		// OBxFlagRecArray = new ArrayList<OBxFlagdetails>();

		// String query="select loinc_num, component from loinc where loinc_num
		// = ? ";
		StringBuilder sb = new StringBuilder();

		PreparedStatement preparedStatement1;
		String opfile = Constants.OUTPUT_FILE;

		try (BufferedReader br = new BufferedReader(new FileReader(opfile))) {

			String sCurrentLine;

			while ((sCurrentLine = br.readLine()) != null) {

				if (sb.length() > 0) {
					sb.append("\n");
				}
				sb.append(sCurrentLine);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		// replace HL7 tags with their meaning/component name

		String str = sb.toString();

		lines = str.split("\n");
		String requestedDateTime = null;
		String loinc_str = null;
		// String Component;
		int j, k;

		System.out.println("XML File ");

		for (int i = 0; i < lines.length; i++) {
			// System.out.println(lines[i]);

			// retrieve patient id
			if (lines[i].contains("<PID.3.1>")) {
				patientid = null;
				// patientid = lines[i].substring(17, (lines[i].length() - 10));
				patientid = returnXMLSubStringValue(lines[i], "<PID.3.1>", "</PID.3.1>");
				System.out.println(patientid);

			}

			if (lines[i].contains("<OBR>")) {

				for (j = i; j < lines.length; j++) {
					// System.out.println(lines[j]);

					if (lines[j].contains("</OBR>")) {
						i = j;
						break;
					} else
					// if (lines[j].contains("<Requested_date_and_time>") ) {
					if (lines[j].contains("<OBR.7>")) {

						requestedDateTime = returnXMLSubStringValue(lines[j], "<OBR.7>", "</OBR.7>");

						obrtimestamp = requestedDateTime;
						// System.out.println("obrtimestamp "+ obrtimestamp);
						i = j;
						// break;

					} else if (lines[j].contains("<OBR.4.2>")) {
						// System.out.println("OBR " + lines[j] + "length "
						obrUniversalID = returnXMLSubStringValue(lines[j], "<OBR.4.2>", "</OBR.4.2>");

						String sb1 = toCamelCase(obrUniversalID);

						// String sb1 =
						// CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_CAMEL,
						// obrUniversalID);

						obrUniversalID = sb1.replaceAll(" ", "");

						// obrUniversalID =
						// StringUtils.camelize(obrUniversalID);
						// obrUniversalID = StringUtils.re
						// .remove(WordUtils.capitalizeFully(obrUniversalID,
						// '_'), "_");

						System.out.println("obrUniversalID " + obrUniversalID);
						i = j;
						// break;
					}

				} // end of for j=i

				i = j;

			} // end of if OBR

			obx = new StringBuilder();

			if (lines[i].contains("<OBX>")) {
				// obx.append(lines[i]);
				System.out.print("input OBX record for processing");

				for (k = i; k < lines.length; k++) {
					System.out.println(lines[k]);

					performOBXprocessing(k);

				} // end of for k=i

			} // end of if obx

		} // end of for i = 0

	}

	// to function to camelize the universal id and remove space

	static String toCamelCase(String s) {
		String[] parts = s.split(" ");
		String camelCaseString = "";
		for (String part : parts) {
			if (part != null && part.trim().length() > 0)
				camelCaseString = camelCaseString + toProperCase(part);
			else
				camelCaseString = camelCaseString + part + " ";
		}
		return camelCaseString;
	}

	static String toProperCase(String s) {
		String temp = s.trim();
		String spaces = "";
		if (temp.length() != s.length()) {
			int startCharIndex = s.charAt(temp.indexOf(0));
			spaces = s.substring(0, startCharIndex);
		}
		temp = temp.substring(0, 1).toUpperCase() + spaces + temp.substring(1).toLowerCase() + " ";
		return temp;

	}

	// OBX processing logic

	public static void performOBXprocessing(int k) {

		if (lines[k].contains("</OBX>")) {
			String jsonstr = obx.toString();

			// System.out.println("INPUT XML with hash values : "
			// +jsonstr);

			for (String key : maps.getObxMap().keySet()) {

				jsonstr = jsonstr.replace(key, maps.getObxMap().get(key));
			}

			try {

				// System.out.println("Modified XML with hash values
				// : " +jsonstr);
				year = obrtimestamp.substring(0, 4);

				prepareOBXFlagHashTable();

				jsonout = org.json.XML.toJSONObject(jsonstr);
				// System.out.println(jsonout.toString());

				writeToJsonHbaseTables();

				prepareToSendMailNotification();

			} catch (JSONException e) {
				e.printStackTrace();
			}

			// break;

		} /// end of if /obx

		else {
			if (lines[k].trim().equals("<OBX.3.1>")) {
				// System.out.println("Only OBX.3.1 header. Need to
				// process OBX.3.1.1");
				obx.append(lines[k]);
				// continue;

			} else

			if ((lines[k].contains("<OBX.3.1>")) || lines[k].contains("<OBX.3.1.1>")) {

				String s = lines[k];
				String strPart1 = null, strPart2 = null;

				if (lines[k].trim().contains("<OBX.3.1>")) {

					strPart1 = "<OBX.3.1>";
					strPart2 = "</OBX.3.1>";
				}
				if (lines[k].trim().contains("<OBX.3.1.1>")) {

					strPart1 = "<OBX.3.1.1>";
					strPart2 = "</OBX.3.1.1>";
				}

				// System.out.println(s.trim());

				int length = lines[k].trim().length();

				String loincNum = returnXMLSubStringValue(lines[k], strPart1, strPart2);

				testid = loincNum;
				boolean found = loincRead.findLoincComponent(loincNum,
						arogyafilelocationDAO.getConnection());
				
				if (found) {

					String sCurrentLinetemp = null;
					sCurrentLinetemp = "<OBX.3.1>" + loincRead.getComponent() + "</OBX.3.1>";
					lines[k] = sCurrentLinetemp;
				}


				obx.append(lines[k]);

			} // end of if obx.3.1

			else {
				if ((lines[k].contains("<OBX.5.1>")) || lines[k].contains("<OBX.5.1.1>")) {

					String s = lines[k];
					String strPart1 = null, strPart2 = null;

					if (lines[k].trim().contains("<OBX.5.1>")) {

						strPart1 = "<OBX.5.1>";
						strPart2 = "</OBX.5.1>";
					}
					if (lines[k].trim().contains("<OBX.5.1.1>")) {

						strPart1 = "<OBX.5.1.1>";
						strPart2 = "</OBX.5.1.1>";
					}
					// if (lines[k].trim().equals("</OBX.5>")) {

					System.out.println("value  string coversion " + lines[k].toString());

					// obxValue = returnXMLSubStringValue(lines[k], "<OBX.5>",
					// "</OBX.5>");
					obxValue = returnXMLSubStringValue(lines[k], strPart1, strPart2);

					System.out.println("obxValue: " + obxValue);

					obx.append(lines[k]);

				} else {
					// OBX Read and store actual result value
					if (lines[k].trim().contains("<OBX.5>")) {
						System.out.println("OBX Read and store actual result value");
						if (lines[k].trim().contains("</OBX.5>")) {

							obxValue = returnXMLSubStringValue(lines[k], "<OBX.5>", "</OBX.5>");
							System.out.println("obxValue: " + obxValue);
						}
						obx.append(lines[k]);
					}

					else

					{
						// OBX Read and store reference Range
						if (lines[k].contains("<OBX.7>")) {

							System.out.println("refRange string coversion");
							String obxRefRange = returnXMLSubStringValue(lines[k], "<OBX.7>", "</OBX.7>");
							StringTokenizer refRange = new StringTokenizer(obxRefRange, " - ");

							// *System.out.println("refRange string " +
							// refRange.toString());
							lowRangeValue = refRange.nextToken();
							highRangeValue = refRange.nextToken();
							System.out.println("lowRangeValue string " + lowRangeValue);
							System.out.println("highRangeValue string " + highRangeValue);

							obx.append(lines[k]);
						}

						else

						{
							// OBX Abnormal Flag read logic
							if (lines[k].contains("<OBX.8>")) {
								obxAbnormalFlag = returnXMLSubStringValue(lines[k], "<OBX.8>", "</OBX.8>");
								System.out.println("obxAbnormalFlag " + obxAbnormalFlag);
								obx.append(lines[k]);
							}

							else

							// OBX Flag read logic
							if (lines[k].contains("<OBX.11>")) {

								OBXFlag = returnXMLSubStringValue(lines[k], "<OBX.11>", "</OBX.11>");

								// System.out.println("OBXFlag "+
								// OBXFlag);
								obx.append(lines[k]);
							}

							else {
								if (!lines[k].contains("<OBX>"))

									obx.append(lines[k]);
							}

						}
					}
				}
			}
		}

	}

	public static void prepareOBXFlagHashTable() {

		boolean found = false;

		for (Map.Entry<String, OBxFlagdetails> tmpflag : OBxFlaghash.entrySet()) {

			String tmpcustid = tmpflag.getValue().getCustid();

			if (tmpcustid.equals(patientid)) {
				System.out.println("Customer already added to obxflag hashmap");
				found = true;

			}

		}

		if (!found) {

			System.out.println("Customer needs to be added to obxflag hashmap : " + patientid);

			OBxFlagdetails obxflag1 = new OBxFlagdetails(patientid, testid, OBXFlag, obxAbnormalFlag, obxValue,
					lowRangeValue, highRangeValue);

			OBxFlaghash.put(patientid, obxflag1);

		}

	}

	public static String returnXMLSubStringValue(String str, String startTag, String endTag) {

		String s = null;
		String tempStr = null;
		String strPart1 = null, endPart1 = null;

		s = str;
		strPart1 = startTag;
		endPart1 = endTag;

		// System.out.println(s.trim());

		int length = s.trim().length();

		tempStr = s.trim().substring(strPart1.length(), (length - endPart1.length()));

		return tempStr;

	}

	public static void writeToHL7HbaseFlagTable() {
		// load Property file into hash table
		HashMap<String, String> map = readPropFile.LoadPropintoHashMap();

		for (Map.Entry<String, OBxFlagdetails> g : OBxFlaghash.entrySet()) {

			String tmpcustid = g.getValue().getCustid();

			String strHL7FlagHbaseRowKey = patientid;
			String tmpSegment = null;
			String abnormalFlag = null;

			for (Map.Entry m : map.entrySet()) {

				String segmentid = (String) m.getKey();
				// System.out.println(m.getKey()+" "+m.getValue());
				List<String> myList = new ArrayList<String>(Arrays.asList(((String) m.getValue()).split(",")));
				// System.out.println(myList);

				for (String temp : myList) {
					// if (temp.equals("1751-8"))
					String tmpTestid = g.getValue().getTestid();

					if (temp.equals(tmpTestid)) {
						tmpSegment = segmentid;
					} else {
						tmpSegment = "Other Parts";
					}
					int obxValueInt = 0;
					int lowRefRange;
					int highRefRange;

					if (g.getValue().getObxValue() == null)
						obxValueInt = 0;
					else {
						boolean res1 = NumberUtils.isNumber(g.getValue().getObxValue());
						if (res1) {
							obxValueInt = Integer.valueOf(g.getValue().getObxValue());
						}
					}

					if (g.getValue().getLowRangeValue() == null)
						lowRefRange = 0;
					else
						lowRefRange = Integer.valueOf(g.getValue().getLowRangeValue());

					if (g.getValue().getHighRangeValue() == null)
						highRefRange = 0;
					else
						highRefRange = Integer.valueOf(g.getValue().getHighRangeValue());

					/*
					 * abnormal flag set logic 1. if abnormal flag is not Normal
					 * then set flag R 2. if obx value is out of ref range then
					 * set flag R
					 * 
					 */

					abnormalFlag = "G";

					System.out.println("obxValueInt " + obxValueInt + " lowRefRange " + lowRefRange + " highRefRange "
							+ highRefRange + " getObxAbnormalFlag " + g.getValue().getObxAbnormalFlag());
					if ((obxValueInt <= lowRefRange) || (obxValueInt >= highRefRange))

						abnormalFlag = "R";

					if ((g.getValue().getObxAbnormalFlag() != null) && (g.getValue().getObxAbnormalFlag().length() > 0))
						if (g.getValue().getObxAbnormalFlag().equals("N"))
							continue;
						else
							abnormalFlag = "R";

				} // END OF FOR

			}

			if (abnormalFlag.equals("G") || abnormalFlag.equals("R")) {
				try {

					Put putrec2 = new Put(Bytes.toBytes(strHL7FlagHbaseRowKey));

					putrec2.addColumn(Bytes.toBytes("segment"), Bytes.toBytes(tmpSegment), Bytes.toBytes(abnormalFlag));

					hHL7FlagTable.put(putrec2);

				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			System.out.println("HBase Flag Table Record Inserted " + strHL7FlagHbaseRowKey);
		}

	}

	public static void writeToHL7HbaseTables() {

		obxHL7HbaseRowKey = patientid + "_" + obrtimestamp + "_" + year;

		Put putrec1 = new Put(Bytes.toBytes(obxHL7HbaseRowKey));

		try {

			putrec1.addColumn("report".getBytes(), "HL7".getBytes(), Files.readAllBytes(Paths.get(filename)));

			hHL7Table.put(putrec1);

			System.out.println("Record Inserted");

		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	public static void writeToJsonHbaseTables() {

		obxJsonHbaseRowKey = patientid + "_" + obrtimestamp + "_" + year + "_" + OBXFlag + "_" + testid;

		System.out.println("HBASE Json Row Key : " + obxJsonHbaseRowKey);

		if (OBXFlag.equals("C")) {
			// delete existing Final OBX record from Hbase table

			obxJsonHbaseDelRowKey = patientid + "_" + obrtimestamp + "_" + year + "_" + "F" + "_" + testid;

			// Instantiating Delete class
			Delete delete = new Delete(Bytes.toBytes(obxJsonHbaseDelRowKey));
			// delete.deleteColumn(Bytes.toBytes("report"),
			// Bytes.toBytes("OBX"));

			// deleting the data
			try {

				hJSONTable.delete(delete);
				System.out.println("HBase JSON Record deleted");

			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		// write to hbase table HL7 and JSON object

		Put putrec = new Put(Bytes.toBytes(obxJsonHbaseRowKey));

		// System.out.print("Universal id at the time of insert is "
		// +obrUniversalID);
		// putrec.addColumn("report".getBytes(), Bytes.toBytes("OBX" + "_" +
		// obrUniversalID),

		putrec.addColumn("report".getBytes(), Bytes.toBytes(obrUniversalID), Bytes.toBytes(jsonout.toString()));

		try {

			hJSONTable.put(putrec);

		} catch (IOException e) {

			e.printStackTrace();
		}

		System.out.println("HBase JSON Record Inserted");

	}

}