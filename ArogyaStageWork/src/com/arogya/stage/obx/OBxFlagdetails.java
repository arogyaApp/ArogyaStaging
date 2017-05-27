package com.arogya.stage.obx;

public class OBxFlagdetails {

	private String custid;
	private String testid;
	private String obxFlag;
	private String obxAbnormalFlag;
	private String obxValue;
	private String lowRangeValue;
	private String highRangeValue;
	
	
	public OBxFlagdetails(String custid, String testid, String obxFlag, String obxAbnormalFlag, String obxValue,
			String lowRangeValue, String highRangeValue) {
		super();
		this.custid = custid;
		this.testid = testid;
		this.obxFlag = obxFlag;
		this.obxAbnormalFlag = obxAbnormalFlag;
		this.obxValue = obxValue;
		this.lowRangeValue = lowRangeValue;
		this.highRangeValue = highRangeValue;
	}


	public String getCustid() {
		return custid;
	}


	public void setCustid(String custid) {
		this.custid = custid;
	}


	public String getTestid() {
		return testid;
	}


	public void setTestid(String testid) {
		this.testid = testid;
	}


	public String getObxFlag() {
		return obxFlag;
	}


	public void setObxFlag(String obxFlag) {
		this.obxFlag = obxFlag;
	}


	public String getObxAbnormalFlag() {
		return obxAbnormalFlag;
	}


	public void setObxAbnormalFlag(String obxAbnormalFlag) {
		this.obxAbnormalFlag = obxAbnormalFlag;
	}


	public String getObxValue() {
		return obxValue;
	}


	public void setObxValue(String obxValue) {
		this.obxValue = obxValue;
	}


	public String getLowRangeValue() {
		return lowRangeValue;
	}


	public void setLowRangeValue(String lowRangeValue) {
		this.lowRangeValue = lowRangeValue;
	}


	public String getHighRangeValue() {
		return highRangeValue;
	}


	public void setHighRangeValue(String highRangeValue) {
		this.highRangeValue = highRangeValue;
	}
	
	
	

}