package com.wzr.dao.entity;

import java.sql.Timestamp;

public class CompleteDetail {
	
//	private int cmId;

	private int cdId;
	
	private Integer fromWs;
	
	private Integer fromEp;
	
	private Integer toWs;
	
	private Integer toEp;
	
	private String barCode;
	
	private Timestamp fromTime;
	
	private Timestamp toTime;

	public Timestamp getAddTime() {
		return addTime;
	}

	public void setAddTime(Timestamp addTime) {
		this.addTime = addTime;
	}

	public Integer getFromUserId() {
		return fromUserId;
	}

	public void setFromUserId(Integer fromUserId) {
		this.fromUserId = fromUserId;
	}

	public Integer getToUserId() {
		return toUserId;
	}

	public void setToUserId(Integer toUserId) {
		this.toUserId = toUserId;
	}

	private Timestamp addTime;
	
	private Integer fromUserId;
	
	private Integer toUserId;
	
//	public int getCmId() {
//		return cmId;
//	}
//
//	public void setCmId(int cmId) {
//		this.cmId = cmId;
//	}

	public int getCdId() {
		return cdId;
	}

	public void setCdId(int cdId) {
		this.cdId = cdId;
	}

	public Integer getFromWs() {
		return fromWs;
	}

	public void setFromWs(Integer fromWs) {
		this.fromWs = fromWs;
	}

	public Integer getFromEp() {
		return fromEp;
	}

	public void setFromEp(Integer fromEp) {
		this.fromEp = fromEp;
	}

	public Integer getToWs() {
		return toWs;
	}

	public void setToWs(Integer toWs) {
		this.toWs = toWs;
	}

	public Integer getToEp() {
		return toEp;
	}

	public void setToEp(Integer toEp) {
		this.toEp = toEp;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public Timestamp getFromTime() {
		return fromTime;
	}

	public void setFromTime(Timestamp fromTime) {
		this.fromTime = fromTime;
	}

	public Timestamp getToTime() {
		return toTime;
	}

	public void setToTime(Timestamp toTime) {
		this.toTime = toTime;
	}

	
}
