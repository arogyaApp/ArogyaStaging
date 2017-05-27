package com.arogya.stage.mail;

public class RegistredUser {

	private String customerid;
	private String emailid;
	// private String passwd;
	private String phoneno;
	// private String otp;

	public RegistredUser() {

	}

	public RegistredUser(String customerid, String emailid, String phoneno) {
		super();
		this.customerid = customerid;
		this.emailid = emailid;
		this.phoneno = phoneno;
	}

	public String getCustomerid() {
		return customerid;
	}

	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}

	public String getEmailid() {
		return emailid;
	}

	public void setEmailid(String emailid) {
		this.emailid = emailid;
	}

	public String getPhoneno() {
		return phoneno;
	}

	public void setPhoneno(String phoneno) {
		this.phoneno = phoneno;
	}

}
