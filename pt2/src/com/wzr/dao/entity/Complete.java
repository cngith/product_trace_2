package com.wzr.dao.entity;

import java.sql.Timestamp;

public class Complete {

	private int cmId;
	
	public int getCmId() {
		return cmId;
	}

	public void setCmId(int cmId) {
		this.cmId = cmId;
	}

	//@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Timestamp cmDatetime;

	public Timestamp getCmDatetime() {
		return cmDatetime;
	}

	public void setCmDatetime(Timestamp cmDatetime) {
		this.cmDatetime = cmDatetime;
	}

	private int userid;

	private int billType;
	
	public int getBillType() {
		return billType;
	}

	public void setBillType(int billType) {
		this.billType = billType;
	}


	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	

	
	
}
