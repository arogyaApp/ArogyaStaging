package com.arogya.stage.sms;

import java.net.HttpURLConnection;


public class WaySms {//class name
    
 private static int responseCode = -1;
 private static String userCredentials = null;
 private static String cookie = null;
 private static String site = null;
 private static String token=null;
 private static Credentials credentials = new Credentials();

 


 private static void getSite() { //just to open the homepage
  URLConnector.connect("http://www.way2sms.com/", false, "GET", null, null);
  responseCode = URLConnector.getResponseCode();
  System.out.println(responseCode);
  if(responseCode != HttpURLConnection.HTTP_MOVED_TEMP && responseCode != HttpURLConnection.HTTP_OK)
   exit("getSite failed!" );
  else {
   site = URLConnector.getLocation();
   if(site != null)
    site = site.substring(7, site.length() - 1);
  }
  System.out.println(site);
  URLConnector.disconnect();
 }

/*

 if you type way2sms.com in URL then you can first of all it opens http://site23.way2sms.com/content/prehome.html? page then it redirects to http://site23.way2sms.com/content/index.html?.Here the Page tries to retrieve its previously stored cookies.Therefore we need this below preHome function

*/

 private static void preHome() {
  URLConnector.connect("http://" + site + "/content/prehome.jsp", false, "GET", null, null);
  responseCode = URLConnector.getResponseCode();
  System.out.println(responseCode);
  if(responseCode != HttpURLConnection.HTTP_MOVED_TEMP && responseCode != HttpURLConnection.HTTP_OK)
   exit("preHome failed" );
  else
   cookie = URLConnector.getCookie();
  token = cookie.substring(cookie.indexOf("~" )+ 1);
  URLConnector.disconnect();
 }

 public static void login(String uid, String pwd) {
  getSite(); //Homepage opens 
  preHome();  //pre home cookie retrieve done

  String location = null;

  credentials.set("username", uid);
  credentials.append("password", pwd);
  credentials.append("button", "Login" );
  userCredentials = credentials.getUserCredentials();

  URLConnector.connect("http://" + site + "/Login1.action", false, "POST", cookie, userCredentials);  //Here we send the retrieved cookie data to login
  responseCode = URLConnector.getResponseCode();
  System.out.println(responseCode);
  if(responseCode != HttpURLConnection.HTTP_MOVED_TEMP && responseCode != HttpURLConnection.HTTP_OK)
   exit("authentication failed!" );
  else
   location = URLConnector.getLocation();
  URLConnector.disconnect();

  URLConnector.connect(location, false, "GET", cookie, null);
  responseCode = URLConnector.getResponseCode();
  System.out.println(responseCode);
  if(responseCode != HttpURLConnection.HTTP_MOVED_TEMP && responseCode != HttpURLConnection.HTTP_OK)
   exit("redirection failed!" );
  URLConnector.disconnect();
 }

 /*
 public static void main(String[] args) {
		
		login("9673676894", "arogyasup");
		sendSMS("9673676894", "So ja");
		System.out.println("Message has been sent successfully!");
		
		}
 
 */
 
 public static void sendSMS(String receiversMobNo, String msg) {
 try
  {
credentials.reset();
credentials.append("Token", token);
credentials.append("message", msg);
credentials.append("mobile", receiversMobNo);
credentials.append("msgLen", "500" );
credentials.append("ssaction", "ss" );


userCredentials = credentials.getUserCredentials();
System.out.println("Token=" + token);
 
URLConnector.connect("http://" + site + "/quicksms.action", true, "POST", cookie, userCredentials);
URLConnector.setProperty("Token", token);
URLConnector.setProperty("message", msg);
URLConnector.setProperty("mobile", receiversMobNo);
URLConnector.setProperty("msgLen", "500" );
URLConnector.setProperty("ssaction", "ss" );
  }
  catch(Exception e)
  {
      System.out.println(e.getMessage());
  }

URLConnector.connect("http://" + site + "/smstoss.action" , true, "POST", cookie, credentials.getUserCredentials());

responseCode = URLConnector.getResponseCode();
System.out.println("IN "+responseCode);
if(responseCode != HttpURLConnection.HTTP_MOVED_TEMP && responseCode != HttpURLConnection.HTTP_OK)
exit("sendSMS failed!" );
URLConnector.disconnect();
}



private static void exit(String errorMsg) {
System.err.println(errorMsg);
System.exit(1);
}
}